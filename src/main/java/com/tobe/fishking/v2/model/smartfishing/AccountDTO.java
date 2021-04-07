package com.tobe.fishking.v2.model.smartfishing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Setter
@Getter
@NoArgsConstructor
public class AccountDTO {

    private @Valid String bankCode;
    private @Valid String accountNum;
    private @Valid String name;

}
