package cc.jinyun.contract.external;


import cc.jinyun.contract.pojo.other.ExposedContractAttr;
import cc.jinyun.contract.pojo.other.ExposedContractUrl;

public interface BestSignService {
    ExposedContractAttr signAnnualContract(SignContractParam signContractParam);

    ExposedContractUrl generateBothUrl(SignContractParam signContractParam);
}
