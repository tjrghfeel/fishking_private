package com.tobe.fishking.v2.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.RideShip;
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
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NaksihaeService {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String BASE_URL = "https://ext.fips.go.kr/ffb/api/";
    private final String ID = "devtobe";
    private final String SECRET_KEY = "41a28f7890b44c9492d1fcf4e15f04e2";

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

    public Map<String, Object> reportRegistration(Goods goods, List<RideShip> riders, String token) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "tkoff/reg.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("X-IBM-Client-id", ID);
        httpPost.addHeader("X-IBM-Client-Secret", SECRET_KEY);
        httpPost.addHeader("Authorization", token);

        Ship ship = goods.getShip();
        String harborCode = getHarborCode(ship.getHarborName(), ship.getHarborDong(), token);

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

        JsonObject capInfo = new JsonObject();
        capInfo.addProperty("embkrNm", ship.getCapName());
        capInfo.addProperty("birthDe", ship.getCapBirth().replaceAll("-", ""));
        capInfo.addProperty("sexdstnCd", ship.getCapSex().equals("M") ? "0" : "1");
        capInfo.addProperty("mobilePhone", ship.getCapPhone());
        capInfo.addProperty("rnadres", ship.getCapAddr());
        capInfo.addProperty("emgncTelno", ship.getCapEmerNum());
        capInfo.addProperty("embkrSeCd", "1");
        capInfo.addProperty("indvdlinfoPrcuseAgreCd", "Y");
        capInfo.addProperty("thptyIndvdlinfoAgreCd", "Y");
        embarkList.add(new Gson().toJsonTree(capInfo));

        riders.forEach(rider -> {
            JsonObject riderInfo = new JsonObject();
            riderInfo.addProperty("embkrNm", rider.getName());
            riderInfo.addProperty("birthDe", rider.getBirthday().replaceAll("-", ""));
            riderInfo.addProperty("sexdstnCd", rider.getSex().equals("M") ? "0" : "1");
            riderInfo.addProperty("mobilePhone", rider.getPhoneNumber());
            riderInfo.addProperty("rnadres", rider.getResidenceAddr());
            riderInfo.addProperty("emgncTelno", rider.getEmergencyPhone());
            riderInfo.addProperty("embkrSeCd", "0");
            riderInfo.addProperty("indvdlinfoPrcuseAgreCd", "Y");
            riderInfo.addProperty("thptyIndvdlinfoAgreCd", "Y");
            embarkList.add(new Gson().toJsonTree(riderInfo));
        });

        data.add("tkoffSttemntInfo", new Gson().toJsonTree(shipInfo));
        data.add("embkrList", new Gson().toJsonTree(embarkList));

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_JSON));

        Map<String, Object> result = new HashMap<>();
        return result;
    }

    private String getHarborCode(
            String harborName,
            String harborDong,
            String token
    ) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "batch/clppList.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("X-IBM-Client-id", ID);
        httpPost.addHeader("X-IBM-Client-Secret", SECRET_KEY);
        httpPost.addHeader("Authorization", token);

        JsonObject data = new JsonObject();

        JsonObject harborInfo = new JsonObject();
        harborInfo.addProperty("clppNm", harborName);
        harborInfo.addProperty("legaldongNm", harborDong);

        JsonArray embarkList = new JsonArray();

        data.add("ffbClppInfo", new Gson().toJsonTree(harborInfo));

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_JSON));

        String code = "";
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            String json = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);
            code = res.getAsJsonObject("resultDomain")
                    .getAsJsonObject("clppList")
                    .get("clppCd").toString();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
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
