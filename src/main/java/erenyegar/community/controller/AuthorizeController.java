package erenyegar.community.controller;

import erenyegar.community.dto.AccessTokenDTO;
import erenyegar.community.dto.GithubUser;
import erenyegar.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("b5b530c9c9ee14c475bb");
        accessTokenDTO.setClient_secret("72e44e281c5a3d321f43e4c7a0e80f1487002b8b");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setState(state);
        githubProvider.getAccessToken(accessTokenDTO);
        //String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //GithubUser user = githubProvider.getUser(accessToken);
        //System.out.println(user.getName());
        return "index";
    }
}
