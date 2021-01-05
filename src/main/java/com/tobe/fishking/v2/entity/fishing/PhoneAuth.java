package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.service.StringConverter;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "phone_auth")
public class PhoneAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(nullable = false, columnDefinition = "varchar(6) comment '인증번호'")
    private String certifyNum;

    @Column(nullable = false, columnDefinition = "bit default '0' comment '인증완료여부'")
    private Boolean isCertified;

}
