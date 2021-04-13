package cc.jinyun.contract.pojo.other;

import lombok.Data;

@Data
public class ExposedContractUrl {
    private String one;

    private String other;

    private String contractId;

    private ExposedContractUrl(String one, String other) {
        this.one = one;
        this.other = other;
    }

    private ExposedContractUrl(String one, String other, String contractId) {
        this.one = one;
        this.other = other;
        this.contractId = contractId;
    }

    public static ExposedContractUrl newInstance(String one, String other, String contractId) {
        return new ExposedContractUrl(one, other, contractId);
    }
}
