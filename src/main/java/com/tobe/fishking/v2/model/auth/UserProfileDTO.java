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
import java.util.List;

/*사용자 프로필 눌렀을때 보이는 정보전달을 위한 DTO. isMe, isShip필드값에 따라 들어있는 정보가 조금씩 다르다.  */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    public Long memberId;
//    public String uid;
//    public String memberName;
    public String nickName;
//    public String password;
//    public String email;
//    private Gender gender;
//    private Role roles;
//    private String sessionToken;
    private String profileImage; //프로파일 사진 경로+이름
//    @Transient
//    @NotBlank
//    private String confirmPassword;
    private Boolean isActive;
//    private String certifiedNo;
//    private Boolean isCertified;
//    private String joinDt;
//    private SNSType snsType ;
//    private String snsId ;
//    private String statusMessage ;
//    @Embedded
//    private Address address;
//    private String city;
//    private String gu;
//    private String dong;
//    @Embedded
//    private PhoneNumber phoneNumber;
//    private String areaCode;
//    private String localNumber;
//    private PhoneNumberInfo phoneNumberInfo;


    private int postCount;//작성글수
    private int likeCount;//좋아요수??
    private Boolean isMe;//현재 프로필DTO가 본인의 것임을 나타내는 필드.
    private int takeCount;//업체 찜 수??. 본인의 프로필일 경우 추가되는 필드.

    //업체인 경우 추가되는 필드.
    private Boolean isShip;//이 멤버가 업체(ship)인지를 나타내는 필드.
    private Long shipId;//ship id
    private String shipName;//선상명
    private String sido;//주소
    private List<String> fishSpecies;//어종 리스트



}
