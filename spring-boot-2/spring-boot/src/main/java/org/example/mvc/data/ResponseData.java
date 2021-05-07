package org.example.mvc.data;

import lombok.Data;

/**
 * @author: jinyun
 * @date: 2021/5/7
 */
@Data
public class ResponseData<T> {
    private Integer code;
    private String message;

    private T data;
}
