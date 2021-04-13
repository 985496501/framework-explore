package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoSignWithVerification {
    private String contractId;
    private String signer;
    private List<SignaturePosition> signaturePositions;

    private String vcode;
    private String sendTarget;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignaturePosition {
        private String x;
        private String y;
        private String pageNum;
    }
}
