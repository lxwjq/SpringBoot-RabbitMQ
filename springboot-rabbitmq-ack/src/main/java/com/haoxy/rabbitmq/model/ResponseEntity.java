package com.haoxy.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 返回参数封装实体
 * @Date: 2019/4/29 16:35
 * @Version: V1.0
 */
@Data
@ToString
@AllArgsConstructor
public class ResponseEntity implements Serializable {

    private static final long serialVersionUID = -7715678696640699601L;

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 提示消息
     */
    private String msg;


    private Object object;

    public static ResponseEntity ok() {
        return new ResponseEntity(200, "success", "");
    }

}
