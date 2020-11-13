package com.tobe.fishking.v2.entity.common;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Getter
public class Address {
    @Column(name = "CITY")
    private String city;

    @Column(name = "GU")
    private String gu;

    @Column(name = "DONG")
    private String dong;

    public Address() {
    }

    public Address(String city, String gu, String dong) {
        this.city = city;
        this.gu = gu;
        this.dong = dong;
    }
}
