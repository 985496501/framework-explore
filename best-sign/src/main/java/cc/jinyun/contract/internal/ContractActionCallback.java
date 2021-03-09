package cc.jinyun.contract.internal;


import cc.jinyun.contract.pojo.reponse.SignContractCallBack;

interface ContractActionCallback {
    /**
     * comma-separated
     *
     * @param t
     */
    void callback(SignContractCallBack t);
}
