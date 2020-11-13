package com.tobe.fishking.v2.model.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.FileEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FilesDTO {

    @ApiModelProperty(value = "파일 ID ")
    private Long filesId;

    @ApiModelProperty(value = "다운로드URL")
    private String downloadUrl;



    public static FilesDTO of(FileEntity files){
        return FilesDTO.builder()
                .filesId(files.getId())
                .downloadUrl(files.getDownloadUrl())
                .build();
    }


}
