package com.tobe.fishking.v2.entity;


import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "files_group")
public class FilesGroup extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'업로드(어선,게시판등)구분', 'USER', DBO, 'TABLE', files_group, 'COLUMN',  file_flag
    @Column(columnDefinition = "varchar(20)   comment '업로드(어선,게시판등)구분'  ")
    private String fileFlag;

    //@Column(columnDefinition = "boolean default false not null comment '삭제여부'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'삭제여부', 'USER', DBO, 'TABLE', files_group, 'COLUMN',  created_date
    // @Column(columnDefinition = "bit default false not null ")
    @Column(columnDefinition = "bit default 0 not null  comment '삭제여부' ") //mssql 은 false 안됨 , 0만 된다.
    private boolean isDelete;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', files_group, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,   updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', files_group, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = " bigint not null comment '수정자'")
    private Member modifiedBy;



}
