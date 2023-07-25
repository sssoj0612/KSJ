package kopo.poly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MapController {
    @GetMapping("/map/chungcheong")
    public String chungcheong() throws Exception {
        log.info(this.getClass().getName() + ".chungcheong 페이지 보여주는 함수 실행");
        return "/map/chungcheong";
    }

    @GetMapping("/map/daegu")
    public String daegu() throws Exception {
        log.info(this.getClass().getName() + ".daegu 페이지 보여주는 함수 실행");
        return "/map/daegu";
    }

    @GetMapping("/map/gimpo")
    public String gimpo() throws Exception {
        log.info(this.getClass().getName() + ".gimpo 페이지 보여주는 함수 실행");
        return "/map/gimpo";
    }

    @GetMapping("/map/gyeonggi")
    public String gyeonggi() throws Exception {
        log.info(this.getClass().getName() + ".gyeonggi 페이지 보여주는 함수 실행");
        return "/map/gyeonggi";
    }

    @GetMapping("/map/gyeongsang")
    public String gyeongsang() throws Exception {
        log.info(this.getClass().getName() + ".gyeongsang 페이지 보여주는 함수 실행");
        return "/map/gyeongsang";
    }

    @GetMapping("/map/incheon")
    public String incheon() throws Exception {
        log.info(this.getClass().getName() + ".incheon 페이지 보여주는 함수 실행");
        return "/map/incheon";
    }

    @GetMapping("/map/jeolla")
    public String jeolla() throws Exception {
        log.info(this.getClass().getName() + ".jeolla 페이지 보여주는 함수 실행");
        return "/map/jeolla";
    }

    @GetMapping("/map/puul")
    public String puul() throws Exception {
        log.info(this.getClass().getName() + ".puul 페이지 보여주는 함수 실행");
        return "/map/puul";
    }

}
