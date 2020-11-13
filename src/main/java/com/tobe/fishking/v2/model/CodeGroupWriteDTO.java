package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CodeGroupWriteDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String remark;
    private Long createdBy;
    private Long modifiedBy;

}
