package erenyegar.community.controller;

import erenyegar.community.dto.QuestionDTO;
import erenyegar.community.mapper.QuestionMapper;
import erenyegar.community.mapper.UserMapper;
import erenyegar.community.model.Question;
import erenyegar.community.model.User;
import erenyegar.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller//识别扫描当前的类并让其作为Spring的Bean去管理，让其接收前端的请求
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        Cookie[] cookies = request.getCookies();
        if(cookies !=null&&cookies.length!=0)
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token =cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if(user !=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }

        List<QuestionDTO> questionList=questionService.list();
        model.addAttribute("questions",questionList);
        return "index";
    }
}
