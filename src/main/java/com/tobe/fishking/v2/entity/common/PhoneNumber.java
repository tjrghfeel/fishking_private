package com.tobe.fishking.v2.entity.common;


import com.tobe.fishking.v2.entity.common.PhoneNumberInfo;
//import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.StringConverter;
import lombok.Getter;

import javax.persistence.*;

@Embeddable
@Getter
public class PhoneNumber {
    @Convert(converter = StringConverter.class)
    @Column(name = "AREACODE")
    private String areaCode;

    @Convert(converter = StringConverter.class)
    @Column(name = "LOCALNUMBER")
    private String localNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PHONE_INFO_ID")
    private PhoneNumberInfo phoneNumberInfo;

    public PhoneNumber() {
    }

    public PhoneNumber(String areaCode, String localNumber) {
        super();
        this.areaCode = areaCode;
        this.localNumber = localNumber;
    }

    public PhoneNumber(String areaCode, String localNumber, PhoneNumberInfo phoneNumberInfo) {
        super();
        this.areaCode = areaCode;
        this.localNumber = localNumber;
        this.phoneNumberInfo = phoneNumberInfo;
    }
}
