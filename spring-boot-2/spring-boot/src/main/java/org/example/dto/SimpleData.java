package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: jinyun
 * @date: 2021/5/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleData implements Serializable {
    private String name;
}
