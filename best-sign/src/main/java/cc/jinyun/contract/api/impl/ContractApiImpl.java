package cc.jinyun.contract.api.impl;

import cc.jinyun.contract.api.ContractApi;
import cc.jinyun.contract.client.HttpClient;
import cc.jinyun.contract.constant.MethodEnum;
import cc.jinyun.contract.pojo.reponse.*;
import cc.jinyun.contract.pojo.request.*;

/**
 * log aop
 * 合同相关所有的api
 */
public class ContractApiImpl implements ContractApi {
    private final HttpClient httpClient;

    public ContractApiImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String register(Register pr) {
        return httpClient.post(MethodEnum.REGISTER, pr, RegisterResult.class).getTaskId();
    }

    @Override
    public String enterpriseRegister(Register pr) {
        return httpClient.post(MethodEnum.REGISTER, pr, RegisterResult.class).getTaskId();
    }

    @Override
    public boolean queryRegister(QueryRegister qr) {
        QueryRegister.QueryRegisterResult result = httpClient.post(MethodEnum.QUERY_REGISTER, qr, QueryRegister.QueryRegisterResult.class);
        return QueryRegister.RegisterEnum.success(result.getStatus());
    }

    @Override
    public void createPersonalSignature(CreateSignature cs) {
        httpClient.post(MethodEnum.CREATE_PERSONAL_SIGNATURE, cs, String.class);
    }

    @Override
    public void createEnterpriseSignature(CreateSignature cs) {
        httpClient.post(MethodEnum.CREATE_ENTERPRISE_SIGNATURE, cs, String.class);
    }

    @Override
    public String uploadContractTemplate(UploadContractTemplate uc) {
        return httpClient.post(MethodEnum.UPLOAD_CONTRACT_TEMPLATE, uc, FidResult.class).getFid();
    }

    @Override
    public String createContractPdf(CreateContractPdf contractPdf) {
        return httpClient.post(MethodEnum.CREATE_CONTRACT_PDF, contractPdf, TemplateTokenResult.class).getTemplateToken();
    }

    @Override
    public String addPdfElements(AddPdfElements ape) {
        return httpClient.post(MethodEnum.ADD_PDF_ELEMENTS, ape, FidResult.class).getFid();
    }

    @Override
    public String createContract(CreateContract cc) {
        return httpClient.post(MethodEnum.CREATE_CONTRACT, cc, String.class);
    }

    @Override
    public String uploadAndCreateContract(UploadAndCreateContract uc) {
        return httpClient.post(MethodEnum.UPLOAD_AND_CREATE_CONTRACT, uc, String.class);
    }

    @Override
    public void addSigner(AddSigner as) {
        httpClient.post(MethodEnum.ADD_SINGER, as, String.class);
    }

    @Override
    public void addSigners(AddSigners ass) {
        httpClient.post(MethodEnum.BATCH_ADD_SINGERS, ass, String.class);
    }

    @Override
    public void sendSms(SendSms ss) {
        httpClient.post(MethodEnum.SEND_SMS, ss, String.class);
    }

    @Override
    public String sendContract(SendContract sc) {
        return httpClient.post(MethodEnum.SEND_CONTRACT, sc, UrlResult.class).getUrl();
    }

    @Override
    public void autoSignWithVerification(AutoSignWithVerification aswv) {
        httpClient.post(MethodEnum.AUTO_SIGN_WITH_VERIFICATION, aswv, String.class);
    }

    @Override
    public void lockContract(ContractId contractId) {
        httpClient.post(MethodEnum.LOCK_CONTRACT, contractId, String.class);
    }

    @Override
    public String previewContract(PreviewContract pc) {
        return httpClient.post(MethodEnum.PREVIEW_CONTRACT, pc, UrlResult.class).getUrl();
    }

    @Override
    public void unifiedNotifyHandler(NotifyResult result, String sign, String rtick) {
        // todo: 签名校验 先不做
        // SpringContextFacade.getBean(ContractActionCallback.class).callback(result.getParams());
    }
}
