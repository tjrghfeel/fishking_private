package com.tobe.fishking.v2.entity.converter;

import com.tobe.fishking.v2.utils.SeedUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CryptoStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return SeedUtils.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return SeedUtils.decrypt(dbData);
    }

}
