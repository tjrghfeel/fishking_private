package com.tobe.fishking.v2.model.page;

import com.tobe.fishking.v2.enums.SortOption;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageableDTO {
    private Integer page;
    private Integer size;
    private Integer totalElements;
    private List<Pair<String, SortOption>> sorts;
}