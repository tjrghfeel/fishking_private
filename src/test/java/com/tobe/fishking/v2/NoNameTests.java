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

import static org.hamcrest.Matchers.*;
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
//        String id = "skb_test03";
        String id = "marine02";
//        String pw = "58326a9bf8afc4091405213a2efe355351e3a88c52a624def8a64f1ca7cad875";
        String pw = "155b89b0f83e5aca060ae2d2795a3fe711b874306a2ea6edccc89d944f3f5d58";
        String token;
        String camId = "243757";

        //when
//        String t = "https://www.fishkingapp.com/resource/ship/seok%20ho_202103291813467.jpg";
//        String sp = t.split("resource")[1];
//        System.out.println(sp);
        token = httpRequestService.loginADT(id, pw, "13");
//        List<Map<String, Object>> result2 = httpRequestService.getADTList(token);
//        Map<String, Object> result = httpRequestService.getADTCameraDetail(camId, token);
        String result3 = httpRequestService.getADTCameraLive(camId, token);
//        System.out.println(result);
//        Map<String, Object> r = result2.get(0);
//        System.out.println(((Double) r.get("camId")).intValue());
//        System.out.println((String) r.get("camName"));
//        System.out.println((String) r.get("imageUri"));
        System.out.println(result3);
        //then
//        assertThat(sp, startsWith("/ship"));
//        assertThat(result.size(), is(5));
//        assertThat(result.length(), is(5));
        assertThat(result3, startsWith("rtsp"));
//        assertThat(r.size(), is(5));
    }

}
