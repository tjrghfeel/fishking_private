package com.tobe.fishking.v2.model.auth;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckUidDto {
    @NotNull
    private Long phoneAuthId;
}
