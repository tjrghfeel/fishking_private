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
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String memberName;//암호화필요
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String uid;
//    private String email;
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
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
    @Size(max = 100)
    @Pattern(regexp = Constants.NUMBER)
    private String areaCode;//암호화필요
    @Size(max = 100)
    @Pattern(regexp = Constants.NUMBER)
    private String localNumber;//암호화필요
    private Boolean isSuspended;

    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String sort="createdDate";
    private Integer pageCount=10;
}
