package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadContractTemplate {
    private String account;

    private String fmd5;

    private String fdata;

    private String fpages;

    private String fname;

    /**
     * 1- Compulsory.
     * 0- Voluntary.
     */
    private String isCleanup;
}
