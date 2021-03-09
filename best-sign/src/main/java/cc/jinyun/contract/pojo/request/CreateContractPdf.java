package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractPdf {
    private String account;

    private String tid;

    private TemplateValue templateValues;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateValue {
        private String userName;
        private String userDate;

        private String companyName;
        private String companyDate;
    }
}
