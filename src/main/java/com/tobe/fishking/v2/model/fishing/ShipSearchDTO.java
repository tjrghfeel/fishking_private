package com.tobe.fishking.v2.model.fishing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ShipSearchDTO {

    private @Valid String orderBy;
    private @Valid Integer pageNumber;
    private @Valid Integer size;

}
