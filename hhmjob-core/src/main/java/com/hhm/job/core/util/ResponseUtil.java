package com.hhm.job.core.util;

import com.hhm.job.core.dto.Response;

/**
 * @Author huanghm
 * @Date 2022/5/24
 */
public class ResponseUtil {

    public static <T> Response<T> success(){
        Response<T>  response = new Response<>();
        response.setCode(20000);
        return response;
    }

    public static <T> Response<T> success(T t){
        Response<T>  response = new Response<>();
        response.setCode(20000);
        response.setData(t);
        return response;
    }

    public static Response<String> fail(String message){
        Response<String>   response = new Response<>();
        response.setCode(-1);
        response.setMessage(message);
        return response;
    }
}
