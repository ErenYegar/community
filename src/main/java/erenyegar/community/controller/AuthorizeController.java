package erenyegar.community.controller;

import erenyegar.community.dto.AccessTokenDTO;
import erenyegar.community.dto.GithubUser;
import erenyegar.community.mapper.UserMapper;
import erenyegar.community.model.User;
import erenyegar.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")//配置github.client.id的k_value来过来赋值，读入map里
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private  String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){//spring自动将上下文里的request放入此方法里
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //githubProvider.getAccessToken(accessTokenDTO);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null && githubUser.getId()!=null){
            User user = new User();
            String token=UUID.randomUUID().toString();
            response.addCookie(new Cookie("token",token));
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtModified());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            return "redirect:/";
            //登陆成功，写入cookie和session
        }else{
            return "redirect:/";
            //登录失败重新登陆
        }
    }
}
