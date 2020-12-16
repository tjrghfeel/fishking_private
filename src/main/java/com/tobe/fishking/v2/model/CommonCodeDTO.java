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
public class CommonCodeDTO {
    private @Valid Long id;
    private @Valid CodeGroup codeGroup;
    private @Valid String code;
    private @Valid String codeName;
    private @Valid String extraValue1;
    private @Valid String remark;

    public CommonCode toEntity() {
        return CommonCode.builder()
                .codeGroup(codeGroup)
                .code(code)
                .codeName(codeName)
                .extraValue1(extraValue1)
                .remark(remark)
                .build();
    }
}