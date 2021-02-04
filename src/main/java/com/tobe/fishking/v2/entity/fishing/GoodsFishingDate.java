package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static com.tobe.fishking.v2.utils.DateUtils.getDateFromString;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "goods_fishing_date")
public class GoodsFishingDate extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // IDENTITY
    @Column(updatable = false, nullable = false, columnDefinition = "bigint comment 'id' ")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goods_id", columnDefinition = "bigint  not null comment '상품'  ")
    private Goods goods;

    @Column(columnDefinition = "datetime comment '조업일' ")
    private LocalDate fishingDate;

    @Column(columnDefinition = "varchar(10) commnet '조업일 문자열' ")
    private String fishingDateString;

    @Column(columnDefinition = "int comment '예약된 인원'")
    private Integer reservedNumber;

    @Column(columnDefinition = "int comment '대기중인 인원'")
    private Integer waitNumber;



    @ManyToOne
    @JoinColumn(name="created_by" ,   updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;

    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = " bigint not null comment '수정자'")
    private Member modifiedBy;


    @Builder
    public GoodsFishingDate(Goods goods, String fishingDateString, Member member) {
        this.goods = goods;
        this.fishingDateString = fishingDateString;
        this.fishingDate = getDateFromString(fishingDateString);
        this.reservedNumber = 0;
        this.waitNumber = 0;
        this.createdBy = member;
        this.modifiedBy = member;
    }

    public void setFishingDate(String fishingDateString, Member member) {
        this.fishingDateString = fishingDateString;
        this.fishingDate = getDateFromString(fishingDateString);
        this.modifiedBy = member;
    }

    public void addReservedNumber(Integer number) {
        this.reservedNumber += number;
    }

    public void addWaitNumber(Integer number) {
        this.waitNumber += number;
    }
}
