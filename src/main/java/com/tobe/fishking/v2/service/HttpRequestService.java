package com.tobe.fishking.v2.service;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpRequestService {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String CAM_BASE_URL = "https://dev-openapi.toastcam.com/";
    private final String CAM_AUTH_URL = "auth/v1/";
    private final String CAM_SERVICE_URL = "service/v1/";
    private final String vendorId = "tobe";
    private final String vendorPw = "toastcam1!";

//    public String getToken() {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(CAM_BASE_URL + CAM_AUTH_URL + "token");
//    }
}
