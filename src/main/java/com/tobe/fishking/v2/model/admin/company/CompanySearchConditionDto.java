package com.tobe.fishking.v2.model.admin.company;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySearchConditionDto {

    private Long companyId;//
    private Long memberId;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String memberName;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String memberNickName;
    @Size(max=100)
    @Pattern(regexp = Constants.NUMBER)
    private String areaCode;//d
    @Size(max=100)
    @Pattern(regexp = Constants.NUMBER)
    private String localNumber;//d
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String companyName;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String shipOwner;
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String sido;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String gungu;
    @Size(max=100)
    @Pattern(regexp = Constants.NUMBER)
    private String tel;
    @Size(max=100)
    @Pattern(regexp = Constants.NUMBER)
    private String phoneNumber;
    @Size(max=100)
    @Pattern(regexp = Constants.NUMBER)
    private String bizNo;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String harbor;
    @Size(max=100)
    @Pattern(regexp = Constants.NUMBER)
    private String accountNum;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String bank;
    private Boolean isOpen;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String skbAccount;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String companyAddress;//
    private Boolean isRegistered;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String adtId;
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String nhnId;
    private Long createdBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateStart;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateEnd;//
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String shipName;

    private Integer pageCount = 10;
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String sort="createdDate";
}
