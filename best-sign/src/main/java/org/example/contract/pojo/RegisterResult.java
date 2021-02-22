package org.example.contract.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResult {
    /**
     * 24h timeout
     */
    private String taskId;
}
