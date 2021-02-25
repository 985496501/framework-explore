package org.example.contract.api;


import org.example.contract.pojo.reponse.SignContractCallBack;

public interface ContractActionCallback {
    void callback(SignContractCallBack t);
}
