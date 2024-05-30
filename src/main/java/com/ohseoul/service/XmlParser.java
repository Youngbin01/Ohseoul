package com.ohseoul.service;

import com.ohseoul.entity.CulturalEventInfo;
import com.ohseoul.entity.EventInfo;
import com.ohseoul.repository.EventInfoRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlParser {

    private final EventInfoRepository eventInfoRepository;

    public XmlParser(EventInfoRepository eventInfoRepository) {
        this.eventInfoRepository = eventInfoRepository;
    }

    @Transactional
    public void parseXmlAndSaveToDatabase() {
        try {
            // XML 데이터를 읽어올 URL을 지정합니다.
            URL url = new URL("http://openapi.seoul.go.kr:8088/5347646b6979756e373862444c7356/xml/culturalEventInfo/1/1000/");

            // JAXBContext를 초기화합니다.
            JAXBContext jaxbContext = JAXBContext.newInstance(CulturalEventInfo.class);

            // Unmarshaller를 생성합니다.
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // URL에서 XML 데이터를 읽어와 CulturalEventInfo 객체로 변환합니다.
            CulturalEventInfo culturalEventInfo = (CulturalEventInfo) jaxbUnmarshaller.unmarshal(url);

            // CulturalEventInfo 객체에서 EventInfo 리스트를 가져옵니다.
            List<EventInfo> eventInfoList = culturalEventInfo.getEventInfoList();

            // 가져온 EventInfo 리스트를 데이터베이스에 저장합니다.
            List<EventInfo> eventInfoEntities = new ArrayList<>();
            for (EventInfo eventInfo : eventInfoList) {
                EventInfo entity = new EventInfo(eventInfo.getCodename(), eventInfo.getGuname(), eventInfo.getTitle(), eventInfo.getDate(), eventInfo.getPlace(), eventInfo.getMain_img(), eventInfo.getIs_free(), eventInfo.getUse_trgt(), eventInfo.getUse_fee(), eventInfo.getLot(),eventInfo.getLat(),eventInfo.getHmpg_addr());
                eventInfoEntities.add(entity);
            }
            eventInfoRepository.saveAll(eventInfoEntities);

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }
}
