package cc.jinyun.contract.pojo.callback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestSignNotifyResult<T> {
    private String action;
    private T params;
}
