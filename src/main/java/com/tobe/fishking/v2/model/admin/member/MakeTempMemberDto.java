package com.tobe.fishking.v2.model.admin.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeTempMemberDto {

    private String uid;
    private String nickName;
    private String pw;
    private String role;
}
