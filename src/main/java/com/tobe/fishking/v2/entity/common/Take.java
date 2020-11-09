package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.TakeType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "take")
public class Take extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', take, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(columnDefinition = "int   comment 'id'  ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'구분', 'USER', DBO, 'TABLE', take, 'COLUMN',  task_flag
    @Column(columnDefinition = "int   comment '구분-상품, 조황일지등'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private TakeType takeType;

    // EXEC sp_addextendedproperty 'MS_Description', N'번호', 'USER', DBO, 'TABLE', take, 'COLUMN',  tid
    @Column(columnDefinition = "int   comment '찜ID'  ")
    private Long linkId;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', take, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null  comment '생성자'")
    private Member createdBy;

    //수정자 없음
}
