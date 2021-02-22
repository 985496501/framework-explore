package org.example.contract.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MethodEnum {
    REGISTER("/user/reg/"),

    QUERY_REGISTER("/user/async/applyCert/status/"),

    CREATE_PERSONAL_SIGNATURE("/signatureImage/user/create/"),

    CREATE_ENTERPRISE_SIGNATURE("/dist/signatureImage/ent/create/"),


    ;

    private final String method;
}
