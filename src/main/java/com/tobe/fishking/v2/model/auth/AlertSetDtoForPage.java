package com.tobe.fishking.v2.model.auth;

import com.tobe.fishking.v2.entity.board.Board;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertSetDtoForPage {
    private String code;
    private String codeName;
    private Boolean isSet;

}
