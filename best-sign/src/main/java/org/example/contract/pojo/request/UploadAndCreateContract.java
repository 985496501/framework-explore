package org.example.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadAndCreateContract {
    private String account;

    private String fmd5;

    private String fdata;

    private String fpages;

    private String fname;

    private String type;

    private String title;

    private String expireTime;
}
