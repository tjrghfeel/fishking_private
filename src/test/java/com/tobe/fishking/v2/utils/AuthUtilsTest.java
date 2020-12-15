package com.tobe.fishking.v2.utils;

import com.tobe.fishking.v2.IntegrationTest;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.service.auth.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthUtilsTest extends IntegrationTest {

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    /*
    @Test
    @Ignore
    void getUserName() {

        Auth auth = (Auth) authService.loadUserByUsername("bbb234@naver.com");
        String id = String.valueOf(auth.getId());

        String authId = AuthUtils.getUserId();

        assertEquals(id, authId);
    }

     */

    @Test
    void test() {

        Member auth = memberRepository.findByEmail("bbb234@naver.com");
        String id = String.valueOf(auth.getId());

        String authId = AuthUtils.getUserId();

        assertEquals(id, authId);
    }
}