package com.ohseoul.controller;

import com.ohseoul.constant.Role;
import com.ohseoul.dto.BoardCommunityDTO;
import com.ohseoul.dto.BoardCommunityFormDTO;
import com.ohseoul.dto.BoardSearchDTO;
import com.ohseoul.dto.ReplyDTO;
import com.ohseoul.entity.BoardCommunity;
import com.ohseoul.entity.Member;
import com.ohseoul.repository.BoardCommunityReplyRepository;
import com.ohseoul.repository.BoardCommunityRepository;
import com.ohseoul.repository.MemberRepository;
import com.ohseoul.service.BoardCommunityReplyService;
import com.ohseoul.service.BoardCommunityService;
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
public class BoardCommunityController {

    private final BoardCommunityService boardCommunityService;
    private final MemberRepository memberRepository;
    private final BoardCommunityReplyService  boardCommunityReplyService;
    private final BoardCommunityReplyRepository boardCommunityReplyRepository;
    private final BoardCommunityRepository boardCommunityRepository;


    @GetMapping({"/board/list","/board/list/{page}"})
    public String getList(Model model, BoardSearchDTO boardSearchDTO, @PathVariable("page")Optional<Integer> page){

        System.out.println("컨트롤로"+boardSearchDTO);
        Pageable pageable = PageRequest.of(page.isPresent()? page.get(): 0,10);
        Page<BoardCommunity> list =  boardCommunityService.getBoardListPage(boardSearchDTO,pageable);
        System.out.println("컨트롤로 list"+list);
        model.addAttribute("list",list);
        model.addAttribute("boardSearchDTO",boardSearchDTO);
        model.addAttribute("maxPage",list.getTotalPages());
        return "/community/board";
    }

    @GetMapping("/board/view/{id}")
    public String getOneMember(Model model, @PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){
        BoardCommunityFormDTO boardCommunityFormDTO = boardCommunityService.getMemberId(id);
        System.out.println("컨트롤러 ++getOneMember"+id);
        System.out.println("컨트롤러 ++boardCommunityFormDTO"+boardCommunityFormDTO);
        ReplyDTO reply = new ReplyDTO();
        List<ReplyDTO> replyDTOList = boardCommunityReplyService.getReplyList(id);
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
            boardCommunityService.increaseViewCount(id);

            // 방문한 게시물임을 클라이언트에게 알리기 위해 쿠키 설정
            Cookie cookie = new Cookie("visited_" + id, "true");
            cookie.setMaxAge(24 * 60 * 60); // 하루동안 유지되는 쿠키 설정 해주기!!
            cookie.setPath("/");  // 쿠키의 경로를 설정하는 자바스크립코드임----> 해당하는 루트 경로를 설정해줌
                                  //  "/" 루트 경로를 설정 해줘서 쿠키는 우리 웹사이트의 모든 페이지에서 유효함을 나타냄.
                                  //  그래서 이 쿠키는 모든 페이지에서 사용 가능함.
            response.addCookie(cookie);   // 여기서 클라이언트에게 설정한 쿠키를 추가시킴!
        }

/*//        세션 방식
        HttpSession session = request.getSession(); // 세션 가져오기

// 방문한 게시물의 ID를 세션에서 가져오기
        HashSet<String> visitedPosts = (HashSet<String>) session.getAttribute("visitedPosts");

        if (visitedPosts == null) {
            visitedPosts = new HashSet<>();
        }

// 방문한 게시물인지 확인
        if (!visitedPosts.contains(id)) {
            boardCommunityService.increaseViewCount(id);
            visitedPosts.add(String.valueOf(id));
            session.setAttribute("visitedPosts", visitedPosts); // 방문한 게시물의 ID를 세션에 저장
        }*/
        model.addAttribute("id",id);
        model.addAttribute("boardCommunity",boardCommunityFormDTO);
        model.addAttribute("replyDTOList",replyDTOList);
        model.addAttribute("reply",reply);
        return "/community/read";
    }

    @GetMapping("/user/board/new")
    public String createBoardCommunityWrite(Model model){
        model.addAttribute("boardCommunityFormDTO",new BoardCommunityDTO());
        return "/community/writeForm";
    }

    @PostMapping("/user/board/new")
    public String boardCommunityNew(@Valid BoardCommunityFormDTO boardCommunityFormDTO, BindingResult bindingResult, Model model,
                                    @RequestParam("boardImgFile")List<MultipartFile> boardImgFileList, Principal principal){

        String email = principal.getName();

        Member member = memberRepository.findByEmail(email);
        member.getMemberNo();

        boardCommunityFormDTO.setMember(member);

        if(bindingResult.hasErrors()){
            return "/community/writeForm";
        }
        try{
            boardCommunityService.saveBoard(boardCommunityFormDTO, boardImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","게시글 등록중 에러가 발생했습니다.");
        }
        model.addAttribute("message","글작성이 완료되었습니다");
        return "redirect:/community/board/list/";
    }

    @GetMapping("/user/board/view/edit/{id}")
    public String communityUpdates(Model model, @PathVariable("id") Long id, Principal principal){
        BoardCommunityFormDTO boardCommunityFormDTO = boardCommunityService.getMemberId(id);
        String createby = boardCommunityFormDTO.getMember().getEmail();
        String email = principal.getName();
        Member member = memberRepository.findByEmail(principal.getName());
        Role role = member.getRole();
        System.out.println("createby++++++:"+createby);
        System.out.println("email++++++:"+email);

        if( !email.equals(createby) && role == Role.USER){
            model.addAttribute("message","작성하지 않은 게시글 입니다.");
            return "redirect:/community/board/list/";
        }
        model.addAttribute("id",id);
        model.addAttribute("boardCommunity",boardCommunityFormDTO);
        return "/community/editForm";


//        if(!boardCommunityFormDTO.getMember().getEmail().equals(principal.getName()) ||
//                role == Role.USER){
//            model.addAttribute("message","작성하지 않은 게시글 입니다.");
//            return "redirect:/community/board/list/";
//        }
//        model.addAttribute("id",id);
//        model.addAttribute("boardCommunity",boardCommunityFormDTO);
//        return "/community/editForm";
    }


    @PostMapping("/user/board/view/edit/{id}")
    public String communityUpdate(@Valid BoardCommunityFormDTO boardCommunityFormDTO,
                                  BindingResult bindingResult, @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList,
                                  Model model , @PathVariable("id") Long id,Principal principal){
        boardCommunityFormDTO.setRegTime(boardCommunityFormDTO.getRegTime());
        boardCommunityFormDTO.setCommunityNo(id);
        String email = principal.getName();
        if (bindingResult.hasErrors()) {

            return "/community/editForm";
        }
        if (boardCommunityFormDTO.getCommunityNo() == null){

            model.addAttribute("errorMessage","존재하지 않는 게시글 입니다.");
            return "/community/editForm";
        }

        try{

            boardCommunityService.updateCommunity(boardCommunityFormDTO,boardImgFileList,email);
        }catch (Exception e){
            model.addAttribute("errorMessage","게시글 수정중 에러가 발생하였습니다.");
            return "/community/editForm";
        }

        return "redirect:/community/board/list/";
    }

    @DeleteMapping("/user/board/delete/{id}")
    public ResponseEntity<String> deleteCommunity(@PathVariable Long id, Principal principal) throws Exception {
        String email = principal.getName();
        System.out.println("딜리트 id + 이메일++ "+id+"asdsadasd+++++"+email);

                // 게시물 삭제 서비스 메서드 호출
        boardCommunityService.deleteCommunity(id,email);
        return ResponseEntity.ok("게시물이 삭제되었습니다.");
    }



}
