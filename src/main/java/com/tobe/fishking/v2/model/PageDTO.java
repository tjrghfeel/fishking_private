package com.tobe.fishking.v2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class PageDTO {

    private @Valid Integer pageNumber;
    private @Valid Integer size;
    private @Valid String searchText;

}
