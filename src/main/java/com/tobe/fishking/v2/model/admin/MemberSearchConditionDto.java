package com.tobe.fishking.v2.model.admin;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberSearchConditionDto {
    private Long id;
//    @Value("${some.key:-1}")
    private Integer role;
    private String memberName;//암호화필요
    private String uid;
    private String email;
    private String nickName;
    private Integer gender;
    private Boolean isActive;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate joinDtStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate joinDtEnd;
    private Integer snsType;
    private String snsId;
    private String city;//암호화필요
    private String gu;//암호화필요
    private String dong;//암호화필요
    private String areaCode;//암호화필요
    private String localNumber;//암호화필요
}
