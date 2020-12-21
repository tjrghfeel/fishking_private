package com.tobe.fishking.v2.enums.converter;


import com.tobe.fishking.v2.enums.IEnumModel;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumValueConvertUtils
{
    public static <T extends Enum<T> & IEnumModel> T ofDBCode(Class<T> enumClass, String dbCode)
    {
        if(StringUtils.isBlank(dbCode))
            return null;

        return EnumSet.allOf(enumClass).stream()
                .filter(v -> v.getValue().equals(dbCode))
                .findAny()
                .orElseThrow(() -> new RuntimeException("파라미터를 파싱할 수 없습니다."));
    }

    public static <T extends Enum<T> & IEnumModel> String toDBcode(T enumValue)
    {
        if(enumValue == null)
            return "";

        return enumValue.getValue();
    }
}