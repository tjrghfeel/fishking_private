package com.tobe.fishking.v2.model.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.common.Popular;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PopularDTO {

    @ApiModelProperty(value = "인기검색어")
    private String searchKeyWord;

    public static PopularDTO of(Popular popular){
        return PopularDTO.builder()
                .searchKeyWord(popular.getSearchKeyWord())
                .build();
    }


}
