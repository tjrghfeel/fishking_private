package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class EntryExitReport extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "varchar(30) comment '시리얼번호'")
    private String serial;

    @Column(columnDefinition = "datetime comment '날짜'")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint not null comment '상품'")
    private Goods goods;

    @Column(columnDefinition = "varchar(20) comment '입항항구 코드'")
    private String entryHarborCode;

    @Column(columnDefinition = "datetime comment '입항시간'")
    private LocalDate entryTime;

    // 1: 신고제출 2: 신고확인 3: 입항 4: 출항취소 5: 운항중
    @Column(columnDefinition = "varchar(10) comment '상태'")
    private String status;

    @Builder
    public EntryExitReport(String serial,
                           LocalDate date,
                           Goods goods,
                           String entryHarborCode,
                           LocalDate entryTime) {
        this.serial = serial;
        this.date = date;
        this.goods = goods;
        this.entryHarborCode = entryHarborCode;
        this.entryTime = entryTime;
        this.status = "1";
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateEntryHarbor(String entryHarborCode,
                                  LocalDate entryTime) {
        this.entryHarborCode = entryHarborCode;
        this.entryTime = entryTime;
    }
}
