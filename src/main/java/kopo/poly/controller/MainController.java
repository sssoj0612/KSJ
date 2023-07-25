package kopo.poly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {
    @GetMapping("/index")
    public String index() throws Exception {
        log.info(this.getClass().getName() + ".index 페이지 보여주는 함수 실행");
        return "/index";
    }


    @GetMapping("/about")
    public String about() throws Exception {
        log.info(this.getClass().getName() + ".about 페이지 보여주는 함수 실행");
        return "/about";
    }


    @GetMapping("/elements")
    public String elements() throws Exception {
        log.info(this.getClass().getName() + ".elements 페이지 보여주는 함수 실행");
        return "/elements";
    }


    @GetMapping("/services")
    public String services() throws Exception {
        log.info(this.getClass().getName() + ".services 페이지 보여주는 함수 실행");
        return "/services";
    }



    @GetMapping("/contact")
    public String contact() throws Exception {
        log.info(this.getClass().getName() + ".contact 페이지 보여주는 함수 실행");
        return "/contact";
    }


}
