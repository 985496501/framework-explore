package cc.jinyun.contract.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MethodEnum {
    REGISTER("/user/reg/"),

    QUERY_REGISTER("/user/async/applyCert/status/"),

    CREATE_PERSONAL_SIGNATURE("/signatureImage/user/create/"),

    CREATE_ENTERPRISE_SIGNATURE("/dist/signatureImage/ent/create/"),

    UPLOAD_CONTRACT_TEMPLATE("/storage/upload/"),

    ADD_PDF_ELEMENTS("/storage/addPdfElements/"),

    CREATE_CONTRACT_PDF("/template/createContractPdf/"),

    CREATE_CONTRACT_BY_FILE("/contract/createByTemplate/"),

    AUTOMATICALLY_SIGN_BY_TEMPLATE_VALS("/contract/sign/template/"),

    MANUAL_SIGN_BY_TEMPLATE_VARS("/contract/sendByTemplate/"),

    CREATE_CONTRACT("/contract/create/"),

    UPLOAD_PDF_CREATE_TEMPLATE("/dist/template/upload/"),

    UPLOAD_AND_CREATE_CONTRACT("/storage/contract/upload/"),

    ADD_SINGER("/contract/addSigner/"),

    BATCH_ADD_SINGERS("/contract/addSigners/"),

    SEND_SMS("/contract/sendSignVCode/"),

    SEND_CONTRACT("/contract/send"),

    AUTO_SIGN_WITH_VERIFICATION("/storage/contract/sign/cert2/"),

    LOCK_CONTRACT("/storage/contract/lock/"),

    PREVIEW_CONTRACT("/contract/getPreviewURL/")
    ;

    private final String method;
}
