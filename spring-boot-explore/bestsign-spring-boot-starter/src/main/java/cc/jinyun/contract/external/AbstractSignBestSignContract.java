package cc.jinyun.contract.external;

import cc.jinyun.contract.internal.ContractApi;
import cc.jinyun.contract.pojo.request.*;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;

/**
 * How to support multiple kinds of contract?
 * Can split userService and contractService
 */
public abstract class AbstractSignBestSignContract implements BestSignService {
    @Resource
    protected ContractApi bestSignApi;

    public abstract String getPushUrl();

    protected void preparePersonalMaterial(UserDetail userDetail) {
        Register pr = new PersonalRegister(userDetail.getPhoneNumber(), userDetail.getUserName(),
                userDetail.getIdNumber());
        String taskId = bestSignApi.register(pr);

        boolean success = bestSignApi.queryRegister(new QueryRegister(userDetail.getPhoneNumber(), taskId));
        // success=false swallow and continue.
        bestSignApi.createPersonalSignature(new CreateSignature(userDetail.getPhoneNumber()));
    }


    /**
     * Obtain the id of contract against the given account and contractTemplate.
     *
     * @param account          the given account
     * @param templateDetail the given template with variables
     * @return the id of the contract
     */
    public final String obtainContractIdByAccount(String account, TemplateDetail templateDetail) {
        CreateContractPdf createContractPdf = CreateContractPdf.builder()
                .account(account)
                .tid(templateDetail.getTid())
                .templateValues(templateDetail.getVariablesOfPlaceHolder())
                .build();
        String token = bestSignApi.generateContractFileByTemplate(createContractPdf);

        CreateContractByFile createContractByFile = new CreateContractByFile();
        createContractByFile.setAccount(account);
        createContractByFile.setTitle(templateDetail.getTitle());
        createContractByFile.setTid(templateDetail.getTid());
        createContractByFile.setTemplateToken(token);
        return bestSignApi.createContractByFile(createContractByFile);
    }


    /**
     * Manually sign the specified contract by the specified user.
     *
     * @param contractId       the specified contract
     * @param account          the specified account
     * @param templateDetail the specified template
     * @param index            the index of variables
     * @return url
     */
    protected String manuallySignByTemplateVals(String contractId, String account, TemplateDetail templateDetail, Integer index) {
        ManuallySignByTemplateVal templateVal = new ManuallySignByTemplateVal();
        templateVal.setContractId(contractId);
        templateVal.setPushUrl(getPushUrl());
        templateVal.setSigner(account);
        templateVal.setTid(templateDetail.getTid());
        templateVal.setIsDrawSignatureImage("0");
        templateVal.setVarNames(StrUtil.join(",", templateDetail.getVariablesOfCharter().get(index)));
        return bestSignApi.manuallySignByTemplateVals(templateVal);
    }
}
