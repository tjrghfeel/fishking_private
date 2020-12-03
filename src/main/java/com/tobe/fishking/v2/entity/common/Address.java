package com.tobe.fishking.v2.entity.common;

//import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.StringConverter;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;


@Embeddable
@Getter
public class Address {
    @Convert(converter = StringConverter.class)
    @Column(name = "CITY")
    private String city;

    @Convert(converter = StringConverter.class)
    @Column(name = "GU")
    private String gu;

    @Convert(converter = StringConverter.class)
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
