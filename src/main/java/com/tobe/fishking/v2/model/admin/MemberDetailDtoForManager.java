package com.tobe.fishking.v2.model.admin;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDetailDtoForManager {
    private Long id;
    @NotNull(message = "uid가 비었습니다")
    private String uid;
    private String memberName;
    @Size(min = 4, max = 10)
    private String nickName;
    @Size(min = 8, max = 16, message = "비밀번호는 8~14자")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
            message = "비밀번호는 특수문자,숫자,영어 포함 8자 이상 14자 이하이어야 합니다. ")
    private String password;
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 잘못되었습니다")
    @NotNull(message = "이메일이 비었습니다")
    private String email;
    @NotNull(message = "성별이 비었습니다")
    private Integer gender;
    @NotNull(message = "회원 유형이 비었습니다")
    private Integer roles;
    private String profileImage;
    @NotNull(message = "활성화 여부가 비었습니다")
    private Boolean isActive;
    private String certifiedNo;
    @NotNull(message = "인증여부가 비었습니다")
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
