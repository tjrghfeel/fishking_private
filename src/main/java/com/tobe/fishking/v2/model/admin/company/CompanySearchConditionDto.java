package com.tobe.fishking.v2.model.admin.company;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySearchConditionDto {
    private Long companyId;//
    private Long memberId;//
    private String memberName;//
    private String memberNickName;
    private String areaCode;//d
    private String localNumber;//d
    private String companyName;//
    private String shipOwner;
    private String sido;//
    private String gungu;
    private String tel;
    private String phoneNumber;
    private String bizNo;//
    private String harbor;
    private String accountNum;//
    private String bank;
    private Boolean isOpen;//
    private String skbAccount;//
    private String companyAddress;//
    private Boolean isRegistered;//
    private String adtId;
    private String nhnId;
    private Long createdBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateStart;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateEnd;//
    private String shipName;

    private Integer pageCount = 10;
    private String sort;
}
