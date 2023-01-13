package com.hhm.job.admin.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class OkHttpUtil {
    private OkHttpUtil() {
    }

    /**
     * 默认json请求
     */
    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.parse("application/json;charset=utf-8");
    private static OkHttpClient httpClient;

    static {
        log.info("init OkHttpUtil success");
        OkHttpUtil.rebuildClient(5000, 5000, 10, 5);
    }

    // 暴露httpClient，可以直接使用，但你要知道你在干什么
    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    private static void rebuildClient(long connectionTimeout, long readTimeout, int maxIdleConnection, long keepAliveDuration) {
        log.info("OkHttpUtil rebuild, maxIdleConnections is {}, connectTimeout is {}, readTimeout is {}, keepAliveDuration is {}",
                maxIdleConnection, connectionTimeout, readTimeout, keepAliveDuration);
        httpClient = new OkHttpClient.Builder()
                // 单位毫秒
                .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                // 单位毫秒
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .sslSocketFactory(SSLSocketClientUtil.getSSLSocketFactory(), SSLSocketClientUtil.getX509TrustManager())
                .hostnameVerifier(SSLSocketClientUtil.getHostnameVerifier())
                // maxIdleConnections：每个地址最大值空闲连接数
                .connectionPool(new ConnectionPool(maxIdleConnection, keepAliveDuration, TimeUnit.MINUTES)).build();
    }

    /**
     * Post提交
     *
     * @param url
     * @param body
     * @throws IOException
     */
    public static String post(String url, String body) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("asyncPostBody请求url为空");
        }

        RequestBody requestBody = RequestBody.create(DEFAULT_MEDIA_TYPE, body);
        Request request = new Request.Builder()
                .url(url)
                .header("accept", "*/*")
                .header("Accept-Language", "zh-CN")
                .header("Proxy-Connection", "Keep-Alive")
                .header("user-agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)")
                .post(requestBody)
                .build();
        Call call = httpClient.newCall(request);

        Response response = call.execute();

        return response.body() == null ? null : response.body().string();
    }


    /**
     * 异步： Post提交告警
     *
     * @param url
     * @param body
     * @param mediaType
     * @param callback
     * @throws IOException
     */
    public static void asyncPost(String url, String body, MediaType mediaType, Callback callback) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("asyncPostBody请求url为空");
        }
        if (mediaType == null) {
            mediaType = DEFAULT_MEDIA_TYPE;
        }
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request request = new Request.Builder()
                .url(url)
                .header("accept", "*/*")
                .header("Accept-Language", "zh-CN")
                .header("Proxy-Connection", "Keep-Alive")
                .header("user-agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)")
                .post(requestBody)
                .build();
        //log.info("url = {}, body = {}", url, body);
        httpClient.newCall(request).enqueue(callback);
    }
}
