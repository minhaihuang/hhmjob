package com.hhm.job.admin.util;

import com.alibaba.fastjson.JSON;
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
import java.util.Map;
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
     * 同步请求url
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, null, null);
    }

    /**
     * 同步请求url
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static <T> T get(String url, Class<T> tClass) throws IOException {
        return JSON.parseObject(get(url, null, null), tClass);
    }

    /**
     * 同步请求URL
     *
     * @param url
     * @param params 携带参数
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("get请求url为空");
        }
        final Request request = createGetRequest(url, params, headers);

        Response response = httpClient.newCall(request).execute();
        return response.body() == null ? null : response.body().string();
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

    /**
     * 构造Get请求Request
     *
     * @param url
     * @param params
     * @return
     */
    private static Request createGetRequest(String url, Map<String, String> params, Map<String, String> headers) {

        Request.Builder requestBuilder = new Request.Builder();

        if (params != null && params.size() > 0) {
            url = appendUrlParams(url, params);
        }

        if (headers != null && headers.size() > 0) {
            headers.forEach(requestBuilder::addHeader);
        }

        return requestBuilder.get().url(url).build();
    }

    /**
     * 构造Url携带参数
     *
     * @param url
     * @param params
     * @return
     */
    private static String appendUrlParams(String url, Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return url;
        }
        String urlParams = getParamString(params);
        if (url.contains("?")) {
            url = url + "&" + urlParams;
        } else {
            url = url + "?" + urlParams;
        }
        return url;
    }

    /**
     * 将参数转为路径字符串
     *
     * @param paramMap 参数
     * @return
     */
    public static String getParamString(Map<String, String> paramMap) {
        if (null == paramMap || paramMap.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            builder.append("&")
                    .append(key).append("=").append(paramMap.get(key));
        }
        return builder.deleteCharAt(0).toString();
    }
}
