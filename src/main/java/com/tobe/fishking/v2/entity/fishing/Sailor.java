package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.converter.CryptoStringConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Sailor extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable= false , columnDefinition  = "bigint not null comment '생성자'")
    private Member createdBy;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Column(columnDefinition = "varchar(20) comment '선원 이름'")
    private String name;

    @Column(columnDefinition = "varchar(10) comment '선원 생년월일'")
    private String birth;

    @Column(columnDefinition = "varchar(1) comment '선원 성별'")
    private String sex;

    @Column(columnDefinition = "varchar(15) comment '선원 전화번호'")
    private String phone;

    @Column(columnDefinition = "varchar(300) comment '선원 주소'")
    private String addr;

    @Column(columnDefinition = "varchar(15) comment '선원 비상연락처'")
    private String emerNum;

    @Convert(converter = CryptoStringConverter.class)
    @Column(columnDefinition = "varchar(300) comment '선원 주민등록번호'")
    private String idNumber;

    @Builder
    public Sailor(Member member,
                  String name,
                  String birth,
                  String sex,
                  String phone,
                  String addr,
                  String emerNum,
                  String idNumber) {
        this.createdBy = member;
        this.modifiedBy = member;
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.phone = phone;
        this.addr = addr;
        this.emerNum = emerNum;
        this.idNumber = idNumber;
    }

    public void updateSailor(Member member,
                             String name,
                             String phone,
                             String emerNum,
                             String addr,
                             String idNumber,
                             String birth,
                             String sex) {
        this.createdBy = member;
        this.modifiedBy = member;
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.phone = phone;
        this.addr = addr;
        this.emerNum = emerNum;
        this.idNumber = idNumber;
    }
}
