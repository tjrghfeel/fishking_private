package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class SearchOrdersDTO {

    @ApiParam(value = "검색 시작일")
    public @Valid String startDate = "";
    @ApiParam(value = "검색 종료일")
    public @Valid String endDate = "";
    @ApiParam(value = "주문상태, ")
    public @Valid String status = "";
    @ApiParam(value = "결제방법")
    public @Valid String payMethod = "";
    @ApiParam(value = "검색어 구분")
    public @Valid String keywordType = "";
    @ApiParam(value = "검색어")
    public @Valid String keyword = "";

}
