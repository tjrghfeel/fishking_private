package com.tobe.fishking.v2.model.common;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFileDto {
    @NotNull
    private Long fileId;
}
