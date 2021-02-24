package com.tobe.fishking.v2.entity.auth;

import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.service.StringConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String uid;

    @Convert(converter = StringConverter.class)
    @Column(name = "member_name")
    public String memberName;

    @Column(name = "nick_name")
    public String nickName;

    @Column
    public String password;

    // EXEC sp_addextendedproperty 'MS_Description', N'이메일', 'USER', DBO, 'TABLE', member, 'COLUMN' email
//    @Convert(converter = StringConverter.class)
    @Column(columnDefinition = "varchar(150) NOT NULL   comment '이메일'  ")
    public String email;

    @Column(columnDefinition = "int comment '성별'")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Enumerated(EnumType.ORDINAL)
    @Column
    private Role roles;

    @Column(name = "session_token")
    private String sessionToken;

    @Column(name = "profile_image")
    private String profileImage; //프로파일 사진 경로+이름

    @Column(name = "profile_background")
    private String profileBackgroundImage;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "certified_no")
    private String certifiedNo;

    @Column(name = "is_certified")
    private Boolean isCertified;

    @Column(name = "join_dt")
    private String joinDt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "sns_type")
    private SNSType snsType;

    @Column(name = "sns_id")
    private String snsId;


    @Column(name = "status_message")
    private String statusMessage;

    @Transient
    private String confirmPassword;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Address address;

    @Column(columnDefinition = "varchar(100) comment '푸쉬알림용 등록토큰'")
    private String registrationToken;

    @Builder
    public Member(Long id, String uid,
                  String memberName, String nickName,
                  String password, String email, Gender gender,
                  Role roles, String sessionToken, String profileImage, String profileBackgroundImage,
                  Boolean isActive, String certifiedNo, Boolean isCertified,
                  String joinDt, SNSType snsType, String snsId, String statusMessage, PhoneNumber phoneNumber,
                  Address address) {
        this.id = id;
        this.uid = uid;
        this.memberName = memberName;
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.roles = roles;
        this.sessionToken = sessionToken;
        this.profileImage = profileImage;
        this.profileBackgroundImage = profileBackgroundImage;
        this.isActive = isActive;
        this.certifiedNo = certifiedNo;
        this.isCertified = isCertified;
        this.joinDt = joinDt;
        this.snsType = snsType;
        this.snsId = snsId;
        this.statusMessage = statusMessage;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }





    //Getters and setters ommitted for brevity
//    @ManyToMany(mappedBy = "coupons")
    //   private List<Coupon> coupons = new ArrayList<>();

    public void deActivateMember() {
        isActive = false;
    }
    public void setRegistrationToken(String token){this.registrationToken = token;}
    public void setMemberName(String name){this.memberName = name;}
}
