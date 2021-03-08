package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractPdf {
    private String account;

    private String tid;

    private List<TemplateValue> templateValues;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateValue {
//        private String userName;
//        private String companyName;
//
//        private String userDate;
//        private String companyDate;


    }
}
