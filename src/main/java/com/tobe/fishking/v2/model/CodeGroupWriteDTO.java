package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeGroupWriteDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String remark;

}
