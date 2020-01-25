package erenyegar.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller//识别扫描当前的类并让其作为Spring的Bean去管理，让其接收前端的请求
public class IndexController {

    @GetMapping("/")
    public String index(){ return "index"; }
}
