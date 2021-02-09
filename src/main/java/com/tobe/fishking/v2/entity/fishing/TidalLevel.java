package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.service.StringConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "tidalLevel")
public class TidalLevel extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    @Column(columnDefinition = "date comment '날짜'")
    private LocalDate date;

    @Column(columnDefinition = "datetime comment '날짜시간'")
    private LocalDateTime dateTime;

    @Column(columnDefinition = "int comment '조위'")
    private Integer level;

    @Column(columnDefinition = "bit comment '만조여부'")
    private Boolean isHighWater;
    @Column(columnDefinition = "bit comment '간조여부'")
    private Boolean isLowWater;

    @ManyToOne
    @JoinColumn(name="observer_code_id", columnDefinition = "bigint not null comment '관측소 코드'")
    private ObserverCode observerCode;

    public void setHighWater(){this.isHighWater = true; }
    public void setLowWater(){this.isLowWater = true;}
}
