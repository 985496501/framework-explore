package cc.jinyun.contract.internal;

import cc.jinyun.contract.pojo.callback.BestSignNotifyResult;
import cc.jinyun.contract.pojo.request.*;

public interface ContractApi {

    /**
     * Apply personal or enterprise signature.
     *
     * @param pr p
     * @return r  taskId
     */
    String register(Register pr);

    /**
     * Apply enterprise signature.
     *
     * @param pr p
     * @return r  taskId
     */
    String enterpriseRegister(Register pr);

    /**
     * Query signature status.
     *
     * @param qr p
     * @return s
     * @see QueryRegister.RegisterEnum
     */
    boolean queryRegister(QueryRegister qr);

    /**
     * Create personal signature.
     *
     * @param cs p
     */
    void createPersonalSignature(CreateSignature cs);

    /**
     * Create enterprise signature.
     *
     * @param cs p
     */
    void createEnterpriseSignature(CreateSignature cs);


    /**
     * Merely upload contract template file to the remote Oss.
     *
     * @param uc p
     * @return fid
     */
    String uploadContractTemplate(UploadContractTemplate uc);

    // -----------------------------------------------------------------

    String generateContractFileByTemplate(CreateContractPdf contractPdf);


    String createContractByFile(CreateContractByFile contractByFile);

    void automaticallySignByTemplateVals(AutomaticallySignByTemplateVal templateVal);

    String manuallySignByTemplateVals(ManuallySignByTemplateVal manuallySignByTemplateVal);

    // -----------------------------------------------------------------


    String uploadPdfTemplate(UploadPdfTemplate uploadPdfTemplate);

    /**
     * Populate specified fid with specified elements.
     *
     * @param ape p
     * @return new fid to create contract.
     */
    String addPdfElements(AddPdfElements ape);

    /**
     * Merely create contract to procession.
     *
     * @param cc p
     * @return fid
     */
    String createContract(CreateContract cc);

    /**
     * Upload and create a contract.
     *
     * @param uc p
     * @return contract id
     */
    String uploadAndCreateContract(UploadAndCreateContract uc);

    /**
     * Add signer.
     *
     * @param as p
     */
    void addSigner(AddSigner as);

    /**
     * Add signer.
     *
     * @param ass p
     */
    void addSigners(AddSigners ass);

    /**
     * Send sms.
     *
     * @param ss p
     */
    void sendSms(SendSms ss);


    /**
     * Send contract.
     *
     * @param sc p
     */
    String sendContract(SendContract sc);

    /**
     * Automatically sign with sms code review.
     *
     * @param aswv p
     */
    void autoSignWithVerification(AutoSignWithVerification aswv);

    /**
     * Automatically sign with sms code review.
     * 公证存证
     *
     * @param contractId p
     */
    void lockContract(ContractId contractId);

    /**
     * Preview contract to specified user[account][phoneNumber]
     *
     * @param pc p
     */
    String previewContract(PreviewContract pc);

    /**
     * unified notification call back handler.
     * 自行实现。
     */
    void unifiedNotifyHandler(BestSignNotifyResult bestSignNotifyResult, String sign, String rtick);
}
