package com.tobe.fishking.v2.entity.common;


import com.tobe.fishking.v2.entity.common.PhoneNumberInfo;
import lombok.Getter;

import javax.persistence.*;

@Embeddable
@Getter
public class PhoneNumber {
    @Column(name = "AREACODE")
    private String areaCode;

    @Column(name = "LOCALNUMBER")
    private String localNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PHONE_INFO_ID")
    private PhoneNumberInfo phoneNumberInfo;

    public PhoneNumber() {
    }

    public PhoneNumber(String areaCode, String localNumber, PhoneNumberInfo phoneNumberInfo) {
        super();
        this.areaCode = areaCode;
        this.localNumber = localNumber;
        this.phoneNumberInfo = phoneNumberInfo;
    }
}
