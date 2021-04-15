package cc.jinyun.contract.external;

import cc.jinyun.contract.config.AppProperties;
import cc.jinyun.contract.pojo.other.ExposedContractAttr;
import cc.jinyun.contract.pojo.other.ExposedContractUrl;
import cc.jinyun.contract.pojo.request.AutomaticallySignByTemplateVal;

import javax.annotation.Resource;
import java.util.Map;


public abstract class BaseSignBestSignContractAdapter extends AbstractSignBestSignContract {
    @Resource
    protected AppProperties appProperties;
    @Resource
    private BestSignUserDetailService userDetailService;
    @Resource
    private BestSignContractTemplateService templateService;

    @Override
    public final String getPushUrl() {
        return appProperties.getPushUrl();
    }

    /**
     * Dedicated to sign annual contract of the given user
     *
     * @param param the given id of user, the id of the configured template and extra params transferred.
     * @return the short url of contract
     */
    @Override
    public final ExposedContractAttr signAnnualContract(SignContractParam param) {
        UserDetail userDetail = getUserDetail(param.getPartB());
        if (!registered(param.getPartB())) {
            this.preparePersonalMaterial(userDetail);
        }

        // run along the whole time.
        TemplateDetail templateDetail = getContractDetail(param.getTid(), param.getExtras());
        String contractId = this.obtainContractIdByAccount(appProperties.getCompanyAccount(), templateDetail);
        this.automaticallySignByTemplateVals(contractId, templateDetail.getTid());
        String contractUrl = this.manuallySignByTemplateVals(contractId, userDetail.getPhoneNumber(), templateDetail, 1);
        return ExposedContractAttr.newInstance(contractId, contractUrl);
    }

    /**
     * Dedicated to sign bi-contract of the given user
     *
     * @param param the given id of user, the id of the configured template and extra params transferred.
     * @return the short url of contract
     */
    @Override
    public final ExposedContractUrl generateBothUrl(SignContractParam param) {
        UserDetail oneUserDetail = getUserDetail(param.getPartA());
        if (!registered(param.getPartA())) {
            this.preparePersonalMaterial(oneUserDetail);
        }

        UserDetail anotherUserDetail = getUserDetail(param.getPartB());
        if (!registered(param.getPartB())) {
            this.preparePersonalMaterial(anotherUserDetail);
        }

        // run along the whole time.
        TemplateDetail templateDetail = getContractDetail(param.getTid(), param.getExtras());
        String contractId = this.obtainContractIdByAccount(oneUserDetail.getPhoneNumber(), templateDetail);
        String consignorUrl = this.manuallySignByTemplateVals(contractId, oneUserDetail.getPhoneNumber(), templateDetail, 0);
        String consigneeUrl = this.manuallySignByTemplateVals(contractId, anotherUserDetail.getPhoneNumber(), templateDetail, 1);
        return ExposedContractUrl.newInstance(consignorUrl, consigneeUrl, contractId);
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

    private boolean registered(Long userId) {
        return userDetailService.registered(userId);
    }

    private UserDetail getUserDetail(Long userId) {
        return userDetailService.getUserDetail(userId);
    }

    private TemplateDetail getContractDetail(String tid, Map<String, Object> extras) {
        return templateService.getContractDetail(tid, extras);
    }
}
