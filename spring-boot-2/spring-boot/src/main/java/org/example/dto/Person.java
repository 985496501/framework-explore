package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: jinyun
 * @date: 2021/4/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
    private String name;
    private Integer age;
    private String job;
}
