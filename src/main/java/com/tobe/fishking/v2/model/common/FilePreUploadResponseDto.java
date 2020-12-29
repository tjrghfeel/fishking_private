package com.tobe.fishking.v2.model.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilePreUploadResponseDto {
    private String downloadUrl;
    private Long fileId;
}
