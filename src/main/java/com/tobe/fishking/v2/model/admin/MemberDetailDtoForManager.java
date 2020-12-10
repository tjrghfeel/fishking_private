package com.tobe.fishking.v2.model.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberDetailDtoForManager {
    private Long id;
    @NotNull
    private String uid;
    private String memberName;
    @NotNull(message = "ccc")
    @Size(min = 4, max = 10)
    private String nickName;
    @Size(min = 8, max = 16, message = "비밀번호는 8~14자")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    private String password;
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 잘못되었습니다")
    @NotNull(message = "이메일이 비었습니다")
    private String email;
    @NotNull(message = "성별이 비었습니다")
    private Integer gender;
    @NotNull
    private Integer roles;
    private String profileImage;
    @Value("${some.key:1}")
    private Boolean isActive;
    private String certifiedNo;
    private Boolean isCertified;
    private String joinDt;
    private Integer snsType;
    private String snsId;
    private String statusMessage;
    private String city;
    private String gu;
    private String dong;
    @NotNull(message = "휴대폰 번호가 비었습니다. ")
    private String areaCode;
    @NotNull(message = "휴대폰 번호가 비었습니다. ")
    private String localNumber;
}
