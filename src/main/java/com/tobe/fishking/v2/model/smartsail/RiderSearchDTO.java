package com.tobe.fishking.v2.model.smartsail;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class RiderSearchDTO {

    @ApiParam(value = "검색 시작일", example = "2021-01-01")
    private @Valid String startDate = "1900-01-01";
    @ApiParam(value = "검색 종료일", example = "2021-06-01")
    private @Valid String endDate = "2100-12-31";
    @ApiParam(value = "검색어")
    private @Valid String keyword = "";
    @ApiParam(value = "검색어 타입. username: 예약자명, riderName: 승선자명")
    private @Valid String keywordType = "";
    @ApiParam(value = "상태. wait: 이용예정, complete: 이용완료, cancel: 취소완료")
    private @Valid String status = "";

}