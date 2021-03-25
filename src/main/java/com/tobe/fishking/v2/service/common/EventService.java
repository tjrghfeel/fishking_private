package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Comment;
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
import com.tobe.fishking.v2.service.auth.MemberService;
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
    @Autowired
    MemberService memberService;

    /*이벤트 목록 출력*/
    @Transactional
    public Page<EventDtoForPage> getEventList(int page, String token, EventSearchCondition dto) {
        //관리자가 api호출시, 비활성화된 이벤트도 반환하도록.
        Boolean isActive = true;
        if(token!=null){
            Member member = memberService.getMemberBySessionToken(token);
            if(member.getRoles()==Role.admin){isActive = null;}
        }

        String startDate = null;
        if(dto.getStartDate()!=null){startDate=dto.getStartDate().toString();}
        String endDate = null;
        if(dto.getEndDate()!=null){endDate=dto.getEndDate().toString();}
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Pageable pageable = PageRequest.of(page,10);
        return eventRepository.findEventList(today, dto.getIsLast(), dto.getTitle(), startDate, endDate, dto.getShipName(), isActive, pageable);
    }

    /*이벤트 상세보기*/
    @Transactional
    public EventDto getEventDetail(Long eventId, String token) throws ResourceNotFoundException {
        EventDto result = null;

        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+eventId));
        if(event.getIsDeleted()==true){throw new RuntimeException("삭제된 이벤트입니다.");}

        Member member = null;
        if(token != null ){
            member = memberRepository.findBySessionToken(token)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        }

        /*이미지 url list 설정*/
        ArrayList<String> imageUrlList = new ArrayList<String>();
        ArrayList<Long> fileIdList = new ArrayList<>();
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublishAndIsDelete(eventId, FilePublish.event,false);
        for(int i=0; i<fileList.size(); i++){
            FileEntity file = fileList.get(i);
            imageUrlList.add(env.getProperty("file.downloadUrl")+"/"+file.getFileUrl()+"/"+file.getStoredFile());
            fileIdList.add(file.getId());
        }
        /*좋아요 여부 설정*/
        Boolean isLikeTo = null;
        LoveTo loveTo = loveToRepository.findByLinkIdAndTakeTypeAndCreatedBy(eventId,TakeType.event, member);
        if(loveTo!=null){ isLikeTo = true;}
        else { isLikeTo = false; }
        /*ship설정*/
        Long shipId = null;
        if(event.getShip()!=null){shipId = event.getShip().getId();}

        result = EventDto.builder()
                .eventId(eventId)
                .shipId(shipId)
                .eventTitle(event.getTitle())
                .createdDate(event.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .imageUrlList(imageUrlList)
                .content(event.getContents())
                .likeCount(event.getStatus().getLikeCount())
                .commentCount(event.getStatus().getCommentCount())
                .isLikeTo(isLikeTo)
                .isActive(event.getIsActive())
                .startDate(event.getStartDay())
                .endDate(event.getEndDay())
                .fileIdList(fileIdList)
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

        Integer orderLevel = null;
        if(member.getRoles() == Role.admin){orderLevel = 10;}

        Event event = new Event(dto.getTitle(),
                dto.getContent(),
                ship,
                dto.getStartDate().toString(),
                dto.getEndDate().toString(),
                new ShareStatus(0,0,0,0),
                false,
                member,
                member,
                orderLevel,
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
    @Transactional
    public Boolean modifyEvent(ModifyEventDto dto, String token) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+dto.getEventId()));
        /*권한확인*/
        if(member.getRoles() != Role.admin && event.getCreatedBy()!=member){
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        /*modify()에 넘겨줄 인자 설정*/
        String startDate = (dto.getStartDate()!=null)? dto.getStartDate().toString() : null;
        String endDate = (dto.getEndDate()!=null)? dto.getEndDate().toString() : null;
//        Ship ship = null; //이벤트가 속한 선박을 수정한다는 시나리오가 부적절하다고 생각되어서.
//                              선박 생성, 수정에서 이벤트를 관리하는데 이 이벤트를 다른 선박으로 보낸다?
//        if(dto.getShipId()!=null){
//            ship = shipRepository.findById(dto.getShipId())
//                    .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+dto.getShipId()));
//        }

        /*이벤트 수정.*/
        event.modify(
                dto.getTitle(),dto.getContent(), /*ship,*/ startDate, endDate, dto.getOrderLevel(), dto.getIsActive()
        );

        //파일수정
        List<FileEntity> preFileEntityList = fileRepository.findByPidAndFilePublishAndIsDelete(event.getId(),FilePublish.event,false);
        for(int i=0; i<preFileEntityList.size(); i++){preFileEntityList.get(i).setIsDelete(true);}
        List<FileEntity> fileEntityList = fileRepository.findAllById(dto.getFileList());
        for(int i=0; i<fileEntityList.size(); i++){ fileEntityList.get(i).saveTemporaryFile(event.getId());}

        return true;
    }

    //숨김처리
    @Transactional
    public Boolean hideEvent(Long id, String active, String token) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        Boolean isActive = null;
        if(active.equals("true")){isActive=true;}
        else if(active.equals("false")){isActive = false; }

        if(member.getRoles()==Role.admin){
            Event event = eventRepository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("comment not found for this id :: "+id));
            event.setActive(isActive);
            return true;
        }
        else return false;
    }

    //이벤트 삭제
    @Transactional
    public Boolean deleteEvent(Long id, String token) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        Event event = eventRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+id));

        if(event.getCreatedBy() != member){return false;}
        else{
            event.delete();
            return true;
        }
    }
}
