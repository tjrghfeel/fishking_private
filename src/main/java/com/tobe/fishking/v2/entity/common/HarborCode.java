package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class HarborCode extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "varchar(50) comment '항구명' ")
    private String name;

    @Column(columnDefinition = "varchar(20) comment '항구코드' ")
    private String code;

    @Column(columnDefinition = "varchar(1000) comment '항구 법정동' ")
    private String dong;

    @Column(columnDefinition = "varchar(20) comment '법정동 코드' ")
    private String dongCode;


    @Builder
    public HarborCode(String name, String code, String dong, String dongCode) {
        this.name = name;
        this.code = code;
        this.dong = dong;
        this.dongCode = dongCode;
    }
}
