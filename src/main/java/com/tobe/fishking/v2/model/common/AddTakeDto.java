package com.tobe.fishking.v2.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AddTakeDto {
    private Long linkId;
    private int takeType;
    private Long memberId;

}
