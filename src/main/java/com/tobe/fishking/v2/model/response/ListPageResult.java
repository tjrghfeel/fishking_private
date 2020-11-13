package com.tobe.fishking.v2.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListPageResult<T> extends CommonResult {
    private List<T> list;
}