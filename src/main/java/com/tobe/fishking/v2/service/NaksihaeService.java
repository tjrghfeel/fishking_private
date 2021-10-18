package com.tobe.fishking.v2.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tobe.fishking.v2.entity.fishing.Ship;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NaksihaeService {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String BASE_URL = "https://ext.fips.go.kr/ffb/api/";
    private final String ID = "devtobe";
    private final String SECRET_KEY = "asfe";

    public Map<String, Object> getToken() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "auth/token.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);

        JsonObject data = new JsonObject();
        data.addProperty("userId", ID);
        data.addProperty("scrtky", SECRET_KEY);
        data.addProperty("apiReqstTy", "10");

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_FORM_URLENCODED));

        Map<String, Object> result = new HashMap<>();
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            String json = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);
            String token = res.getAsJsonObject("responseJson")
                    .getAsJsonObject("resultDomain")
                    .get("tkn").toString();
            String expires = res.getAsJsonObject("responseJson")
                    .getAsJsonObject("resultDomain")
                    .get("expiresIn").toString();
            String created = res.getAsJsonObject("responseJson")
                    .getAsJsonObject("resultDomain")
                    .get("tknCreatDt").toString();
            httpClient.close();
            LocalDateTime date = LocalDateTime.parse(created, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss."));
            date.plusSeconds(Long.parseLong(expires));

            result.put("token", token);
            result.put("expireDate", date);
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public Map<String, Object> reportRegistration(Ship ship) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "tkoff/reg.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);

        JsonObject data = new JsonObject();

        JsonObject shipInfo = new JsonObject();
        shipInfo.addProperty("fshhpSn", ship.getShipNumber());
        shipInfo.addProperty("fshhpNm", ship.getShipName());
        shipInfo.addProperty("fshhpSttusCd", "1");
        shipInfo.addProperty("tkoffDt", ship.getShipName());
        shipInfo.addProperty("clppCd", ship.getShipName());
        shipInfo.addProperty("etryptPrearngeDt", ship.getShipName());
        shipInfo.addProperty("etryptPrearngePrtCd", "1");
        shipInfo.addProperty("nowEmbrkNmpr", "4");

        JsonArray embarkList = new JsonArray();

        data.addProperty("userId", ID);
        data.addProperty("scrtky", SECRET_KEY);
        data.addProperty("apiReqstTy", "10");

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_JSON));

        Map<String, Object> result = new HashMap<>();
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
