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
    @Size(max = 100, message = "상품명은 100자 이하이어야합니다")
    @Pattern(regexp = Constants.STRING, message = "상품명은 한글,숫자,영문자로 구성되어야합니다")
    private String goodsName;//
    @Size(max = 100, message = "회원명은 100자 이하이어야합니다")
    @Pattern(regexp = Constants.STRING, message = "회원명은 한글,숫자,영문자로 구성되어야합니다")
    private String memberName;//
    @Size(max = 100, message = "회원 휴대폰번호 앞자리는 100자 이하이어야합니다")
    @Pattern(regexp = Constants.NUMBER, message = "회원 휴대폰번호는 앞자리 숫자로 구성되어야합니다")
    private String memberAreaCode;//
    @Size(max = 100, message = "회원 휴대폰번호 뒷자리는 100자 이하이어야합니다")
    @Pattern(regexp = Constants.NUMBER, message = "회원 휴대폰번호 뒷자리는 숫자로 구성되어야합니다")
    private String memberLocalNumber;//
    @Size(max = 100, message = "선박명은 100자 이하이어야합니다")
    @Pattern(regexp = Constants.STRING, message = "선박명은 한글,숫자,영문자로 구성되어야합니다")
    private String shipName;//

    private Integer pageCount = 10;
}
