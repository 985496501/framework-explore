package org.example.loadbalanced;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jinyun
 * @date: 2021/2/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    private String ip;
    private String port;
    private String name;
}
