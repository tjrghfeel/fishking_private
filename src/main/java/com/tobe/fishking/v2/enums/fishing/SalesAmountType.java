package com.tobe.fishking.v2.enums.fishing;


import com.tobe.fishking.v2.enums.IEnumModel;

import java.util.function.Function;

public enum SalesAmountType  implements IEnumModel {
    ORIGIN_AMOUNT("원금액", amount -> amount),
    SUPPLY_AMOUNT("공급가액", amount -> Math.round(amount.doubleValue() * 10 / 11)),
    VAT_AMOUNT("부가세", amount -> Math.round(amount.doubleValue() / 11)),
    NOT_USED("사용안함", amount -> 0L);

    private String value;
    private Function<Long, Long> expression;

    SalesAmountType(String value, Function<Long, Long> expression) {
        this.value = value;
        this.expression = expression;
    }

    public long calculate(long amount){
        return expression.apply(amount);
    }


    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

}