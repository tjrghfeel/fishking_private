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
public class OrderSearchConditionDto {
    private Long orderId;
    private Long orderDetailId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate orderDateStart;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate orderDateEnd;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fishingDateStart;//
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fishingDateEnd;//
    private Integer totalAmountStart;
    private Integer totalAmountEnd;
    private Integer discountAmountStart;
    private Integer discountAmountEnd;
    private Integer paymentAmountStart;
    private Integer paymentAmountEnd;
    private Boolean isPay;//
    private String payMethod;//
    private String orderStatus;//
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String goodsName;//
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String memberName;//
    @Size(max = 100)
    @Pattern(regexp = Constants.NUMBER)
    private String memberAreaCode;//
    @Size(max = 100)
    @Pattern(regexp = Constants.NUMBER)
    private String memberLocalNumber;//
    @Size(max = 100)
    @Pattern(regexp = Constants.STRING)
    private String shipName;//

    private Integer pageCount = 10;
}
