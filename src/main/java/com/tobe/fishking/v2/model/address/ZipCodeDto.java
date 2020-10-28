package com.tobe.fishking.v2.model.address;


import com.tobe.fishking.v2.entity.common.ZipCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ZipCodeDto {

    private String sido;
    private String sigungu;
    private String dong;

    private String searchValue;

    public ZipCode toEntity(ZipCode zipCode) {
        return zipCode;
    }
}