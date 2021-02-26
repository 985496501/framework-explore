package cc.jinyun.contract;

import cc.jinyun.contract.api.ContractActionCallback;
import cc.jinyun.contract.pojo.reponse.SignContractCallBack;
import org.springframework.stereotype.Service;

/**
 * @author: jinyun
 * @date: 2021/2/25
 */
@Service
public class Dobusiness implements ContractActionCallback {
    @Override
    public void callback(SignContractCallBack t) {
        System.out.println("在这里做你的罗级=========================================================");
        System.out.println("在这里做你的罗级=========================================================");
        System.out.println("在这里做你的罗级=========================================================");
        System.out.println("在这里做你的罗级=========================================================");
        System.out.println("在这里做你的罗级=========================================================");
        System.out.println("在这里做你的罗级=========================================================");
        System.out.println("在这里做你的罗级=========================================================");
        System.out.println("在这里做你的罗级=========================================================");
    }
}
