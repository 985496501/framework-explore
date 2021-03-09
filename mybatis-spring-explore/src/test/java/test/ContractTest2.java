package test;

import cc.jinyun.contract.pojo.request.*;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.EntryApplication;
import cc.jinyun.contract.internal.ContractApi;
import cc.jinyun.contract.pojo.other.CharPosition;
import cc.jinyun.contract.pojo.other.PdfPageContentPositions;
import cc.jinyun.contract.util.PdfUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryApplication.class)
public class ContractTest2 {
    @Resource
    private ContractApi bestSignApi;

    private static final String ACCOUNT = "17862328960";

    @Test
    public void personalRegisterTest() {
        Register pr = new PersonalRegister("17862328960", "刘津运", "370481199607143214");
        String s = bestSignApi.register(pr);
        System.out.println(s);
    }

    @Test
    public void queryRegisterTest() {
        boolean b = bestSignApi.queryRegister(new QueryRegister("17862328960", "161417279401000001"));
        System.out.println(b);
    }

    @Test
    public void enterpriseRegisterTest() {
        Register pr = new EnterpriseRegister("13540287939", "四川世纪兆通物流有限公司", "91510107MA61T73PTW", "蒋开洪", "370481199607143214");
        String register = bestSignApi.register(pr);
        System.out.println(register);
    }

    @Test
    public void createPersonalSignTest() {
        bestSignApi.createPersonalSignature(new CreateSignature(ACCOUNT));
    }

    @Test
    public void createEnterpriseSignTest() {
        bestSignApi.createEnterpriseSignature(new CreateSignature("13540287939"));
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
        String s = bestSignApi.uploadContractTemplate(uc);
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
        String contract = bestSignApi.createContract(cc);
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

        String s = bestSignApi.uploadAndCreateContract(uc);
        System.out.println(s);
        // {"contractId":"161417057801000001"}
    }


    @Test
    public void addSignerTest() {
        AddSigner addSigner = new AddSigner("161422172901000002", "17862328960");
        bestSignApi.addSigner(addSigner);
    }


    @Test
    public void sendSmsTest() {
        SendSms sendSms = new SendSms("161422172901000002", "17862328960", "17862328960", "sms");
        bestSignApi.sendSms(sendSms);
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
        bestSignApi.autoSignWithVerification(autoSign);
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
        String s = bestSignApi.sendContract(contract);
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


    // -------------------------------------------------------------------------------------------------------------------


    /**
     * 通过模版生成合同文件
     */
    @Test
    public void createContractFromPdfTest() {
        CreateContractPdf contractPdf = new CreateContractPdf();
        contractPdf.setAccount("18380294241");
        contractPdf.setTid("161500019701000001");

        CreateContractPdf.TemplateValue templateValue = new CreateContractPdf.TemplateValue();
        templateValue.setUserName("刘津运");
        templateValue.setCompanyName("轻链科技有限公司");

        templateValue.setUserDate("2021-3-9");
        templateValue.setUserDate("2021-3-9");
        contractPdf.setTemplateValues(templateValue);

        String token = bestSignApi.generateContractFileByTemplate(contractPdf);
        System.out.println(token);
    }


    /**
     * eyJmaWQiOiI0OTgzMjI4NzM0MzkyNDY4MDQxIiwidmFsdWVJdGVtcyI6eyJjb21wYW55RGF0ZSI6eyJpc1JlcXVpcmVkIjoiMSIsInNpZ25lclRhZyI6IjAiLCJ4IjowLjYxMjgsInRhZ1R5cGUiOiIwIiwid2lkdGgiOjAuMTg1MywieSI6MC40MTM1LCJ0YWciOiLnrb7nvbLml6XmnJ8iLCJ0eXBlIjoiMjAiLCJwYWdlTnVtIjoyLCJ2YWx1ZSI6IiIsImhlaWdodCI6MC4wMTk2fSwiY29tcGFueU5hbWUiOnsiaXNSZXF1aXJlZCI6IjEiLCJzaWduZXJUYWciOiIwIiwieCI6MC4xNTI1LCJ0YWdUeXBlIjoiMCIsIndpZHRoIjowLjE2NzcsInkiOjAuMTg1MywidGFnIjoi5paH5pysIiwidHlwZSI6IjExIiwicGFnZU51bSI6MSwidmFsdWUiOiLovbvpk77np5HmioDmnInpmZDlhazlj7giLCJoZWlnaHQiOjAuMDIwNH0sInVzZXJOYW1lIjp7ImlzUmVxdWlyZWQiOiIxIiwic2lnbmVyVGFnIjoiMCIsIngiOjAuMTUxMywidGFnVHlwZSI6IjAiLCJ3aWR0aCI6MC4xNTM4LCJ5IjowLjE1NTksInRhZyI6IuaWh+acrCIsInR5cGUiOiIxMSIsInBhZ2VOdW0iOjEsInZhbHVlIjoi5YiY5rSl6L+QIiwiaGVpZ2h0IjowLjAyNH0sInVzZXJEYXRlIjp7ImlzUmVxdWlyZWQiOiIxIiwic2lnbmVyVGFnIjoiMCIsIngiOjAuMTcyNywidGFnVHlwZSI6IjAiLCJ3aWR0aCI6MC4xNSwieSI6MC40MTYyLCJ0YWciOiLnrb7nvbLml6XmnJ8iLCJ0eXBlIjoiMjAiLCJwYWdlTnVtIjoyLCJ2YWx1ZSI6IiIsImhlaWdodCI6MC4wMjR9fSwidGlkIjoiMTYxNTAwMDE5NzAxMDAwMDAxIiwic2lnbmF0dXJlVmFycyI6eyJjb21wYW55RGF0ZSI6eyJpc1JlcXVpcmVkIjoiMSIsImRhdGVUaW1lRm9ybWF0IjoieXl5eS1NTS1kZCIsIngiOjAuNjEyOCwidGFnVHlwZSI6IjAiLCJuYW1lIjoiY29tcGFueURhdGUiLCJ3aWR0aCI6MC4xODUzLCJ5IjowLjQxMzUsImZvbnRTaXplIjoxNC4wLCJ0YWciOiLnrb7nvbLml6XmnJ8iLCJ0eXBlIjoiMjAiLCJwYWdlTnVtIjoyLCJoZWlnaHQiOjAuMDE5Nn0sImNvbXBhbnkiOnsiaXNSZXF1aXJlZCI6IjEiLCJkYXRlVGltZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJ4IjowLjY3MzMsInRhZ1R5cGUiOiIwIiwibmFtZSI6ImNvbXBhbnkiLCJ5IjowLjI5NTksInRhZyI6IueblueroCIsInR5cGUiOiI1MCIsInBhZ2VOdW0iOjJ9LCJ1c2VyIjp7ImlzUmVxdWlyZWQiOiIxIiwiZGF0ZVRpbWVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwieCI6MC4yMjY5LCJ0YWdUeXBlIjoiMCIsIm5hbWUiOiJ1c2VyIiwieSI6MC4yODY5LCJ0YWciOiLnm5bnq6AiLCJ0eXBlIjoiNTAiLCJwYWdlTnVtIjoyfSwidXNlckRhdGUiOnsiaXNSZXF1aXJlZCI6IjEiLCJkYXRlVGltZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJ4IjowLjE3MjcsInRhZ1R5cGUiOiIwIiwibmFtZSI6InVzZXJEYXRlIiwid2lkdGgiOjAuMTUsInkiOjAuNDE2MiwiZm9udFNpemUiOjE0LjAsInRhZyI6Iuetvue9suaXpeacnyIsInR5cGUiOiIyMCIsInBhZ2VOdW0iOjIsImhlaWdodCI6MC4wMjR9fX0=.09668279a81fd63e0f38409ca16a4c64
     */
    @Test
    public void createContractByFileTest() {
        String token = "eyJmaWQiOiI5ODEzODgyODk0ODIxMzYyOSIsInZhbHVlSXRlbXMiOnsiY29tcGFueURhdGUiOnsiaXNSZXF1aXJlZCI6IjEiLCJzaWduZXJUYWciOiIwIiwieCI6MC42MTI4LCJ0YWdUeXBlIjoiMCIsIndpZHRoIjowLjE4NTMsInkiOjAuNDEzNSwidGFnIjoi562+572y5pel5pyfIiwidHlwZSI6IjIwIiwicGFnZU51bSI6MiwidmFsdWUiOiIiLCJoZWlnaHQiOjAuMDE5Nn0sImNvbXBhbnlOYW1lIjp7ImlzUmVxdWlyZWQiOiIxIiwic2lnbmVyVGFnIjoiMCIsIngiOjAuMTU1MSwidGFnVHlwZSI6IjAiLCJ3aWR0aCI6MC4zOTk3LCJ5IjowLjE5NTEsInRhZyI6IuaWh+acrCIsInR5cGUiOiIxMSIsInBhZ2VOdW0iOjEsInZhbHVlIjoi6L276ZO+56eR5oqA5pyJ6ZmQ5YWs5Y+4IiwiaGVpZ2h0IjowLjAzMDN9LCJ1c2VyTmFtZSI6eyJpc1JlcXVpcmVkIjoiMSIsInNpZ25lclRhZyI6IjAiLCJ4IjowLjE1NTEsInRhZ1R5cGUiOiIwIiwid2lkdGgiOjAuMzkzNCwieSI6MC4xNjA0LCJ0YWciOiLmlofmnKwiLCJ0eXBlIjoiMTEiLCJwYWdlTnVtIjoxLCJ2YWx1ZSI6IuWImOa0pei/kCIsImhlaWdodCI6MC4wMjk0fSwidXNlckRhdGUiOnsiaXNSZXF1aXJlZCI6IjEiLCJzaWduZXJUYWciOiIwIiwieCI6MC4xNzI3LCJ0YWdUeXBlIjoiMCIsIndpZHRoIjowLjE1MTMsInkiOjAuNDE2MiwidGFnIjoi562+572y5pel5pyfIiwidHlwZSI6IjIwIiwicGFnZU51bSI6MiwidmFsdWUiOiIiLCJoZWlnaHQiOjAuMDI0fX0sInRpZCI6IjE2MTUwMDAxOTcwMTAwMDAwMSIsInNpZ25hdHVyZVZhcnMiOnsiY29tcGFueURhdGUiOnsiaXNSZXF1aXJlZCI6IjEiLCJkYXRlVGltZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJ4IjowLjYxMjgsInRhZ1R5cGUiOiIwIiwibmFtZSI6ImNvbXBhbnlEYXRlIiwid2lkdGgiOjAuMTg1MywieSI6MC40MTM1LCJmb250U2l6ZSI6MTQuMCwidGFnIjoi562+572y5pel5pyfIiwidHlwZSI6IjIwIiwicGFnZU51bSI6MiwiaGVpZ2h0IjowLjAxOTZ9LCJjb21wYW55Ijp7ImlzUmVxdWlyZWQiOiIxIiwiZGF0ZVRpbWVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwieCI6MC42NzMzLCJ0YWdUeXBlIjoiMCIsIm5hbWUiOiJjb21wYW55IiwieSI6MC4yOTU5LCJ0YWciOiLnm5bnq6AiLCJ0eXBlIjoiNTAiLCJwYWdlTnVtIjoyfSwidXNlciI6eyJpc1JlcXVpcmVkIjoiMSIsImRhdGVUaW1lRm9ybWF0IjoieXl5eS1NTS1kZCIsIngiOjAuMjM0NSwidGFnVHlwZSI6IjAiLCJuYW1lIjoidXNlciIsInkiOjAuMzUxMSwidGFnIjoi562+5ZCNIiwidHlwZSI6IjQwIiwicGFnZU51bSI6Mn0sInVzZXJEYXRlIjp7ImlzUmVxdWlyZWQiOiIxIiwiZGF0ZVRpbWVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwieCI6MC4xNzI3LCJ0YWdUeXBlIjoiMCIsIm5hbWUiOiJ1c2VyRGF0ZSIsIndpZHRoIjowLjE1MTMsInkiOjAuNDE2MiwiZm9udFNpemUiOjE0LjAsInRhZyI6Iuetvue9suaXpeacnyIsInR5cGUiOiIyMCIsInBhZ2VOdW0iOjIsImhlaWdodCI6MC4wMjR9fX0=.5a2d94732c6c973df0868e38a3d6bcb2";
        CreateContractByFile createContractByFile = new CreateContractByFile();
        createContractByFile.setAccount("17862328960");
        createContractByFile.setTitle("合同");
        createContractByFile.setTid("161500019701000001");
        createContractByFile.setTemplateToken(token);
        String contractByFile = bestSignApi.createContractByFile(createContractByFile);
        System.out.println(contractByFile);
    }

    // 161527533101000001
    @Test
    public void manuallySignByTemplateValsTest() {
        ManuallySignByTemplateVal templateVal = new ManuallySignByTemplateVal();
        templateVal.setContractId("161527781201000001");
        templateVal.setPushUrl("");
        templateVal.setSigner("17862328960");
        templateVal.setTid("161500019701000001");
        templateVal.setIsDrawSignatureImage("0");
        templateVal.setVarNames("user,userName,userDate");
        String s = bestSignApi.manuallySignByTemplateVals(templateVal);
        System.out.println(s);
    }


    @Test
    public void automaticallySignByTemplateValsTest() {
        AutomaticallySignByTemplateVal templateVal = new AutomaticallySignByTemplateVal();
        templateVal.setContractId("161527781201000001");
        templateVal.setTid("161500019701000001");

        AutomaticallySignByTemplateVal.Vals vals = new AutomaticallySignByTemplateVal.Vals(new AutomaticallySignByTemplateVal.Val("18380294241"),
                new AutomaticallySignByTemplateVal.Val("18380294241"), new AutomaticallySignByTemplateVal.Val("18380294241"));
        templateVal.setVars(vals);
        bestSignApi.automaticallySignByTemplateVals(templateVal);
    }


    @Test
    public void lockContractTest() {
        ContractId contractId = new ContractId("161527781201000001");
        bestSignApi.lockContract(contractId);
    }

    @Test
    public void queryContractTest() {
        PreviewContract previewContract = new PreviewContract("161527781201000001", "17862328960");
        String s = bestSignApi.previewContract(previewContract);
        System.out.println(s);
    }





}
