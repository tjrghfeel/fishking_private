package com.tobe.fishking.v2.entity.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PHONE_NUMBER_INFO")
@Getter
@Setter
public class PhoneNumberInfo {
    @Id
    @Column(name = "PHONE_ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PHONE_SEQ_GENERATOR")
    @TableGenerator(
            name = "PHONE_SEQ_GENERATOR",
            table = "MY_SEQUENCE",
            pkColumnName = "SEQ_NAME", //MY_SEQUENCE 테이블에 생성할 필드이름(시퀀스네임)
            pkColumnValue = "PHONE_SEQ", //SEQ_NAME이라고 지은 칼럼명에 들어가는 값.(키로 사용할 값)
            allocationSize = 1
    )
    private Long id;

    @Column(name = "PHONE_ORNER_NAME")
    private String name;

    @Column(name = "ORNER_AGE")
    private int age;

    /*@OneToOne(mappedBy = "info")
    private PhoneNumber number;*/
}
