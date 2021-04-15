package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContract {
    private String account;

    private String fid;

    private String title;

    private String expireTime;
}
