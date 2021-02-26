package cc.jinyun.contract.pojo.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignContractCallBack {
    private String contractId;

    private String account;

    private String signerStatus;

    private String errMsg;

    private String code;

    private String sid;
}
