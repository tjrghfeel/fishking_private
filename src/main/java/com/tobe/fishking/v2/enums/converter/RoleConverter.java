package com.tobe.fishking.v2.enums.converter;

import com.tobe.fishking.v2.enums.auth.Role;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter extends AbstractEnumAttributeConverter<Role> {
    public static final String ENUM_NAME = "권한";

    public RoleConverter() {
        super(Role.class, true, ENUM_NAME);
    }
}