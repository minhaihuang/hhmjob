package com.hhm.job.admin.dto;

import lombok.Data;

/**
 * @Author huanghm
 * @Date 2022/5/24
 */
@Data
public class Response<T> {
    private int code = 20000;
    private T data;
    private String message;
}
