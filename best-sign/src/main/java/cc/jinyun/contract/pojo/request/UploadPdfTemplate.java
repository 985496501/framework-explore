package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadPdfTemplate {
    /**
     *
     */
    private String account;

    /**
     *
     */
    private String fdata;

    /**
     *
     */
    private String fmd5;

    /**
     *
     */
    private String ftype;

    /**
     *
     */
    private String fname;

    /**
     *
     */
    private String fpages;

    /**
     *
     */
    private String title;
}
