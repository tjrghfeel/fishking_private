package com.tobe.fishking.v2.model.fishing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private String goodsName;
    private Integer amount;
    private String orderName;
    private String email;
    private String phoneNumber;
    private String showCard;
    private String installMentType;
    private String interestType;
    private String reply;
    private String shopNumber;
    private String payMethod;

    public OrderResponse (Long id,
                          String orderNumber,
                          String goodsName,
                          Integer amount,
                          String orderName,
                          String email,
                          String phoneNumber,
                          String payMethod) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.goodsName = goodsName;
        this.amount = amount;
        this.orderName = orderName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.orderNumber = orderNumber;
        this.showCard = "C";
        this.installMentType = "ALL(0)";
        this.interestType = "NONE";
        this.reply = "/payresult";
        this.shopNumber = "2999199900";
        this.payMethod = payMethod;
    }

}
