package com.tobe.fishking.v2.model.admin;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsSearchConditionDto {
    private Long goodsId;
    private String goodsName;
    private Integer priceStart;
    private Integer priceEnd;
    private String shipName;

    //날짜 조건
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fishingDateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fishingDateEnd;

    private String[] speciesList = new String[0];

    private Integer pageCount =10;
}
