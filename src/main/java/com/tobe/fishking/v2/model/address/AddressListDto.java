package com.tobe.fishking.v2.model.address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddressListDto {

    private List<ZipCodeDto> sidoList;
    private List<ZipCodeDto> sigunguList;
    private List<ZipCodeDto> dongList;
}