package com.tobe.fishking.v2.model.auth;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyAlertSetDto {
    private List<String> alertSetCodeList;
}
