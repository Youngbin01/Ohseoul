package com.ohseoul.controller;

import com.ohseoul.dto.EventDTO;
import com.ohseoul.dto.EventItemDTO;
import com.ohseoul.dto.ReviewDTO;
import com.ohseoul.dto.SearchCriteriaDTO;
import com.ohseoul.entity.EventInfo;
import com.ohseoul.service.BoardReviewService;
import com.ohseoul.service.EventService;
import com.ohseoul.service.XmlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final EventService eventService;
    private final XmlParser xmlParser;
    private final BoardReviewService reviewService;


    @GetMapping("/schedule")
    public String schedule(Model model, @RequestParam(defaultValue = "0") int page) {
        // 첫 번째 페이지 요청일 때만 XML 파싱 및 데이터베이스 저장 메서드 실행
        if (page == 0 && !eventService.isDataAlreadyLoaded()) {
            xmlParser.parseXmlAndSaveToDatabase();
        }

        Pageable pageable = PageRequest.of(page, 12);
        Page<EventDTO> events = eventService.getEvents(pageable);
        // 모델에 이벤트 정보 추가
        model.addAttribute("events", events);

        return "schedule/schedule_detail";
    }

    @GetMapping("/test")
    public String home() {
        return "schedule/test";
    }

    @GetMapping("/schedule/search")
    public String searchEvents(SearchCriteriaDTO criteria, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Page<EventInfo> events = eventService.searchEvents(criteria, page, size);
        model.addAttribute("events", events);
        model.addAttribute("criteria", criteria);
        return "schedule/schedule_detail";
    }



    @GetMapping("/schedule/{eventId}")
    public String eventDetail(@PathVariable Long eventId, Model model) {
        System.out.println("컨트롤러 eventId = " + eventId);
        EventItemDTO event = eventService.getEventByEventId(eventId);
        System.out.println("컨트롤러 event = " + event);
        ReviewDTO review =new ReviewDTO();
        List<ReviewDTO> reviewDTO = reviewService.getEventReview(event.getEventId());
        System.out.println("reviewDTO+++++++컨트롤러"+reviewDTO);
        model.addAttribute("review", reviewDTO);
        model.addAttribute("event", event);
        model.addAttribute("one", review);
        return "schedule/schedule_item_detail";
    }




}
