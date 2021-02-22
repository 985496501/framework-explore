package org.example.contract.api.impl;

import org.example.contract.api.ContractApi;
import org.example.contract.client.HttpClient;
import org.example.contract.pojo.PersonalRegister;

/**
 * 合同相关所有的api
 */
public class ContractApiImpl implements ContractApi {
    private final HttpClient httpClient;

    public ContractApiImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String personalRegister(PersonalRegister pr) {
        return httpClient.toString();
    }
}
