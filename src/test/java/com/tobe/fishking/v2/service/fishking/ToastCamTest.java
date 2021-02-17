package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.service.HttpRequestService;
import org.junit.Test;
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
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToastCamTest {

    @Autowired
    private HttpRequestService httpRequestService;

    @Test
    public void getTokenTest() {
//        List<Map<String, Object>> result = null;
//        try {
//            result = httpRequestService.getCameraList();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        assertThat(result, is((result != null ? result.size() : 0) > 0));
    }
}
