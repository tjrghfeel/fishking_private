package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.entity.common.Event;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.*;
import com.tobe.fishking.v2.repository.common.EventRepository;
import com.tobe.fishking.v2.service.common.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Api(tags = {"이벤트"})
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
@RestController
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;

    /*이벤트 목록 보기*/
    @ApiOperation(value = "이벤트 목록",notes = "" +
            "요청 필드 ) \n" +
            "- isLast : Boolean / 선택 / true시, 지난 이벤트 반환\n" +
            "- title : String / 선택 / 이벤트명 검색시 사용\n" +
            "- startDate : String / 선택 / 이벤트 시작일 검색시 사용. yyyy-MM-dd형식\n" +
            "- endDate : STring / 선택 / 이벤트 종료일 검색시 사용. yyyy-MM-dd형식\n" +
            "- shipName : String / 선택 / 이벤트중인 선박명 검색시 사용. \n" +
            "응답 필드 ) \n" +
            "- eventId : Long / 이벤트의 id\n" +
            "- shipId : Long / 해당 이벤트가 속한 선박 id\n" +
            "- imageUrl : String / 이벤트 내용의 이미지 중 하나의 url\n" +
            "- eventTitle : String / 이벤트명\n" +
            "- shipName : String / 해당 이벤트가 속한 선박의 선박명\n" +
            "- startDay : String / 이벤트 시작일\n" +
            "- endDay : String / 이벤트 종료일\n")
    @GetMapping("/event/list/{page}")
    public Page<EventDtoForPage> getEventList(
            @PathVariable("page") int page,
            @RequestHeader(value = "Authorization",required = false) String token,
            EventSearchCondition dto
    ) throws EmptyListException {
//        return eventService.getEventList(page);
        Page<EventDtoForPage> events = eventService.getEventList(page, token, dto);
        if (events.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return events;
        }
    }

    /*이벤트 상세보기*/
    @ApiOperation(value = "이벤트 상세보기",notes = "" +
            "요청 필드 ) \n" +
            "- eventId : 상세보기하려는 이벤트의 id\n" +
            "- 헤더의 세션토큰(선택)" +
            "응답 필드 ) \n" +
            "- eventId : Long / 이벤트의 id\n" +
            "- shipId : Long / 해당 이벤트가 속한 선박 id\n" +
            "- eventTitle : String / 이벤트명\n" +
            "- createdDate : String / 이벤트 생성일 / yyyy-MM-dd \n" +
            "- imageUrlList : ArrayList<String> / 이벤트 내용의 이미지파일 url\n" +
            "- content : String / 이벤트 내용\n" +
            "- likeCount : Integer / 좋아요 수\n" +
            "- commentCount : Integer / 댓글 수 \n" +
            "- isLikeTo : Boolean / 현재 회원이 해당 이벤트에 대해 좋아요를 눌렀는지 여부\n")
    @GetMapping("/event/detail")
    public EventDto getEventDetail(
            @RequestParam("eventId") Long eventId,
            @RequestHeader(value = "Authorization", required = false) String token
    ) throws ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}

        return eventService.getEventDetail(eventId, token);
    }

    /*이벤트 작성*/
    @ApiOperation(value = "이벤트 생성",notes = "" +
            "")
    @PostMapping("/event")
    public Long makeEvent(
            @RequestBody MakeEventDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        /*입력값 검증*/
        if (dto.getFileList() == null) { dto.setFileList(new Long[0]);}
        if(dto.getStartDate().isAfter(dto.getEndDate())){throw new RuntimeException("시작일은 종료일 이전이어야 합니다.");}

        return eventService.makeEvent(dto, token);
    }

    /*이벤트 수정*/
    @ApiOperation(value = "이벤트 수정",notes = "")
    @PutMapping("/event")
    public Boolean modifyEvent(
            @RequestBody @Valid ModifyEventDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        //입력값 검증.
        if (dto.getFileList() == null) { dto.setFileList(new Long[0]);}
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(()->new ResourceNotFoundException("event not found for this id :: "+dto.getEventId()));
        LocalDate.parse(event.getStartDay());
        LocalDate startDate = (dto.getStartDate()==null)? LocalDate.parse(event.getStartDay()):dto.getStartDate();
        LocalDate endDate = (dto.getEndDate()==null)? LocalDate.parse(event.getEndDay()):dto.getEndDate();
        if(startDate.isAfter(endDate)){throw new RuntimeException("시작일은 종료일 이전이어야 합니다.");}

        return eventService.modifyEvent(dto,token);
    }

    //이벤트 비활성화
    @ApiOperation(value = "비활성화")
    @PutMapping("/event/hide/{id}/{active}")
    public Boolean hideEvent(
            @PathVariable("id") Long eventId,
            @PathVariable("active") String active,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        if(!active.equals("true") && !active.equals("false") ){ throw new RuntimeException("active값에는 'true'또는 'false'만 가능합니다.");}
        return eventService.hideEvent( eventId, active, token);
    }
    //이벤트 삭제
    @ApiOperation(value = "이벤트 삭제",notes = "")
    @DeleteMapping("/event/{id}")
    public Boolean deleteEvent(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return eventService.deleteEvent(id,token);
    }
}
