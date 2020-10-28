package com.tobe.fishking.v2.entity.auth;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "Member")
@Table(name = "member")
public class Member extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', member, 'COLUMN' id
    public Long id;

    @Column(nullable = false, unique = true, length = 50, columnDefinition = " comment '코드' ")
    public String uid;

    // EXEC sp_addextendedproperty 'MS_Description', N'이름', 'USER', DBO, 'TABLE', member, 'COLUMN' username
    @Column(columnDefinition = "nvarchar(100) null   comment '이름'  ")
    public String memberName;
    
    // EXEC sp_addextendedproperty 'MS_Description', N'별명', 'USER', DBO, 'TABLE', member, 'COLUMN' nickname
    @Column(columnDefinition = "varchar(100) null   comment '별명'  ")
    public String nickName;

    // EXEC sp_addextendedproperty 'MS_Description', N'패스워드', 'USER', DBO, 'TABLE', member, 'COLUMN' password
    @Column(columnDefinition = "varchar(100) not null   comment '패스워드'  ")
    public String password;

    // EXEC sp_addextendedproperty 'MS_Description', N'이메일', 'USER', DBO, 'TABLE', member, 'COLUMN' email
    @Column(columnDefinition = "varchar(20) NOT NULL   comment '이메일'  ")
    public String email;

    // EXEC sp_addextendedproperty 'MS_Description', N'성별', 'USER', DBO, 'TABLE', member, 'COLUMN' gender
    @Column(columnDefinition = "int NOT NULL   comment '성별'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private Gender gender;

    // EXEC sp_addextendedproperty 'MS_Description', N'역할', 'USER', DBO, 'TABLE', ad, 'COLUMN',  role
    @Column(columnDefinition = "int NOT NULL   comment '역할'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private Role roles;


    // EXEC sp_addextendedproperty 'MS_Description', N'토큰', 'USER', DBO, 'TABLE', member, 'COLUMN' sessionToken
    @Column(columnDefinition = "varchar(100) NULL   comment '토큰'  ")
    private String sessionToken;
    
    // EXEC sp_addextendedproperty 'MS_Description', N'프로파일이미지', 'USER', DBO, 'TABLE', member, 'COLUMN' profile_image
    @Column(columnDefinition = "varchar(100) NOT NULL   comment '프로파일이미지'  ")
    private String profileImage; //프로파일 사진 경로+이름

    @Transient
    @NotBlank
    // EXEC sp_addextendedproperty 'MS_Description', N'패스워드확인', 'USER', DBO, 'TABLE', member, 'COLUMN' confirm_password
    @Column(nullable = false, columnDefinition = "varchar(100)   comment '패스워드확인'  ")
    private String confirmPassword;

    // EXEC sp_addextendedproperty 'MS_Description', N'활성여부-사용여부', 'USER', DBO, 'TABLE', member, 'COLUMN' is_active
    @Column(nullable = false, columnDefinition = "bit comment '활성여부-사용여부'  ")
    private Boolean isActive;

    // EXEC sp_addextendedproperty 'MS_Description', N'인증번호', 'USER', DBO, 'TABLE', member, 'COLUMN' certifiedNo
    @Column(nullable = false, columnDefinition = "varchar(100)   comment '인증번호'  ")
    private String certifiedNo;

    // EXEC sp_addextendedproperty 'MS_Description', N'인증여부', 'USER', DBO, 'TABLE', member, 'COLUMN' is_certified
    @Column(nullable = false, columnDefinition = "bit   comment '인증여부'  ")
    private Boolean isCertified;

    // EXEC sp_addextendedproperty 'MS_Description', N'가입일', 'USER', DBO, 'TABLE', member, 'COLUMN' joinDt
    @Column(nullable = false, columnDefinition = "varchar(8)   comment '가입일'  ")
    private String joinDt;

    // EXEC sp_addextendedproperty 'MS_Description', N'SNS유형', 'USER', DBO, 'TABLE', member, 'COLUMN' is_certified
    @Column(nullable = false, columnDefinition = "bit   comment 'SNS유형'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private SNSType snsType ;

    // EXEC sp_addextendedproperty 'MS_Description', N'SNS ID', 'USER', DBO, 'TABLE', member, 'COLUMN' is_certified
    @Column(nullable = false, columnDefinition = "varchar(50)   comment 'SNS ID'  ")
    private String snsId ;

    // EXEC sp_addextendedproperty 'MS_Description', N'상태메세지', 'USER', DBO, 'TABLE', member, 'COLUMN' is_certified
    @Column(nullable = false, columnDefinition = "varchar(20)   comment '상태메세지'  ")
    private String statusMessage ;



    //Getters and setters ommitted for brevity
//    @ManyToMany(mappedBy = "coupons")
 //   private List<Coupon> coupons = new ArrayList<>();

}
