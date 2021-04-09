package com.tobe.fishking.v2.model.smartsail;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class AddRiderDTO {

    @ApiParam(value = "주문아이디")
    private @Valid Long orderId;
    @ApiParam(value = "승선자 이름")
    private @Valid String name;
    @ApiParam(value = "생년월일")
    private @Valid String birthDate = "";
    @ApiParam(value = "연락처")
    private @Valid String phone = "";
    @ApiParam(value = "비상연락처")
    private @Valid String emergencyPhone;

}
