package cc.jinyun.contract.api;


import cc.jinyun.contract.pojo.reponse.SignContractCallBack;

public interface ContractActionCallback {
    void callback(SignContractCallBack t);
}
