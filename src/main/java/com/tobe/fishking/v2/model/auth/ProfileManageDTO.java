package com.tobe.fishking.v2.model.auth;

import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.common.PhoneNumberInfo;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProfileManageDTO {
    public Long id;
    public String uid;
    //public String memberName;
    public String nickName;
    //public String password;
    public String email;
    //private Gender gender;
    //private Role roles;
    //private String sessionToken;
    private String profileImage; //프로파일 사진 경로+이름
    //private String confirmPassword;
    //private Boolean isActive;
    //private String certifiedNo;
    //private Boolean isCertified;
    //private String joinDt;
    //private SNSType snsType ;
    //private String snsId ;
    private String statusMessage ;
    //private Address address;
    //private PhoneNumber phoneNumber;
    private String areaCode;
    private String localNumber;
    //private PhoneNumberInfo phoneNumberInfo;


}
