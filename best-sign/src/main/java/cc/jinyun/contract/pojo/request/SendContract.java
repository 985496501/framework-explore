package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendContract {
    private String contractId;

    private String account;

    private String signer;

    private String expireTime;

    private List<SignaturePosition> signaturePositions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignaturePosition {
        private String x;
        private String y;
        private String pageNum;
    }
}
