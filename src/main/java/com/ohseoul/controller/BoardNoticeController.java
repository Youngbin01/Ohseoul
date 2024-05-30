package com.ohseoul.controller;

import com.ohseoul.dto.BoardNoticeFormDTO;
import com.ohseoul.dto.BoardNoticeImgDTO;
import com.ohseoul.dto.NoticeSearchDTO;
import com.ohseoul.dto.ReplyDTO;
import com.ohseoul.entity.BoardNotice;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.BoardNoticeReplyRepository;
import com.ohseoul.repository.BoardNoticeRepository;
import com.ohseoul.repository.MemberRepository;
import com.ohseoul.service.BoardNoticeReplyService;
import com.ohseoul.service.BoardNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class BoardNoticeController {
    private final BoardNoticeService boardNoticeService;
    private final MemberRepository memberRepository;
    private final BoardNoticeReplyService boardNoticeReplyService;
    private final BoardNoticeReplyRepository boardNoticeReplyRepository;
    private final BoardNoticeRepository boardNoticeRepository;


    @GetMapping({"/notice/list","/notice/list/{page}"})
    public String getNoticeList(Model model, NoticeSearchDTO noticeSearchDTO, @PathVariable("page") Optional<Integer> page){

        System.out.println("컨트롤로"+noticeSearchDTO);
        Pageable pageable = PageRequest.of(page.isPresent()? page.get(): 0,10);
        Page<BoardNotice> noticeList =  boardNoticeService.getNoticeListPage(noticeSearchDTO,pageable);
        System.out.println("컨트롤로 noticeList"+noticeList);
        model.addAttribute("noticeList",noticeList);
        model.addAttribute("noticeSearchDTO",noticeSearchDTO);
        model.addAttribute("maxPage",noticeList.getTotalPages());
        return "/notice/noticeList";
    }

    @GetMapping("/notice/view/{id}")
    public String getOneMember(Model model, @PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){
        BoardNoticeFormDTO boardNoticeFormDTO = boardNoticeService.getNoticeMemberId(id);
        System.out.println("컨트롤러 ++getOneMember"+id);
        System.out.println("컨트롤러 ++boardCommunityFormDTO"+boardNoticeFormDTO);
        ReplyDTO reply = new ReplyDTO();
        List<ReplyDTO> replyDTOList = boardNoticeReplyService.getNoticeReplyList(id);
        System.out.println(replyDTOList);
        //---------------게시물을 처음 들어왔을때 쿠키를 설정해 조회수 증가시키는 코드------------

        //웹 요청에서 클라이언트가 전송한 모든 쿠키를 가져오는 작업
        Cookie[] cookies = request.getCookies();

        boolean visited = false;  //   visited 라는 변수를 사용해서 해당 게시물을 방문 했는지 여부 추적
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                //  --> cookie.getName().equals("visited_" + id 이건 게시물마다 조회수가 1씩 증가해야하므로 id를 붙여줘서
                //  다른 게시물들도 방문하면 조회수가 1증가하도록 만듦
                if (cookie.getName().equals("visited_" + id)) {
                    visited = true;
                    break;
                }
            }
        }
        // 방문하지 않은 게시물일 경우에만 조회수 증가
        if (!visited) {
            boardNoticeService.increaseViewCount(id);

            // 방문한 게시물임을 클라이언트에게 알리기 위해 쿠키 설정
            Cookie cookie = new Cookie("visited_" + id, "true");
            cookie.setMaxAge(24 * 60 * 60); // 하루동안 유지되는 쿠키 설정 해주기!!
            cookie.setPath("/");  // 쿠키의 경로를 설정하는 자바스크립코드임----> 해당하는 루트 경로를 설정해줌
            //  "/" 루트 경로를 설정 해줘서 쿠키는 우리 웹사이트의 모든 페이지에서 유효함을 나타냄.
            //  그래서 이 쿠키는 모든 페이지에서 사용 가능함.
            response.addCookie(cookie);   // 여기서 클라이언트에게 설정한 쿠키를 추가시킴!
        }

        //        세션 방식
//        HttpSession session = request.getSession(); // 세션 가져오기
//
//// 방문한 게시물의 ID를 세션에서 가져오기
//        HashSet<String> visitedPosts = (HashSet<String>) session.getAttribute("visitedPosts");
//
//        if (visitedPosts == null) {
//            visitedPosts = new HashSet<>();
//        }
//
//// 방문한 게시물인지 확인
//        if (!visitedPosts.contains(id)) {
//            boardNoticeService.increaseViewCount(id);
//            visitedPosts.add(String.valueOf(id));
//            session.setAttribute("visitedPosts", visitedPosts); // 방문한 게시물의 ID를 세션에 저장
//        }


        model.addAttribute("id",id);
        model.addAttribute("boardNotice",boardNoticeFormDTO);
        model.addAttribute("replyDTOList",replyDTOList);
        model.addAttribute("reply",reply);
        return "/notice/noticeRead";
    }

    @GetMapping("/admin/notice/new")
    public String createNoticeCommunityWrite(Model model){
        model.addAttribute("boardNoticeFormDTO",new BoardNoticeFormDTO());
        return "/notice/noticeWriteForm";
    }

    @PostMapping("/admin/notice/new")
    public String boardNoticeNew(@Valid BoardNoticeFormDTO boardNoticeFormDTO, BindingResult bindingResult, Model model,
                                    @RequestParam("noticeImgFile")List<MultipartFile> noticeImgFileList, Principal principal){

        String email = principal.getName();

        Member member = memberRepository.findByEmail(email);
        member.getMemberNo();

        boardNoticeFormDTO.setMember(member);

        if(bindingResult.hasErrors()){
            return "/notice/noticeWriteForm";
        }
        try{
            boardNoticeService.saveNotice(boardNoticeFormDTO, noticeImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","게시글 등록중 에러가 발생했습니다.");
        }
        model.addAttribute("message","글작성이 완료되었습니다");
        return "redirect:/community/notice/list/";
    }

    @GetMapping("/admin/notice/view/edit/{id}")
    public String communityUpdates(Model model, @PathVariable("id") Long id){
        BoardNoticeFormDTO boardNoticeFormDTO = boardNoticeService.getNoticeMemberId(id);
        System.out.println("boardNoticeFormDTO==========================="+boardNoticeFormDTO);
        System.out.println("memberNickName================="+boardNoticeFormDTO.getMember().getMemberNickname());
        model.addAttribute("id",id);
        model.addAttribute("boardNoticeFormDTO",boardNoticeFormDTO);
        return "/notice/noticeEditForm";
    }


    @PostMapping("/admin/notice/view/edit/{id}")
    public String communityUpdate(@Valid BoardNoticeFormDTO boardNoticeFormDTO,
                                  BindingResult bindingResult, @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList,
                                  Model model , @PathVariable("id") Long id,Principal principal){

        boardNoticeFormDTO.setNoticeNo(id);
        boardNoticeFormDTO.setMember(memberRepository.findByEmail(principal.getName()));
        String email = principal.getName();

        System.out.println("email========================"+email);
        System.out.println("boardNoticeFormDTO==================="+boardNoticeFormDTO);
        if (bindingResult.hasErrors()) {

            return "/notice/noticeEditForm";
        }
        if (boardNoticeFormDTO.getNoticeNo() == null){

            model.addAttribute("errorMessage","존재하지 않는 게시글 입니다.");
            return "/notice/noticeEditForm";
        }
        try{

            boardNoticeService.updateNotice(boardNoticeFormDTO,noticeImgFileList,email);
        }catch (Exception e){
            model.addAttribute("errorMessage","게시글 수정중 에러가 발생하였습니다.");
            return "/notice/noticeEditForm";
        }

        return "redirect:/community/notice/list";
    }

    @DeleteMapping("/admin/notice/delete/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable Long id, Principal principal) throws Exception {
        String email = principal.getName();
        // 게시물 삭제 서비스 메서드 호출
        boardNoticeService.deleteNotice(id,email);
        return ResponseEntity.ok("게시물이 삭제되었습니다.");
    }

}
