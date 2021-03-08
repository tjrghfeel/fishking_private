package com.tobe.fishking.v2.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    //동영상 카운트
    private int cnt = 1;
    private final String apiKey = "AIzaSyCCIfMaRDWX-7fGtQZHGvMi2E5fnR1eIHc";
    private final String channelId = "UCB643Ee7lPtNWJem92gb03g";
    private final String UPplaylistid ="UUB643Ee7lPtNWJem92gb03g";
    private String apiUrl = "https://www.googleapis.com/youtube/v3/playlistItems?key="+ apiKey
            + "&playlistId="+ UPplaylistid
            + "&part=snippet"
            + "&type=video"
            + "&fields=nextPageToken,pageInfo,items(id,snippet(resourceId(videoId)))"
            + "&order=date"
            + "&maxResults=10";
//            + "&type=video&fields=nextPageToken,pageInfo,items(id,snippet(publishedAt,title,description,thumbnails(high(url)),resourceId(videoId)))&order=date&maxResults=10";

    private String videoApiUrl = "https://www.googleapis.com/youtube/v3/videos?key="+ apiKey
            + "&part=snippet,statistics"
            + "&type=video"
            + "&fields=items(id,snippet(publishedAt,title,description,thumbnails(high(url))),statistics(viewCount))"
            + "&maxResults=10";

    private final String USER_AGENT = "Mozilla/5.0";
    private final String playUrl = "https://www.youtube.com/watch?v=";

    public Map<String, Object> getYoutube(String nextToken) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        //2번째 부터 다음 토큰값 있으면 파리미터 추가한다.
        if (!nextToken.equals("")) {
            apiUrl+= "&pageToken=" + nextToken ;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nextPageToken", null);

        CloseableHttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(apiUrl);
        httpGet.addHeader("User-Agent", USER_AGENT);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        String json = EntityUtils.toString(response.getEntity());

        Map<String, Object> res = (Map<String, Object>) new Gson().fromJson(json, HashMap.class);

        if (res.get("nextPageToken") != null) {
            result.put("nextPageToken", res.get("nextPageToken"));
        }

        String ids = ((List<Map<String, Object>>) res.get("items")).stream().map(
                i -> (String) ((Map<String, Object>) ((Map<String, Object>) i.get("snippet")).get("resourceId")).get("videoId")
        ).collect(Collectors.joining(","));
        videoApiUrl = videoApiUrl + "&id=" + ids;

        HttpGet videoGet = new HttpGet(videoApiUrl);
        videoGet.addHeader("User-Agent", USER_AGENT);
        CloseableHttpResponse videoResponse = httpClient.execute(videoGet);
        String videoJson = EntityUtils.toString(videoResponse.getEntity());
        List<Map<String, Object>> resv = (List<Map<String, Object>>) ((Map<String, Object>) new Gson().fromJson(videoJson, HashMap.class)).get("items");
        result.put("items", resv);
        result.put("playUrl", playUrl);

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
