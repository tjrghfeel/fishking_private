package com.tobe.fishking.v2.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class AuthUtils {

    private static UserDetails getAuth()
    {
        //Auth user = (Auth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return null;
    }

    public static String getUserId()
    {
        return getAuth().getUsername();
    }
}
