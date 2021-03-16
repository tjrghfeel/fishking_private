package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class AddEvent {

    @ApiParam(value = "이벤트 제목")
    private @Valid String title;
    @ApiParam(value = "이벤트 내용")
    private @Valid String contents;
    @ApiParam(value = "이미지 아이디")
    private @Valid Long image_id;

}
