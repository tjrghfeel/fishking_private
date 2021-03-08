package com.tobe.fishking.v2.model.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletingTakeDto {
    private Long takeId;
    private Long linkId;
}
