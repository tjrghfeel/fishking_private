package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class AddEvent {

    @ApiParam(value = "이벤트 id (신규 작성의 경우 null)")
    private @Valid Long eventId;
    @ApiParam(value = "이벤트 제목")
    private @Valid String title;
    @ApiParam(value = "시작 일")
    private @Valid String startDate;
    @ApiParam(value = "종료 일")
    private @Valid String endDate;
    @ApiParam(value = "이벤트 내용")
    private @Valid String contents;
    @ApiParam(value = "이미지 아이디 ")
    private @Valid Long imageId;
    @ApiParam(value = "이미지 url (신규의 경우 null) ")
    private @Valid String imageUrl;

}
