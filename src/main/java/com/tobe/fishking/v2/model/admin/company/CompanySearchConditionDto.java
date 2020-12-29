package com.tobe.fishking.v2.model.admin.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CompanySearchConditionDto {
    private Long companyId;//
    private Long memberId;//
    private String memberName;//
    private String companyName;//
    private String sido;//
    private String gungu;//
    private String tel;//
    private String bizNo;//
    private String bank;//
    private String accountNum;//
    private String harbor;
    private String ownerWording;
    private Boolean isOpen;//
    private String skbAccount;//
    private String companyAddress;//
    private Boolean isRegistered;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateStart;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateEnd;//
    private String sort;
}
