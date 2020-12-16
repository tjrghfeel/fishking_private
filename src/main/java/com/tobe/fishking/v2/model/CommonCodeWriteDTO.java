package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonCodeWriteDTO {
    //not null 필드
    private Long id;
    private Double retValue1;
    //not null & default 필드.
    private Boolean active;
    private Integer level;
    private Integer orderBy;
    //nullable 필드
    private String code;
    private String codeName;
    private String aliasName;
    private String extraValue1;
    private String remark;
    private Long codeGroup;

}
