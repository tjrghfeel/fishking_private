package com.tobe.fishking.v2.model;


import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class CodeGroupDTO {

    private @Valid String code;
    private @Valid String name;
    private @Valid String description;
    public CodeGroup toEntity() {
        return CodeGroup.builder()
                .code(code)
                .name(name)
                .description(description)
                .build();
    }
}