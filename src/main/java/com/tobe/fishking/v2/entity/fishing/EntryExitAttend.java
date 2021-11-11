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
public class EntryExitAttend extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '출입항신고서'")
    private EntryExitReport report;

    @Column(columnDefinition = "varchar(20) comment '이름'")
    private String name;

    @Column(columnDefinition = "varchar(10) comment '생년월일'")
    private String birth;

    @Column(columnDefinition = "varchar(1) comment '성별'")
    private String sex;

    @Column(columnDefinition = "varchar(15) comment '전화번호'")
    private String phone;

    @Column(columnDefinition = "varchar(300) comment '주소'")
    private String addr;

    @Column(columnDefinition = "varchar(15) comment '비상연락처'")
    private String emerNum;

    @Column(columnDefinition = "varchar(15) comment '승선자 구분코드 0:승선객, 1:선장, 2:선원'")
    private String type;

    @Convert(converter = CryptoStringConverter.class)
    @Column(columnDefinition = "varchar(300) comment '주민등록번호 승선객의 경우 빈문자열'")
    private String idNumber;

    @Column(columnDefinition = "bigint comment '승선자 id'")
    private Long rideShipId;

    @Builder
    public EntryExitAttend(String name,
                           String birth,
                           String sex,
                           String phone,
                           String addr,
                           String emerNum,
                           String type,
                           String idNumber,
                           Long rideShipId,
                           EntryExitReport report) {
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.phone = phone;
        this.addr = addr;
        this.emerNum = emerNum;
        this.type = type;
        this.idNumber = idNumber;
        this.rideShipId = rideShipId;
        this.report = report;
    }

    public void updateAttend(String name,
                             String phone,
                             String emerNum,
                             String addr,
                             String idNumber,
                             String birth,
                             String type,
                             String sex) {
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.phone = phone;
        this.addr = addr;
        this.emerNum = emerNum;
        this.type = type;
        this.idNumber = idNumber;
    }
}
