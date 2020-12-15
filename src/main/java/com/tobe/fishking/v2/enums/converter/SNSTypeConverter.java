package com.tobe.fishking.v2.enums.converter;

import com.tobe.fishking.v2.enums.fishing.SNSType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class SNSTypeConverter extends AbstractEnumAttributeConverter<SNSType> {
    public static final String ENUM_NAME = "sns 타입";

    public SNSTypeConverter() {
        super(SNSType.class, true, ENUM_NAME);
    }
}