package com.tobe.fishking.v2.entity.common;


import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.model.CommonCodeWriteDTO;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
//@Table(name = "com_codes", comment = "Table for com_code") //com_code
@Entity
@Table(name = "common_code" ) //com_code
public class CommonCode extends BaseTime  {
        // EXEC sp_addextendedproperty 'MS_Description', N'코드', 'USER', DBO, 'TABLE', com_code

        @Id
        @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
        @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
        private Long id;

        // EXEC sp_addextendedproperty 'MS_Description', N'코드그룹', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  code_group_id
        @ManyToOne
        @JoinColumn(columnDefinition = "int NOT NULL comment '코드그룹'  ")
        private CodeGroup codeGroup;

        // EXEC sp_addextendedproperty 'MS_Description', N'코드', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  code
        //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
        @Column(columnDefinition = "varchar(10)   comment '코드'  ")
        private String code;


        // EXEC sp_addextendedproperty 'MS_Description', N'명칭', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  code_name
        @Column(columnDefinition = "varchar(200)   comment '명칭-이름'  ")
        private String codeName;

        // EXEC sp_addextendedproperty 'MS_Description', N'별칭', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  alias_name
        @Column(columnDefinition = "varchar(200)   comment '별칭'  ")
        private String aliasName;


        // EXEC sp_addextendedproperty 'MS_Description', N'대체값1 -string', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  extra_value1
        @Column(columnDefinition = "varchar(500)   comment '대체값1 -string'  ")
        private String extraValue1;

        // EXEC sp_addextendedproperty 'MS_Description', N'', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  ret_value1
        @Column(nullable = false, columnDefinition = "float not null default 0   comment '대체값1-double'  ")
        private Double retValue1;

        // EXEC sp_addextendedproperty 'MS_Description', N'', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  is_active
        @Column(nullable = false, columnDefinition = "bit default 1   comment '사용여부'  ")
        private Boolean isActive;

        // EXEC sp_addextendedproperty 'MS_Description', N'레벨', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  i_level
        @Column(nullable = false, columnDefinition = "INT  default 1   comment '레벨'  ")
        private Integer iLevel;

        // EXEC sp_addextendedproperty 'MS_Description', N'순서', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  order_by
        @Column(nullable = false, columnDefinition = "INT  default 1   comment '순서'  ")
        private Integer orderBy;


        // EXEC sp_addextendedproperty 'MS_Description', N'비고', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  remark
        @Column(columnDefinition = "varchar(500)   comment '비고'  ")
        private String remark;

        // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  created_by
        @ManyToOne
        @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
        private Member createdBy;

        // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', com_code, 'COLUMN',  modified_by
        @ManyToOne
        @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
        private Member modifiedBy;

        public void updateCommonCode(CommonCodeWriteDTO dto, Member modifiedBy, CodeGroup codeGroup){
                this.codeGroup = codeGroup;
                isActive = dto.getActive();
                iLevel = dto.getLevel();
                orderBy = dto.getOrderBy();

                code = dto.getCode();
                codeName = dto.getCodeName();
                aliasName = dto.getAliasName();
                extraValue1 = dto.getExtraValue1();
                retValue1 = dto.getRetValue1();
                remark = dto.getRemark();
                this.modifiedBy = modifiedBy;
        }

}
