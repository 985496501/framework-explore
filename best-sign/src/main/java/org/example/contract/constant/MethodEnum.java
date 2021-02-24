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

    UPLOAD_AND_CREATE_CONTRACT("/storage/contract/upload/"),

    ADD_SINGER("/contract/addSigner/"),

    BATCH_ADD_SINGERS("/contract/addSigners/"),

    SEND_SMS("/contract/sendSignVCode/"),

    AUTO_SIGN_WITH_VERIFICATION("/storage/contract/sign/cert2/"),

    LOCK_CONTRACT("/storage/contract/lock/"),

    PREVIEW_CONTRACT("/contract/getPreviewURL/")
    ;

    private final String method;
}
