package org.example.contract.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MethodEnum {
    PERSONAL_REGISTER("/user/reg/"),

    ;

    private final String method;
}
