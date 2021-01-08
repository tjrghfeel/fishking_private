package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.enums.auth.Gender;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
public class NoNameDTO {

    private @Valid Long Id;
    private @Valid String memberName;
    private @Valid Gender gender;


}
