package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.enums.auth.Gender;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoNameDTO {
    private Long id;
    private String stringList;

    private String contents;
    private Boolean parent;
    /*public NoNameDTO(Long id, String stringList, String contents){
        System.out.println("hello");
    }*/
}
