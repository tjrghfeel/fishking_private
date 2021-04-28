package com.tobe.fishking.v2.model.admin;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsSearchConditionDto {
    private Long goodsId;
    
//    @Size(max=100, message = "상품명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "상품명은 한글,숫자,영문자로 구성되어야합니다")
    private String goodsName;
    
    private Integer priceStart;
    
    private Integer priceEnd;
    
//    @Size(max=100, message = "선박명은 100자 이하이어야합니다")
//    @Pattern(regexp = Constants.STRING, message = "선박명은 한글,숫자,영문자로 구성되어야합니다")
    private String shipName;

    //날짜 조건
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fishingDateStart;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fishingDateEnd;

    private String[] speciesList = new String[0];

    private Integer pageCount =10;
}
