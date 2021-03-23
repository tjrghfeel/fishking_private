package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Event;
import com.tobe.fishking.v2.entity.common.LoveTo;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.EventRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.LoveToRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    Environment env;
    @Autowired
    LoveToRepository loveToRepository;

    /*이벤트 목록 출력*/
    @Transactional
    public Page<EventDtoForPage> getEventList(int page, EventSearchCondition dto) {
        String startDate = null;
        if(dto.getStartDate()!=null){startDate=dto.getStartDate().toString();}
        String endDate = null;
        if(dto.getEndDate()!=null){endDate=dto.getEndDate().toString();}
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Pageable pageable = PageRequest.of(page,10);
        return eventRepository.findEventList(today, dto.getIsLast(), dto.getTitle(), startDate, endDate, dto.getShipName(), pageable);
    }

    /*이벤트 상세보기*/
    @Transactional
    public EventDto getEventDetail(Long eventId, String token) throws ResourceNotFoundException {
        EventDto result = null;

        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+eventId));
        Member member = null;
        if(token != null ){
            member = memberRepository.findBySessionToken(token)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        }


        /*이미지 url list 설정*/
        ArrayList<String> imageUrlList = new ArrayList<String>();
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublishAndIsDelete(eventId, FilePublish.event,false);
        for(int i=0; i<fileList.size(); i++){
            FileEntity file = fileList.get(i);
            imageUrlList.add(env.getProperty("file.downloadUrl")+"/"+file.getFileUrl()+"/"+file.getStoredFile());
        }
        /*좋아요 여부 설정*/
        Boolean isLikeTo = null;
        LoveTo loveTo = loveToRepository.findByLinkIdAndTakeTypeAndCreatedBy(eventId,TakeType.event, member);
        if(loveTo!=null){ isLikeTo = true;}
        else { isLikeTo = false; }

        result = EventDto.builder()
                .eventId(eventId)
                .shipId(event.getShip().getId())
                .eventTitle(event.getTitle())
                .createdDate(event.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .imageUrlList(imageUrlList)
                .content(event.getContents())
                .likeCount(event.getStatus().getLikeCount())
                .commentCount(event.getStatus().getCommentCount())
                .isLikeTo(isLikeTo)
                .build();
        return result;
    }

    /*이벤트 생성*/
    @Transactional
    public Long makeEvent(MakeEventDto dto, String token) throws ResourceNotFoundException {
        Ship ship = null;
        if(dto.getShipId()!=null) {
            ship = shipRepository.findById(dto.getShipId())
                    .orElseThrow(() -> new ResourceNotFoundException("ship not found for this id :: " + dto.getShipId()));
        }
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));

        if(member.getRoles() != Role.admin && member.getRoles() != Role.shipowner){
            throw new RuntimeException("이벤트 작성 권한이 없습니다.");
        }

        Event event = new Event(dto.getTitle(),
                dto.getContent(),
                ship,
                dto.getStartDate().toString(),
                dto.getEndDate().toString(),
                new ShareStatus(0,0,0,0),
                false,
                member,
                member,
                true);

        event = eventRepository.save(event);

        /*파일 저장*/
        Long[] fileList = dto.getFileList();
        if(fileList.length>0){
            List<FileEntity> fileEntityList = fileRepository.findAllById(fileList);
            for(int i=0; i<fileEntityList.size(); i++){
                fileEntityList.get(i).saveTemporaryFile(event.getId());
            }
        }

        return event.getId();
    }

    /*이벤트 수정*/
//    @Transactional
//    public Boolean modifyEvent(ModifyEventDto dto, String token){
//        Member membe = memberRepository
//    }
}
