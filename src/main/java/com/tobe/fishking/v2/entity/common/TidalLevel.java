package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TidalLevel extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    private Long id;

    @Column(columnDefinition = "int comment '조위'")
    private Integer level;

    @Column(columnDefinition = "date comment '날짜'")
    private LocalDate date;

    @Column(columnDefinition = "datetime comment '날짜 시간'")
    private LocalDateTime dateTime;

    @Column(columnDefinition = "varchar(6) comment '만조간조' ")
    private String peak;

    @ManyToOne
    @JoinColumn(name="observer_code_id", columnDefinition  = " bigint not null comment '관측소코드' ")
    private ObserverCode observerCode;

}
