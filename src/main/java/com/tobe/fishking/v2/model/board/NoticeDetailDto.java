package com.tobe.fishking.v2.model.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public interface NoticeDetailDto {
    Long getId();
    @Value("#{@mapperUtility.transEnumChannelType(target.channelType)}")
    String getChannelType();
    String getTitle();
    LocalDateTime getDate();
    String getContents();
    @Value("#{@mapperUtility.transFileUrlArray(target.fileNameList, target.filePathList)}")
    ArrayList<String> getFileList();
}
