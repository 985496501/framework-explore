package org.example.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    protected String account;
    protected String name;
    protected String userType;
    protected String applyCert;
}
