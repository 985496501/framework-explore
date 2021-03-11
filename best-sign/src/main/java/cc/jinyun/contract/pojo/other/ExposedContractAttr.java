package cc.jinyun.contract.pojo.other;

import lombok.Data;

@Data
public class ExposedContractAttr {
    private String contractId;

    private String contractUrl;

    private ExposedContractAttr(String contractId, String contractUrl) {
        this.contractId = contractId;
        this.contractUrl = contractUrl;
    }

    public static ExposedContractAttr newInstance(String contractId, String contractUrl) {
        return new ExposedContractAttr(contractId, contractUrl);
    }
}
