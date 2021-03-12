package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractPdf {
    private String account;

    private String tid;

    /**
     * template value
     * 1. userName userDate companyName companyDate
     * 2. populate the placeholder with the given value that matches the specified key.
     */
    private Map<String, String> templateValues;
}
