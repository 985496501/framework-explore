package org.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: jinyun
 * @date: 2021/2/8
 */
@Data
public class Demo implements Serializable {
    private Integer id;
    private String a;
    private String b;
    private String c;
}
