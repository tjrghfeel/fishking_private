package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class ReservePersonDTO {

    @ApiParam(value = "승선자 이름")
    private @Valid String name;

    @ApiParam(value = "승선자 전화번호")
    private @Valid String phone;

    @ApiParam(value = "승선자 생년월일")
    private @Valid String birthdate;
}
