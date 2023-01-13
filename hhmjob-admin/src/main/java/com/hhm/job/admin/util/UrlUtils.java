package com.hhm.job.admin.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author wenxy
 * 功能：Url 处理工具
 * 日期：2020/10/24-16:35
 * 版本       开发者     描述
 * 1.0.0     wenxy     ...
 */
@Slf4j
public class UrlUtils {

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

    /**
     * URLEncoder utf-8
     *
     * @param url
     * @return
     */
    public static String urlEncoder(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("urlEncoder不支持的encoding");
        }
        return null;
    }

    /**
     * URLDecoder utf-8
     *
     * @param s
     * @return
     */
    public static String urlDecoder(String s) {
        try {
            return URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("urlDecoder不支持的encoding");
        }
        return null;
    }

}
