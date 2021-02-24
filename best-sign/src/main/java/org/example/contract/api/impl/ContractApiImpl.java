package org.example.contract.api.impl;

import org.example.contract.api.ContractApi;
import org.example.contract.client.HttpClient;
import org.example.contract.constant.MethodEnum;
import org.example.contract.pojo.*;

/**
 * log aop
 * 合同相关所有的api
 */
public class ContractApiImpl implements ContractApi {
    private final HttpClient httpClient;

    public ContractApiImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String register(Register pr) {
        return httpClient.post(MethodEnum.REGISTER, pr, RegisterResult.class).getTaskId();
    }

    @Override
    public String enterpriseRegister(Register pr) {
        return httpClient.post(MethodEnum.REGISTER, pr, RegisterResult.class).getTaskId();
    }

    @Override
    public boolean queryRegister(QueryRegister qr) {
        QueryRegister.QueryRegisterResult result = httpClient.post(MethodEnum.QUERY_REGISTER, qr, QueryRegister.QueryRegisterResult.class);
        return QueryRegister.RegisterEnum.success(result.getStatus());
    }

    @Override
    public void createPersonalSignature(CreateSignature cs) {
        httpClient.post(MethodEnum.CREATE_PERSONAL_SIGNATURE, cs, String.class);
    }

    @Override
    public void createEnterpriseSignature(CreateSignature cs) {
        httpClient.post(MethodEnum.CREATE_ENTERPRISE_SIGNATURE, cs, String.class);
    }

    @Override
    public String uploadAndCreateContract(UploadContract uc) {
        return httpClient.post(MethodEnum.UPLOAD_AND_CREATE_CONTRACT, uc, String.class);
    }

    @Override
    public void addSigner(AddSigner as) {
        httpClient.post(MethodEnum.ADD_SINGER, as, String.class);
    }

    @Override
    public void addSigners(AddSigners ass) {
        httpClient.post(MethodEnum.BATCH_ADD_SINGERS, ass, String.class);
    }

    @Override
    public void sendSms(SendSms ss) {
        httpClient.post(MethodEnum.SEND_SMS, ss, String.class);
    }

    @Override
    public void autoSignWithVerification(AutoSignWithVerification aswv) {
        httpClient.post(MethodEnum.AUTO_SIGN_WITH_VERIFICATION, aswv, String.class);
    }

    @Override
    public void lockContract(ContractId contractId) {
        httpClient.post(MethodEnum.LOCK_CONTRACT, contractId, String.class);
    }

    @Override
    public String previewContract(PreviewContract pc) {
        return httpClient.post(MethodEnum.PREVIEW_CONTRACT, pc, String.class);
    }
}
