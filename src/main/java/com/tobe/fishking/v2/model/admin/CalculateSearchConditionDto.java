package com.tobe.fishking.v2.model.admin;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculateSearchConditionDto {
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String companyName;
    @Size(max=100)
    @Pattern(regexp = Constants.STRING)
    private String shipName;
    @Size(max=100)
    @DateTimeFormat(pattern = "yyyy-MM")
    private String dateStart;
    @Size(max=100)
    @DateTimeFormat(pattern = "yyyy-MM")
    private String dateEnd;
    private Boolean isCalculated;
    @Max(value=Long.MAX_VALUE)
    private Long totalAmountStart;
    @Max(value=Long.MAX_VALUE)
    private Long totalAmountEnd;

    private Integer pageCount = 80;
//    private String sort="month";
}
