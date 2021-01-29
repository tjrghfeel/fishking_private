package com.tobe.fishking.v2.entity.board;


import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import lombok.*;

import javax.persistence.*;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "Accuse")
@Table(name = "accuse")  //댓글신고,
public class Accuse extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', accuse, 'COLUMN',  id
@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    @Column(columnDefinition = "int not null comment '신고 대상 종류'")
    private EntityType targetType;

    @Column(columnDefinition = "bigint not null comment '신고 대상의 id'")
    private Long linkId;
    
    // EXEC sp_addextendedproperty 'MS_Description', N'내용', 'USER', DBO, 'TABLE', accuse, 'COLUMN',  report_contents
    @Column(columnDefinition = "varchar(2000) comment '신고내용'")
    private String reportContents;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', accuse, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , updatable= false , columnDefinition = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;


}
