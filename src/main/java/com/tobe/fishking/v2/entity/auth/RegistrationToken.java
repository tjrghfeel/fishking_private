package com.tobe.fishking.v2.entity.auth;

import com.tobe.fishking.v2.entity.BaseTime;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "registration_token")
@Builder
public class RegistrationToken extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255) comment '기기등록토큰'")
    private String token;

//    @Column(columnDefinition = "bit not null default 1 comment '해당 기기의 로그인 여부'")
//    Boolean

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint not null comment '회원'")
    private Member member;

    @Column(columnDefinition = "varchar(5) default 'c' comment '앱 타입 (고객: c 출조: f)' ")
    private String type;

}
