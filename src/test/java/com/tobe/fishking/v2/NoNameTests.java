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
//        String pw = "solution1204";
        String pw = "58326a9bf8afc4091405213a2efe355351e3a88c52a624def8a64f1ca7cad875";
        String token = "UHFZa3BSWWR0SlZsMFBwZ3cxdmk5KzdPTFpXMGVNTVVkWU1mb083WUVFTHRBSDd5Z3NnelNEOFRNaGUxSm5NK2N6Q0VmV1RnUktVPQ0K";
        String camId = "86322";

        //when
//        Map<String, Object> result = httpRequestService.loginADT(id, pw);
        token = httpRequestService.loginADT(id, pw, "13");
        List<Map<String, Object>> result = httpRequestService.getADTList(token);
//        String result = httpRequestService.getADTCameraLive(camId, token);
        Map<String, Object> r = result.get(0);
        System.out.println(((Double) r.get("camId")).intValue());
        System.out.println((String) r.get("camName"));
        System.out.println((String) r.get("imageUri"));
        //then
        assertThat(result.size(), is(5));
//        assertThat(result.length(), is(5));
    }

}
