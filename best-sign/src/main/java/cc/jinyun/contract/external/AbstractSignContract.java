package cc.jinyun.contract.external;


import cc.jinyun.contract.config.AppProperties;
import cc.jinyun.contract.config.ContractTemplate;
import cc.jinyun.contract.external.user.UserDetail;
import cc.jinyun.contract.external.user.UserDetailService;
import cc.jinyun.contract.internal.ContractApi;
import cc.jinyun.contract.pojo.other.ExposedContractAttr;
import cc.jinyun.contract.pojo.request.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * How to support multiple kinds of contract?
 */
@Slf4j
public abstract class AbstractSignContract implements UserDetailService {
    @Resource
    protected ContractApi bestSignApi;
    @Resource
    private AppProperties appProperties;

    @Override
    public abstract UserDetail getUserDetail(Long userId);

    public abstract boolean hasRegistered(Long userId);

    /**
     * Dedicated to sign annual contract of the given user
     *
     * @param userId        the given id of user
     * @param contractIndex the index of contractList configured
     * @return the short url of contract
     */
    public final ExposedContractAttr signAnnualContract(Long userId, Integer contractIndex) {
        UserDetail userDetail = getUserDetail(userId);
        if (!hasRegistered(userId)) {
            this.preparePersonalMaterial(userDetail);
        }

        ContractTemplate contractTemplate = appProperties.getContractTemplateList().get(contractIndex);
        String contractId = this.obtainContractId(userDetail.getUserName(), contractTemplate.getTid(), contractTemplate.getTitle());
        this.automaticallySignByTemplateVals(contractId, contractTemplate.getTid());
        String contractUrl = this.manuallySignByTemplateVals(contractId, userDetail.getPhoneNumber(), contractTemplate.getTid());
        return ExposedContractAttr.newInstance(contractId, contractUrl);
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

    private String obtainContractId(String userName, String tid, String title) {
        log.info("generating contract file by template...");
        String localDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        CreateContractPdf createContractPdf = CreateContractPdf.builder()
                .account(appProperties.getCompanyAccount())
                .tid(tid)
                .templateValues(new CreateContractPdf.TemplateValue(userName, localDate,
                        appProperties.getCompanyName(), localDate))
                .build();
        String token = bestSignApi.generateContractFileByTemplate(createContractPdf);

        CreateContractByFile createContractByFile = new CreateContractByFile();
        createContractByFile.setAccount(appProperties.getCompanyAccount());
        createContractByFile.setTitle(title);
        createContractByFile.setTid(tid);
        createContractByFile.setTemplateToken(token);
        return bestSignApi.createContractByFile(createContractByFile);
    }

    private void automaticallySignByTemplateVals(String contractId, String tid) {
        AutomaticallySignByTemplateVal templateVal = new AutomaticallySignByTemplateVal();
        templateVal.setContractId(contractId);
        templateVal.setTid(tid);

        AutomaticallySignByTemplateVal.Vals vals = new AutomaticallySignByTemplateVal.Vals(
                new AutomaticallySignByTemplateVal.Val(appProperties.getCompanyAccount()),
                new AutomaticallySignByTemplateVal.Val(appProperties.getCompanyAccount()),
                new AutomaticallySignByTemplateVal.Val(appProperties.getCompanyAccount()));
        templateVal.setVars(vals);
        bestSignApi.automaticallySignByTemplateVals(templateVal);
    }

    private String manuallySignByTemplateVals(String contractId, String phoneNumber, String tid) {
        ManuallySignByTemplateVal templateVal = new ManuallySignByTemplateVal();
        templateVal.setContractId(contractId);

        templateVal.setPushUrl(appProperties.getPushUrl());
        templateVal.setSigner(phoneNumber);
        templateVal.setTid(tid);
        templateVal.setIsDrawSignatureImage("0");
        templateVal.setVarNames("user,userName,userDate");
        return bestSignApi.manuallySignByTemplateVals(templateVal);
    }
}
