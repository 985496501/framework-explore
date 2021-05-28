package org.example.mvc.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: jinyun
 * @date: 2021/5/20
 */
@Data
public class RequestData implements Serializable {
    private String key;

    private String val;
}
