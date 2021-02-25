package test;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.EntryApplication;
import org.example.contract.api.ContractApi;
import org.example.contract.pojo.other.CharPosition;
import org.example.contract.pojo.other.PdfPageContentPositions;
import org.example.contract.pojo.request.*;
import org.example.contract.util.PdfUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.IOException;
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
    public void uploadContractTest() {
        UploadContractTemplate uc = new UploadContractTemplate();
        uc.setAccount("13540287939");

        BufferedInputStream bufferedInputStream = FileUtil.getInputStream("转正申请-刘津运.pdf");
        FastByteArrayOutputStream read = IoUtil.read(bufferedInputStream);
        byte[] bytes = read.toByteArray();
        uc.setFdata(Base64.encode(bytes));

        uc.setFname("转正申请-刘津运.pdf");
        uc.setFmd5(DigestUtils.md5Hex(bytes));
        uc.setFpages("2");
        uc.setIsCleanup("1");
        String s = contractApi.uploadContractTemplate(uc);
        System.out.println(s);
        // {
        //    "fid": "4176791633686556341"
        //}
    }

    @Test
    public void createContractTest() {
        CreateContract cc = new CreateContract();
        cc.setAccount("13540287939");
        cc.setFid("4176791633686556341");
        cc.setTitle("转正申请");
        String contract = contractApi.createContract(cc);
        System.out.println(contract);
        // {"contractId":"161422172901000002"}
    }

    @Test
    public void uploadAndCreateContractTest() {
        UploadAndCreateContract uc = new UploadAndCreateContract();
        uc.setAccount("13540287939");
        uc.setType("pdf");
        long times = System.currentTimeMillis() / 1000 + 3600 * 7 * 30;
        uc.setExpireTime("" + times);

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
        AddSigner addSigner = new AddSigner("161422172901000002", "17862328960");
        contractApi.addSigner(addSigner);
    }


    @Test
    public void sendSmsTest() {
        SendSms sendSms = new SendSms("161422172901000002", "17862328960", "17862328960", "sms");
        contractApi.sendSms(sendSms);
    }

    @Test
    public void autoSignWithVerificationTest() {
        AutoSignWithVerification autoSign = new AutoSignWithVerification();
        autoSign.setContractId("161422172901000002");
        autoSign.setSendTarget("17862328960");
        autoSign.setVcode("123456");
        autoSign.setSigner("17862328960");
        List<AutoSignWithVerification.SignaturePosition> signaturePositions = Collections.singletonList(new AutoSignWithVerification.SignaturePosition("0.4146", "0.1448", "2"));
        autoSign.setSignaturePositions(signaturePositions);
        contractApi.autoSignWithVerification(autoSign);
    }

    @Test
    public void lockContractTest() {
        ContractId contractId = new ContractId("161422172901000002");
        contractApi.lockContract(contractId);
    }

    @Test
    public void queryContractTest() {
        PreviewContract previewContract = new PreviewContract("161422172901000002", "17862328960");
        String s = contractApi.previewContract(previewContract);
        System.out.println(s);
    }

    // __________________________________________________________________________________________________


    @Test
    public void sendContractTest() {
        SendContract contract = new SendContract();
        contract.setContractId("161423364501000001");
        contract.setAccount("17862328960");
        contract.setExpireTime("");
        contract.setSigner("17862328960");
        List<SendContract.SignaturePosition> signaturePositions = Collections.singletonList(new SendContract.SignaturePosition("0.4146", "0.1448", "2"));
        contract.setSignaturePositions(signaturePositions);
        String s = contractApi.sendContract(contract);
        System.out.println(s);
    }


    @Test
    public void pdfTest() throws IOException {

        String keyword = "申请人签字";

        byte[] pdfData = PdfUtils.getFileData("E:\\learning\\framework\\mybatis-spring-explore\\src\\main\\resources\\转正申请-刘津运.pdf");

        List<PdfPageContentPositions> pdfPageContentPositions = null;
        try {
            pdfPageContentPositions = PdfUtils.getPdfContentPositionsList(pdfData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PdfPageContentPositions pdfPageContentPosition : pdfPageContentPositions) {
            List<CharPosition> charPositions = PdfUtils.findPositions(keyword, pdfPageContentPosition);
            if (charPositions == null || charPositions.size() < 1) {
                continue;
            }

            System.out.println(JSONUtil.toJsonPrettyStr(charPositions));
        }


    }
}
