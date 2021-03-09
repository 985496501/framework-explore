package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutomaticallySignByTemplateVal {
    private String contractId;
    private String tid;

    private Vals vars;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Vals {
        private Val company;
        private Val companyDate;
        private Val companyName;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Val {
        private String account;
    }
}
