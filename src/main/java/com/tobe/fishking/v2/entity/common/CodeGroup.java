package com.tobe.fishking.v2.entity.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.model.CodeGroupWriteDTO;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "code_group") //ad
// EXEC sp_addextendedproperty 'MS_Description', N'코드그룹', 'USER', DBO, 'TABLE', code_group
public class CodeGroup extends BaseTime {

        // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', code_group, 'COLUMN',  id
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

        // EXEC sp_addextendedproperty 'MS_Description', N'명칭', 'USER', DBO, 'TABLE', code_group, 'COLUMN',  name
        @Column(columnDefinition = "varchar(50)   comment '코드'  ")
        private String code;

        // EXEC sp_addextendedproperty 'MS_Description', N'명칭', 'USER', DBO, 'TABLE', code_group, 'COLUMN',  name
        @Column(columnDefinition = "varchar(200)   comment '명칭'  ")
        private String name;

        // EXEC sp_addextendedproperty 'MS_Description', N'설명', 'USER', DBO, 'TABLE', code_group, 'COLUMN',  description
        @Column(columnDefinition = "varchar(500)   comment '명칭'  ")
        private String description;


        // EXEC sp_addextendedproperty 'MS_Description', N'비고', 'USER', DBO, 'TABLE', code_group, 'COLUMN',  remark
     @Column(columnDefinition = "varchar(500)   comment '비고'  ")
        private String remark;

        // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', code_group, 'COLUMN',  created_by
        @ManyToOne
        @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
        @JsonIgnore
        private    Member createdBy;

        // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', code_group, 'COLUMN',  modified_by
        @ManyToOne
        @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
        @JsonIgnore
        private Member modifiedBy;

        public CodeGroup(String code, String name, String description, Member createdBy) {
            this.code = code;
            this.name = name;
            this.description = description;
            this.createdBy = createdBy;
        }

        public void updateCodeGroup(CodeGroupWriteDTO dto, Member modifiedBy){
            name = dto.getName();
            code = dto.getCode();
            description = dto.getDescription();
            remark = dto.getRemark();
            this.modifiedBy = modifiedBy;

        }
}
