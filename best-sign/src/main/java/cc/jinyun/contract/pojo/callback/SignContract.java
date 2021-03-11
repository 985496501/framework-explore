package cc.jinyun.contract.pojo.callback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignContract {
    private String contractId;
    private String signerStatus;
    private String account;
}
