package com.tobe.fishking.v2;

import com.tobe.fishking.v2.service.HttpRequestService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoNameTests {

    @Autowired
    private HttpRequestService httpRequestService;

    @Test
    public void querydslCustomTest() throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        //given
        Long memberId = 13L;
        String id = "skb_test03";
        String pw = "solution1204";
        String token = "UHFZa3BSWWR0SlZsMFBwZ3cxdmk5KzdPTFpXMGVNTVVkWU1mb083WUVFTHRBSDd5Z3NnelNEOFRNaGUxSm5NK2N6Q0VmV1RnUktVPQ0K";
        String camId = "86322";

        //when
//        Map<String, Object> result = httpRequestService.loginADT(id, pw);
//        List<Map<String, Object>> result = httpRequestService.getADTList(token);
        Map<String, Object> result = httpRequestService.getADTCameraLive(camId, token);
        System.out.println(result);
        //then
        assertThat(result.size(), is(5));
    }

}
