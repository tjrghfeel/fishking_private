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
//    @Size(max=100, message = "회원명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "회원명은 한글,숫자,영문자로 구성되어야합니다")
    private String memberName;//
//    @Size(max=100, message = "회원 닉네임은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "회원닉네임은 한글,숫자,영문자로 구성되어야합니다")
    private String memberNickName;
//    @Size(max=100, message = "회원 휴대전화번호 앞자리는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.NUMBER, message = "회원 휴대전화번호 앞자리는 숫자로 구성되어야합니다")
    private String areaCode;//d
//    @Size(max=100, message = "회원 휴대전화번호뒷자리는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.NUMBER, message = "회원 휴대전화번호뒷자리는 숫자로 구성되어야합니다")
    private String localNumber;//d
//    @Size(max=100, message = "업체명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "업체명은 한글,숫자,영문자로 구성되어야합니다")
    private String companyName;//
//    @Size(max=100, message = "업주명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "업주명은 한글,숫자,영문자로 구성되어야합니다")
    private String shipOwner;
//    @Size(max=100, message = "주소(시,도)는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "주소(시,도)는 한글,숫자,영문자로 구성되어야합니다")
    private String sido;//
//    @Size(max=100, message = "주소(군,구)는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "주소(군,구)는 한글,숫자,영문자로 구성되어야합니다")
    private String gungu;
//    @Size(max=100, message = "업체전화번호는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.NUMBER, message = "업체전화번호는 숫자로 구성되어야합니다")
    private String tel;
//    @Size(max=100, message = "휴대폰번호는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.NUMBER, message = "휴대전화번호는 숫자로 구성되어야합니다")
    private String phoneNumber;
//    @Size(max=100, message = "사업자등록번호는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.NUMBER2, message = "사업자등록번호 형식이 아닙니다")
    private String bizNo;//
//    @Size(max=100, message = "항구명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "항구명은 한글,숫자,영문자로 구성되어야합니다")
    private String harbor;
//    @Size(max=100, message = "계좌번호는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.NUMBER2, message = "계좌번호는 형식이 아닙니다")
    private String accountNum;//
//    @Size(max=100, message = "은행명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "은행명은 한글,숫자,영문자로 구성되어야합니다")
    private String bank;
    private Boolean isOpen;//
//    @Size(max=100, message = "SKB아이디는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "SKB아이디 한글,숫자,영문자로 구성되어야합니다")
    private String skbAccount;//
//    @Size(max=100, message = "지역명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "지역명은 한글,숫자,영문자로 구성되어야합니다")
    private String companyAddress;//
    private Boolean isRegistered;//
//    @Size(max=100, message = "NHN아이디는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = " 한글,숫자,영문자로 구성되어야합니다")
    private String nhnId;
    private Long createdBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateStart;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateEnd;//
//    @Size(max=100, message = "선박명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "선박명은 한글,숫자,영문자로 구성되어야합니다")
    private String shipName;

    private Integer pageCount = 10;
//    @Size(max=100, message = "정렬기준은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "정렬기준은 한글,숫자,영문자로 구성되어야합니다")
    private String sort="createdDate";
}
