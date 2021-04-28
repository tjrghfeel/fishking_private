package com.tobe.fishking.v2.model.admin.member;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSearchConditionDto {
//    private Long id;
//    @Value("${some.key:-1}")
    private String roles;
//    @Size(max = 100,message = "회원명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "회원명은 한글,숫자,영문자로 구성되어야합니다")
    private String memberName;//암호화필요
//    @Size(max = 100,message = "회원 아이디는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "회원 아이디는 한글,숫자,영문자로 구성되어야합니다")
    private String uid;
//    private String email;
//    @Size(max = 100,message = "닉네임은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "닉네임은 한글,숫자,영문자로 구성되어야합니다")
    private String nickName;
//    private Integer gender;
    private Boolean isActive;
//    private String certifiedNo;
//    private Boolean isCertified;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate joinDtStart;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate  joinDtEnd;
//    private Integer snsType;
//    private String snsId;
//    private String city;//암호화필요
//    private String gu;//암호화필요
//    private String dong;//암호화필요
//@Size(max = 100,message = "휴대폰번호 앞자리는 100자 이하이어야합니다")
//@Pattern(regexp = Constants.NUMBER, message = "휴대폰번호 앞자리는 숫자로 구성되어야합니다")
    private String areaCode;//암호화필요

//    @Size(max = 100,message = "휴대폰번호 뒷자리는 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.NUMBER, message = "휴대폰번호 뒷자리는 숫자로 구성되어야합니다")
    private String localNumber;//암호화필요

    private Boolean isSuspended;

//    @Size(max = 100,message = "정렬기준은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "정렬기준은 한글,숫자,영문자로 구성되어야합니다")
    private String sort="createdDate";
    
    private Integer pageCount=10;
}
