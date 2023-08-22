package kopo.poly.controller;


import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.INoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NoticeController {
    // url 로부터 넘어온 데이터를 서비스 함수로 보내야하기 때문에 서비스 함수 호출해야함
    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller 에서 사용할 수 있게 주입함.
    private final INoticeService noticeService;


    // 1. 게시판 리스트 보여주기
    // GetMapping("value=notice/noticeList") => GET 방식을 통해 접속되는 URL 이 notice/noticeList 경우 아래 함수
    @GetMapping(value = "/notice/noticeList")
    public String noticeList(ModelMap model) throws Exception {

        // 로그 찍기
        log.info(this.getClass().getName() + ".noticeList 페이지 보여주는 함수 실행");

        // 공지사항 리스트 조회하기
        List<NoticeDTO> rList = noticeService.getNoticeList();
        if (rList == null) rList = new ArrayList<>();

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        // 로그 찍기
        log.info(this.getClass().getName() + ".noticeList 페이지 보여주는 함수 끝");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeList";
    }


    // 2. 게시판 작성 페이지로 접근하기 위해 만든 함수
    // GetMapping("value=notice/noticeReg") => GET 방식을 통해 접속되는 URL 이 notice/noticeReg 경우 아래 함수
    @GetMapping(value = "/notice/noticeReg")
    public String NoticeReg() {
        log.info(this.getClass().getName() + ".noticeReg 페이지 보여주는 함수 실행");
        log.info(this.getClass().getName() + ".noticeReg 페이지 보여주는 함수 끝");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeReg";
    }


    // 3. 게시판 글 등록
    // 게시글 등록은 Ajax 로 호출되기 때문에 결과는 JSON 구조로 전달해야만 함.
    // JSON 구조로 결과 메시지를 전송하기 위해 @ResponseBody 어노테이션 추가함.

    @PostMapping(value = "/notice/noticeInsert")
    // url 로부터 들어온 값을 꺼냄, 값을 결과로 보여주는 객체,
    public String noticeInsert(HttpServletRequest request, ModelMap model, HttpSession session) {
        log.info(this.getClass().getName() + "게시판 글 등록하는.noticeInsert 함수 실행");
        String msg = ""; // 메시지 내용
        String url = "/notice/noticeReg"; // 이동할 경로 내용

        try { // 로그인 된 사용자 아이디를 가져오기
            // 로그인을 아직 구현하지 않았기에 공지사항 리스트에서 로그인 한 것 처럼 Session 값을 저장함.
            String user_id = kopo.poly.util.CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String title = kopo.poly.util.CmmUtil.nvl(request.getParameter("title")); // 제목
            String notice_yn = kopo.poly.util.CmmUtil.nvl(request.getParameter("notice_yn")); // 공지글 여부
            String contents = kopo.poly.util.CmmUtil.nvl(request.getParameter("contents")); // 내용


            log.info("session user_id : " + user_id);
            log.info("title : " + title);
            log.info("notice_yn : " + notice_yn);
            log.info("contents : " + contents);

            // 데이터 저장하기 위해 DTO 에 저장하기
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUser_id(user_id);
            pDTO.setTitle(title);
            pDTO.setNotice_yn(notice_yn);
            pDTO.setContents(contents);

            // 게시글 등록하기 위한 비즈니스 로직 호출
            noticeService.insertNoticeInfo(pDTO);

            // 저장이 완료되면 사용자에게 보여줄 페이지
            msg = "등록되었습니다.";
            url = "/notice/noticeList";
        } catch (Exception e) { // 저장이 실패되면 사용자에게 보여줄 메시지

            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally { // 결과 메시지 전달하기
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);
            log.info(this.getClass().getName() + "게시판 글 등록하는.noticeInsert 함수 끝");
        }

        return "/redirect";
    }


    // 4. 게시판 상세보기
    @GetMapping(value = "/notice/noticeInfo")
    public String noticeInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + "상세보기 함수 .noticeInfo 실행");

        String nSeq = kopo.poly.util.CmmUtil.nvl(request.getParameter("nSeq")); // 공지글번호(PK)

        log.info("nSeq : " + nSeq);

        // 값 전달은 반드시 DTO 객체를 이용해서 처리함. 전달 받은 값을 DTO 객체에 넣는다.
        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNotice_seq(nSeq);

        // 공지사항 상세정보 가져오기
        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
                .orElseGet(NoticeDTO::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + "상세보기 함수 .noticeInfo 끝");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeInfo";
    }


    // 5. 게시판 수정을 위한 페이지
    @GetMapping(value = "/notice/noticeEditInfo")
    public String noticeEditInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + "수정페이지 접근 .noticeEditInfo 함수 실행");

        String nSeq = kopo.poly.util.CmmUtil.nvl(request.getParameter("nSeq"));

        log.info("nSeq : " + nSeq);

        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNotice_seq(nSeq);

        NoticeDTO rDTO = noticeService.getNoticeInfo(pDTO, false);
        if (rDTO == null) rDTO = new NoticeDTO();

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + "수정페이지 접근 .noticeEditInfo 함수 끝");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/notice/noticeEditInfo";
    }


    // 6. 게시판 글 수정 실행 로직
    @PostMapping(value = "/notice/noticeUpdate")
    public String noticeUpdate(HttpSession session, ModelMap model, HttpServletRequest request) {
        log.info(this.getClass().getName() + "게시글 수정 함수 .noticeUpdate 실행");

        String msg = ""; // 메시지 내용
        String url = ""; // 이동할 경로 내용

        try {
            String user_id = kopo.poly.util.CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 아이디
            String nSeq = kopo.poly.util.CmmUtil.nvl(request.getParameter("nSeq")); // 글번호
            String title = kopo.poly.util.CmmUtil.nvl(request.getParameter("title")); // 제목
            String notice_yn = kopo.poly.util.CmmUtil.nvl(request.getParameter("notice_yn")); // 공지글 여부
            String contents = kopo.poly.util.CmmUtil.nvl(request.getParameter("contents")); // 내용

            log.info("user_id : " + user_id);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("notice_yn : " + notice_yn);
            log.info("contents : " + contents);

            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUser_id(user_id);
            pDTO.setNotice_seq(nSeq);
            pDTO.setTitle(title);
            pDTO.setNotice_yn(notice_yn);
            pDTO.setContents(contents);


            // 게시글 수정하기 DB
            noticeService.updateNoticeInfo(pDTO);

            msg = "수정되었습니다.";
            url = "/notice/noticeInfo?nSeq="+nSeq;
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);
            log.info(this.getClass().getName() + "게시글 수정 함수 .noticeUpdate 끝");
        }

        return "/redirect";

    }


    // 7. 게시판 글 삭제 로직
    @GetMapping (value = "/notice/noticeDelete")
    public String noticeDelete(ModelMap model, HttpServletRequest request) {
        log.info(this.getClass().getName() + "게시글 삭제 함수 .noticeDelete 실행");

        String nSeq = kopo.poly.util.CmmUtil.nvl(request.getParameter("nSeq"));
        log.info("nSeq: " + nSeq);

        String msg = ""; // 메시지 내용
        String url = "/notice/noticeInfo"; // 이동할 경로 내용

        try {
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setNotice_seq(nSeq);


            // 게시글 삭제하기 DB
            noticeService.deleteNoticeInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "삭제 실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);
            log.info(this.getClass().getName() + "게시글 삭제 함수 .noticeDelete 끝");
        }

        return "/redirect";
    }


    // 8. 파일첨부하기
    @PostMapping(value = "/notice/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file){
        return "redirect:/success";
    }


}
