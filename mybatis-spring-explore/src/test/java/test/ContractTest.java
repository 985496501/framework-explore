package test;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.EntryApplication;
import org.example.contract.api.ContractApi;
import org.example.contract.pojo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryApplication.class)
public class ContractTest {
    @Autowired
    private ContractApi contractApi;

    private static final String ACCOUNT = "17862328960";

    @Test
    public void personalRegisterTest() {
        Register pr = new PersonalRegister("17862328960", "刘津运", "370481199607143214");
        String s = contractApi.register(pr);
        System.out.println(s);
    }

    @Test
    public void queryRegisterTest() {
        boolean b = contractApi.queryRegister(new QueryRegister("17862328960", "161417279401000001"));
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

    @Test
    public void createAndCreateContractTest() {
        UploadContract uc = new UploadContract();
        uc.setAccount("13540287939");
        uc.setType("pdf");
        long times = System.currentTimeMillis()/1000 + 3600 * 7 * 30;
        uc.setExpireTime("" + times);
        //文件内容Base64字符串，例如： FileInputStream file = new FileInputStream("d: \\test\\接口系统.pdf");
        // byte[] bdata = IOUtils.toByteArray(file); String fdata =Base64.encodeBase64String(bdata);
        BufferedInputStream bufferedInputStream = FileUtil.getInputStream("转正申请-刘津运.pdf");
        FastByteArrayOutputStream read = IoUtil.read(bufferedInputStream);
        byte[] bytes = read.toByteArray();
        uc.setFdata(Base64.encode(bytes));

        uc.setFname("转正申请-刘津运.pdf");
        uc.setFmd5(DigestUtils.md5Hex(bytes));
        uc.setTitle("转正申请");
        uc.setFpages("2");

        String s = contractApi.uploadAndCreateContract(uc);
        System.out.println(s);
        // {"contractId":"161417057801000001"}
    }

    @Test
    public void addSignerTest() {
        AddSigner addSigner = new AddSigner("161417057801000001", "17862328960");
        contractApi.addSigner(addSigner);
    }


    @Test
    public void sendSmsTest() {
        SendSms sendSms = new SendSms("161417057801000001", "17862328960", "17862328960", "sms");
        contractApi.sendSms(sendSms);
    }

    @Test
    public void autoSignWithVerificationTest() {
        AutoSignWithVerification autoSign = new AutoSignWithVerification();
        autoSign.setContractId("161417057801000001");
        autoSign.setSendTarget("17862328960");
        autoSign.setVcode("121212");
        autoSign.setSigner("1786232890");
        List<AutoSignWithVerification.SignaturePosition> signaturePositions = Collections.singletonList(new AutoSignWithVerification.SignaturePosition("100", "200", "2"));
        autoSign.setSignaturePositions(signaturePositions);
        contractApi.autoSignWithVerification(autoSign);
    }

}
