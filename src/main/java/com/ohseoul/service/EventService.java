package com.ohseoul.service;

import com.ohseoul.dto.EventDTO;
import com.ohseoul.dto.EventItemDTO;
import com.ohseoul.dto.SearchCriteriaDTO;
import com.ohseoul.entity.EventInfo;
import com.ohseoul.repository.EventInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventInfoRepository eventInfoRepository;

    @Autowired
    public EventService(EventInfoRepository eventInfoRepository) {
        this.eventInfoRepository = eventInfoRepository;
    }

    public Page<EventDTO> getEvents(Pageable pageable) {
        Page<EventInfo> eventInfos = eventInfoRepository.findAll(pageable);
        return eventInfos.map(eventInfo -> {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setEventId(eventInfo.getEventId());
            eventDTO.setTitle(eventInfo.getTitle());
            eventDTO.setDate(eventInfo.getDate());
            eventDTO.setMain_img(eventInfo.getMain_img());
            return eventDTO;
        });
    }




    public Page<EventInfo> searchEvents(SearchCriteriaDTO criteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventInfoRepository.findFilteredEventInfo(
                criteria.getIs_free(), criteria.getMcodename(), criteria.getCodename(), criteria.getSearchKeyword(), pageable);
    }

    public EventItemDTO getEventByEventId(Long eventId) {
        EventInfo eventInfo = eventInfoRepository.findByEventId(eventId);
        if (eventInfo != null) {
            EventItemDTO eventItemDTO = new EventItemDTO();
            eventItemDTO.setEventId(eventInfo.getEventId());
            eventItemDTO.setTitle(eventInfo.getTitle());
            eventItemDTO.setDate(eventInfo.getDate());
            eventItemDTO.setMain_img(eventInfo.getMain_img());
            eventItemDTO.setLat(eventInfo.getLat());
            eventItemDTO.setLot(eventInfo.getLot());
            eventItemDTO.setUse_trgt(eventInfo.getUse_trgt());
            eventItemDTO.setUse_fee(eventInfo.getUse_fee());
            eventItemDTO.setMcodename(eventInfo.getMcodename());
            eventItemDTO.setCodename(eventInfo.getCodename());
            eventItemDTO.setHmpg_addr(eventInfo.getHmpg_addr());
            eventItemDTO.setPlace(eventInfo.getPlace());
            return eventItemDTO;
        } else {
            // 해당 타이틀의 공연 정보가 없을 경우 처리
            return null;
        }
    }

    public boolean isDataAlreadyLoaded() {
        long count=eventInfoRepository.count();
        return count>0;
    }
}
