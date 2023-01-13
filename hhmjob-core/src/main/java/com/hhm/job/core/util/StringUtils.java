package com.hhm.job.core.util;

/**
 * @Author huanghm
 * @Date 2023/1/4
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() <= 0;
    }
}
