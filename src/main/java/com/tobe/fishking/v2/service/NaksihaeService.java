package com.tobe.fishking.v2.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.tobe.fishking.v2.entity.common.HarborCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.repository.common.HarborCodeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NaksihaeService {

    private final HarborCodeRepository harborCodeRepository;
    private final String USER_AGENT = "Mozilla/5.0";
    private final String BASE_URL = "https://ext.fips.go.kr/ffb/api/";
    private final String ID = "devtobe";
    private final String SECRET_KEY = "41a28f7890b44c9492d1fcf4e15f04e2";

    public Map<String, Object> getToken() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, UnsupportedEncodingException {
        String uri = BASE_URL + "auth/token.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("userId", (ID)));
        data.add(new BasicNameValuePair("scrtky", (SECRET_KEY)));
        data.add(new BasicNameValuePair("apiReqstTy", ("10")));

        httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));

        Map<String, Object> result = new HashMap<>();
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);
            String token = res.getAsJsonObject("resultDomain")
                    .get("tkn").toString();
            String expires = res.getAsJsonObject("resultDomain")
                    .get("expiresIn").toString();
            String created = res.getAsJsonObject("resultDomain")
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

    @Transactional(readOnly = true)
    public String reportRegistration(Goods goods, List<RideShip> riders, String token) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "tkoff/reg.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("X-IBM-Client-id", ID);
        httpPost.addHeader("X-IBM-Client-Secret", SECRET_KEY);
        httpPost.addHeader("Authorization", token);

        Ship ship = goods.getShip();
        List<HarborCode> harborCode = harborCodeRepository.findAllByNameAndDong(ship.getHarborName(), ship.getHarborDong());
        String code = harborCode.get(0).getCode();

        JsonObject data = new JsonObject();

        LocalDate date = LocalDate.now();
        if (goods.getFishingEndDate().equals("익일")) {
            date = date.plusDays(1L);
        }
        String start = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + goods.getFishingStartTime() + "00";
        String end = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + goods.getFishingEndTime() + "00";
        JsonObject shipInfo = new JsonObject();
        shipInfo.addProperty("fshhpSn", ship.getShipNumber());
        shipInfo.addProperty("fshhpNm", ship.getShipName());
        shipInfo.addProperty("fshhpSttusCd", "1");
        shipInfo.addProperty("tkoffDt", start);
        shipInfo.addProperty("clppCd", code);
        shipInfo.addProperty("etryptPrearngeDt", end);
        shipInfo.addProperty("etryptPrearngePrtCd", code);
        shipInfo.addProperty("nowEmbrkNmpr", riders.size()+1);

        JsonArray embarkList = new JsonArray();

        JsonObject capInfo = new JsonObject();
        capInfo.addProperty("embkrNm", ship.getCapName());
        capInfo.addProperty("birthDe", ship.getCapBirth().replaceAll("-", ""));
        capInfo.addProperty("sexdstnCd", ship.getCapSex().equals("M") ? "0" : "1");
        capInfo.addProperty("mobilePhone", ship.getCapPhone());
        capInfo.addProperty("rnadres", ship.getCapAddr());
        capInfo.addProperty("emgncTelno", ship.getCapEmerNum());
        capInfo.addProperty("embkrSeCd", "1");
        capInfo.addProperty("mstrIhidnum", ship.getCapBirth().replaceAll("-", "").substring(2)+"1234567");
        capInfo.addProperty("mrntecnLcnsSn", ship.getCapNumber());
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

        String result = "";
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);
//            System.out.println(res);
            String resultCode = res.get("resultCode").getAsString();
            String reportSerial = res.getAsJsonObject("resultDomain")
                    .getAsJsonObject("tkoffSttemntInfo")
                    .get("dclrtSn").getAsString();
            if (resultCode.equals("S001")) {
                result = reportSerial;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getHarborCode(
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

        data.add("ffbClppInfo", new Gson().toJsonTree(harborInfo));

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_JSON));

        String code = "";
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            String json = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);
            JsonArray harbors = res.getAsJsonObject("resultDomain")
                    .getAsJsonArray("clppList");
            System.out.println(harbors.size());

            harbors.forEach(harbor -> {
                System.out.println(harbor);
                String code1 = ((JsonObject) harbor).get("clppCd").getAsString();
                String name = ((JsonObject) harbor).get("clppNm").getAsString();
                String dongCode = ((JsonObject) harbor).get("legaldongCd") == JsonNull.INSTANCE ? "" : ((JsonObject) harbor).get("legaldongCd").getAsString();
                String dong = ((JsonObject) harbor).get("legaldongNm") == JsonNull.INSTANCE ? "" : ((JsonObject) harbor).get("legaldongNm").getAsString();
                harborCodeRepository.save(
                        HarborCode.builder()
                                .name(name)
                                .code(code1)
                                .dong(dong)
                                .dongCode(dongCode)
                                .build()
                );
            });
//            code = ((JsonObject) res.getAsJsonObject("resultDomain")
//                    .getAsJsonArray("clppList")
//                    .get(0))
//                    .get("clppCd").getAsString();
//            httpClient.close();
//            code = code.replaceAll("\"", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    /*
        0.사전등록,1.신고제출, 2.신고확인, 3.입항, 4.출항취소, 5.운항중
     */
    public String checkReportStatus(String serial, Goods goods, String token) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "tkoff/status.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("X-IBM-Client-id", ID);
        httpPost.addHeader("X-IBM-Client-Secret", SECRET_KEY);
        httpPost.addHeader("Authorization", token);

        Ship ship = goods.getShip();

        JsonObject data = new JsonObject();

        JsonObject harborInfo = new JsonObject();
        harborInfo.addProperty("dclrtSn", serial);
        harborInfo.addProperty("fshhpSn", ship.getShipNumber());
        harborInfo.addProperty("mrntecnLcnsSn", ship.getCapNumber());

        data.add("tkoffSttemntInfo", new Gson().toJsonTree(harborInfo));

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_JSON));

        String status = "";
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            String json = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);

            status = res.getAsJsonObject("resultDomain")
                    .getAsJsonObject("tkoffSttemntInfo")
                    .get("fshhpSttusCd").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    /*
        0.사전등록,1.신고제출, 2.신고확인, 3.입항, 4.출항취소, 5.운항중
     */
    @Transactional
    public boolean updateReportStatus(String serial, Goods goods, String status, String token) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "tkoff/mod.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("X-IBM-Client-id", ID);
        httpPost.addHeader("X-IBM-Client-Secret", SECRET_KEY);
        httpPost.addHeader("Authorization", token);
        Ship ship = goods.getShip();

        String statusCode = "";
        switch (status) {
            case "출항":
                statusCode = "5";
                break;
            case "입항":
                statusCode = "3";
                break;
            case "취소":
                statusCode = "4";
                break;
        }

        if (statusCode.equals("")) {
            return false;
        }

        JsonObject data = new JsonObject();

        JsonObject harborInfo = new JsonObject();
        harborInfo.addProperty("dclrtSn", serial);
        harborInfo.addProperty("fshhpSn", ship.getShipNumber());
        harborInfo.addProperty("mrntecnLcnsSn", ship.getCapNumber());
        harborInfo.addProperty("fshhpSttusCd", status);

        if (statusCode.equals("3")) {
            List<HarborCode> harborCode = harborCodeRepository.findAllByNameAndDong(ship.getHarborName(), ship.getHarborDong());
            String code = harborCode.get(0).getCode();

            LocalDate date = LocalDate.now();
            if (goods.getFishingEndDate().equals("익일")) {
                date = date.plusDays(1L);
            }
            String end = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + goods.getFishingEndTime() + "00";

            harborInfo.addProperty("etryptDt", end);
            harborInfo.addProperty("etryptPrtCd", code);
        }

        data.add("tkoffSttemntInfo", new Gson().toJsonTree(harborInfo));

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_JSON));

        String result = "";
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            String json = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            JsonObject res = gson.fromJson(json, JsonObject.class);

            result = res.getAsJsonObject("resultCode").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.equals("S001");
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
