package com.tobe.fishking.v2.service;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@RequiredArgsConstructor
@Service
public class NaksihaeService {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String BASE_URL = "https://ext.fips.go.kr/ffb/api/";
    private final String ID = "devtobe";
    private final String SECRET_KEY = "asfe";

    public void getToken() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String uri = BASE_URL + "api/auth/token.do";
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", USER_AGENT);

        JsonObject data = new JsonObject();
        data.addProperty("userId", ID);
        data.addProperty("scrtky", SECRET_KEY);
        data.addProperty("apiReqstTy", "10");

        httpPost.setEntity(new StringEntity(data.toString(), ContentType.APPLICATION_FORM_URLENCODED));
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
