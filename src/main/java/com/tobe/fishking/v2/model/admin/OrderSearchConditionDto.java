package com.tobe.fishking.v2.model.admin;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String goodsName;//
    private String memberName;//
    private String memberAreaCode;//
    private String memberLocalNumber;//
    private String shipName;//

    private Integer pageCount = 10;
}
