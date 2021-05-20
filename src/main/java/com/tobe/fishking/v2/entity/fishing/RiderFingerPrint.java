package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.fishing.FingerType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
public class RiderFingerPrint extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    private Long id;

    @Column(columnDefinition = "varchar(20) comment '이름' ")
    private String name;

    @Column(columnDefinition = "varchar(15) comment '전화번호' ")
    private String phone;

    @Column(columnDefinition = "int comment '지문 손가락 ' ")
    @Enumerated(EnumType.ORDINAL)
    private FingerType finger;

    @Column(columnDefinition = "varchar(2000) comment '지문정보' ")
    private String fingerprint;

    @Column(columnDefinition = "int comment '승선횟수' ")
    private Integer count;

    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;

    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Builder
    public RiderFingerPrint(String name,
                            String phone,
                            FingerType finger,
                            String fingerprint,
                            Member member) {
        this.name = name;
        this.phone = phone;
        this.finger = finger;
        this.fingerprint = fingerprint;
        this.count = 1;
        this.modifiedBy = member;
        this.createdBy = member;
    }

    public void updateCount(Member member) {
        this.count += 1;
        this.modifiedBy = member;
    }

    public void updateFingerprint(FingerType finger, String fingerprint, Member member) {
        this.finger = finger;
        this.fingerprint = fingerprint;
        this.modifiedBy = member;
    }
}
