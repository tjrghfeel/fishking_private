package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.EventDto;
import com.tobe.fishking.v2.model.common.EventDtoForPage;
import com.tobe.fishking.v2.service.common.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"이벤트"})
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
@RestController
public class EventController {

    @Autowired
    EventService eventService;

    /*이벤트 목록 보기*/
    @ApiOperation(value = "이벤트 목록",notes = "" +
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
            @PathVariable("page") int page
    ) throws EmptyListException {
//        return eventService.getEventList(page);
        Page<EventDtoForPage> events = eventService.getEventList(page);
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
}
