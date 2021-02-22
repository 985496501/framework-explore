package org.example.contract.api.impl;

import org.example.contract.api.ContractApi;
import org.example.contract.client.HttpClient;
import org.example.contract.constant.MethodEnum;
import org.example.contract.pojo.CreateSignature;
import org.example.contract.pojo.QueryRegister;
import org.example.contract.pojo.Register;
import org.example.contract.pojo.RegisterResult;

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
}
