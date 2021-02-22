package test;

import org.example.EntryApplication;
import org.example.contract.api.ContractApi;
import org.example.contract.pojo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryApplication.class)
public class ContractTest {
    @Autowired
    private ContractApi contractApi;

    private static final String ACCOUNT = "17862328960";

    @Test
    public void personalRegisterTest() {
        Register pr = new PersonalRegister(ACCOUNT, "刘津运", "370481199607143214");
        String s = contractApi.register(pr);
        System.out.println(s);
    }

    @Test
    public void queryRegisterTest() {
        boolean b = contractApi.queryRegister(new QueryRegister("13540287939", "161397247201000002"));
        System.out.println(b);
    }

    @Test
    public void enterpriseRegisterTest() {
        Register pr = new EnterpriseRegister("13540287939", "四川世纪兆通物流有限公司", "91510107MA61T73PTW", "蒋开洪", "370481199607143214");
        String register = contractApi.register(pr);
        System.out.println(register);
    }

    @Test
    public void createPersonalSignTest() {
        contractApi.createPersonalSignature(new CreateSignature(ACCOUNT));
    }

    @Test
    public void createEnterpriseSignTest() {
        contractApi.createEnterpriseSignature(new CreateSignature("13540287939"));
    }
}
