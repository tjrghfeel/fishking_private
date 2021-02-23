package com.tobe.fishking.v2.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HttpRequestService {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String CAM_BASE_URL = "https://openapi.toastcam.com/";
    private final String CAM_AUTH_URL = "auth/v1/";
    private final String CAM_SERVICE_URL = "service/v1/";
    private final String vendorId = "tobe";
    private final String vendorPw = "toastcam1!";

    public String getToken() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, UnsupportedEncodingException {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(CAM_BASE_URL + CAM_AUTH_URL + "token");
        httpPost.addHeader("User-Agent", USER_AGENT);

        JsonObject data = new JsonObject();
        data.addProperty("bizUserId", "dev@to-be.kr");
        data.addProperty("bizUserPassword", vendorPw);
        data.addProperty("vendorId", vendorId);
        data.addProperty("country", "KR");
        data.addProperty("domain", "toastcam");

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_JSON));

        String result;
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println("Response Status: " + response.getStatusLine().getStatusCode());

            String json = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);
            String token = res.getAsJsonObject("result").get("token").toString();

            httpClient.close();

            result = token;
        } catch (IOException e) {
            e.printStackTrace();
            result = "error";
        }
        return result;
    }

    public Map<String, String> refreshToken(String token) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CloseableHttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(CAM_BASE_URL + CAM_AUTH_URL + "token/refresh");
        httpGet.addHeader("x-access-token", token);

//        JsonObject result;
        String newToken;
        String expireTime;
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("Response Status: " + response.getStatusLine().getStatusCode());

            String json = EntityUtils.toString(response.getEntity());
            Map<String, Object> res = (Map<String, Object>) new Gson().fromJson(json, HashMap.class);

            httpClient.close();

            newToken = ((Map<String, Object>) res.get("result")).get("token").toString();
            expireTime = ((Map<String, Object>) res.get("result")).get("expirationTime").toString();
        } catch (IOException e) {
            e.printStackTrace();
            newToken = "";
            expireTime = "";
        }
        Map<String, String> result = new HashMap<>();
        result.put("token", newToken);
        result.put("expireTime", expireTime);
        return result;
    }

    public List<Map<String, Object>> getCameraList(String token) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(CAM_BASE_URL + CAM_SERVICE_URL + "cameras");
        httpGet.addHeader("x-access-token", token);

//        JsonObject result;
        List<Map<String, Object>> result;
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("Response Status: " + response.getStatusLine().getStatusCode());

            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
//            Gson gson = new Gson();
            Map<String, Object> res = (Map<String, Object>) new Gson().fromJson(json, HashMap.class);

            httpClient.close();

            result = (List<Map<String, Object>>) res.get("result");
        } catch (IOException e) {
            e.printStackTrace();
            result = new ArrayList<>();
        }
        return result;
    }

    public String getPlayUrl(String token, String serial) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String result = "";
        CloseableHttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(CAM_BASE_URL + CAM_SERVICE_URL + "cameras/" + serial + "/play");
        httpGet.addHeader("x-access-token", token);

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("Response Status: " + response.getStatusLine().getStatusCode());

            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
//            Gson gson = new Gson();
            Map<String, Object> res = (Map<String, Object>) new Gson().fromJson(json, HashMap.class);
//            JsonObject res = new JsonParser().parse(json).getAsJsonObject();

            httpClient.close();
            result = ((Map<String, Object>) res.get("result")).get("hlsStreamUrl").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private CloseableHttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        SSLContext sslContext = SSLContexts.custom()
                .setProtocol("SSL")
                .loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
                        return true;
                    }
                })
                .build();

        httpClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()).setSSLContext(sslContext);

        return httpClientBuilder.build();
    }
}
