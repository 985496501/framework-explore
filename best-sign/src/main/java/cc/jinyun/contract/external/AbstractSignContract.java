package cc.jinyun.contract.external;


import cc.jinyun.contract.config.AppProperties;
import cc.jinyun.contract.internal.ContractApi;
import cc.jinyun.contract.pojo.request.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public abstract class AbstractSignContract implements UserDetailService {
    public static final String COMPANY_ACCOUNT = "18380294241";
    public static final String COMPANY_NAME = " 四川世纪兆通物流有限公司";
    public static final String CONTRACT_FILE_NAME = "年签合同";
    public static final String CONTRACT_TEMPLATE_ID = "161500019701000001";
    public static final String PUSH_URL = "http://3qw3em.natappfree.cc/callback";

    @Resource
    protected ContractApi bestSignApi;
    @Resource
    private AppProperties appProperties;

    @Override
    public abstract UserDetail getUserDetail(Long userId);

    public abstract boolean hasRegistered(Long userId);

    public String signContract(Long userId) {
        UserDetail userDetail = getUserDetail(userId);
        if (!hasRegistered(userId)) {
            this.preparePersonalMaterial(userDetail);
        }

        String contractId = this.obtainContractId(userDetail.getUserName());
        this.automaticallySignByTemplateVals(contractId);
        return this.manuallySignByTemplateVals(contractId, userDetail.getPhoneNumber());
    }

    private void preparePersonalMaterial(UserDetail userDetail) {
        Register pr = new PersonalRegister(userDetail.getPhoneNumber(), userDetail.getUserName(),
                userDetail.getIdNumber());
        log.info("registering...");
        String taskId = bestSignApi.register(pr);

        boolean success = bestSignApi.queryRegister(new QueryRegister(userDetail.getPhoneNumber(), taskId));
        if (success) {
            log.info("registration successes...");
        }

        bestSignApi.createPersonalSignature(new CreateSignature(userDetail.getPhoneNumber()));
    }

    private String obtainContractId(String userName) {
        log.info("generating contract file by template...");
        String localDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        CreateContractPdf createContractPdf = CreateContractPdf.builder()
                .account(COMPANY_ACCOUNT)
                .tid(CONTRACT_TEMPLATE_ID)
                .templateValues(new CreateContractPdf.TemplateValue(userName, localDate, COMPANY_NAME, localDate))
                .build();
        String token = bestSignApi.generateContractFileByTemplate(createContractPdf);

        CreateContractByFile createContractByFile = new CreateContractByFile();
        createContractByFile.setAccount(COMPANY_ACCOUNT);
        createContractByFile.setTitle(CONTRACT_FILE_NAME);
        createContractByFile.setTid(CONTRACT_TEMPLATE_ID);
        createContractByFile.setTemplateToken(token);
        return bestSignApi.createContractByFile(createContractByFile);
    }

    private void automaticallySignByTemplateVals(String contractId) {
        AutomaticallySignByTemplateVal templateVal = new AutomaticallySignByTemplateVal();
        templateVal.setContractId(contractId);
        templateVal.setTid(CONTRACT_TEMPLATE_ID);

        AutomaticallySignByTemplateVal.Vals vals = new AutomaticallySignByTemplateVal.Vals(new AutomaticallySignByTemplateVal.Val(COMPANY_ACCOUNT),
                new AutomaticallySignByTemplateVal.Val(COMPANY_ACCOUNT), new AutomaticallySignByTemplateVal.Val(COMPANY_ACCOUNT));
        templateVal.setVars(vals);
        bestSignApi.automaticallySignByTemplateVals(templateVal);
    }

    private String manuallySignByTemplateVals(String contractId, String phoneNumber) {
        ManuallySignByTemplateVal templateVal = new ManuallySignByTemplateVal();
        templateVal.setContractId(contractId);

        String pushUrl = appProperties.getPushUrl();
        templateVal.setPushUrl(pushUrl == null ? PUSH_URL : pushUrl);
        templateVal.setSigner(phoneNumber);
        templateVal.setTid(CONTRACT_TEMPLATE_ID);
        templateVal.setIsDrawSignatureImage("0");
        templateVal.setVarNames("user,userName,userDate");
        return bestSignApi.manuallySignByTemplateVals(templateVal);
    }

}
