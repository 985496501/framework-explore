package org.example.contract.api;

import org.example.contract.annotation.HttpDelegate;
import org.example.contract.annotation.Post;
import org.example.contract.client.HttpClient;
import org.example.contract.pojo.*;

/**
 * 直接使用这个接口即可
 * 即使不需要
 * 1.可插拔的注解支持，包括Feign注解和JAX-RS注解。
 * 2.支持可插拔的HTTP编码器和解码器（Gson，Jackson，Sax，JAXB，JAX-RS，SOAP）。
 * 3.支持Hystrix和它的Fallback。
 * 4.支持Ribbon的负载均衡。
 * 5.支持HTTP请求和响应的压缩。
 * 6.灵活的配置：基于 name 粒度进行配置
 * 7.支持多种客户端：JDK URLConnection、apache httpclient、okhttp，ribbon）
 * 8.支持日志
 * 9.支持错误重试
 * 10.url支持占位符
 * 11.可以不依赖注册中心独立运行
 */
@HttpDelegate(delegate = HttpClient.class)
public interface ContractApi {

    /**
     * Apply personal or enterprise signature.
     *
     * @param pr p
     * @return r  taskId
     */
    @Post("/user/reg/")
    String register(Register pr);

    /**
     * Apply enterprise signature.
     *
     * @param pr p
     * @return r  taskId
     */
    @Post("/user/reg/")
    String enterpriseRegister(Register pr);

    /**
     * Query signature status.
     *
     * @param qr p
     * @return s
     * @see QueryRegister.RegisterEnum
     */
    @Post("/user/async/applyCert/status/")
    boolean queryRegister(QueryRegister qr);

    /**
     * Create personal signature.
     *
     * @param cs p
     */
    @Post("/signatureImage/user/create/")
    void createPersonalSignature(CreateSignature cs);

    /**
     * Create enterprise signature.
     *
     * @param cs p
     */
    @Post("/dist/signatureImage/ent/create/")
    void createEnterpriseSignature(CreateSignature cs);

    /**
     * Upload and create a contract.
     *
     * @param uc p
     * @return contract id
     */
    @Post("/storage/contract/upload/")
    String uploadAndCreateContract(UploadContract uc);

    /**
     * Add signer.
     *
     * @param as p
     */
    @Post("/storage/contract/upload/")
    void addSigner(AddSigner as);

    /**
     * Add signer.
     *
     * @param ass p
     */
    @Post("/storage/contract/upload/")
    void addSigners(AddSigners ass);

    /**
     * Send sms.
     *
     * @param ss p
     */
    @Post("/contract/sendSignVCode/")
    void sendSms(SendSms ss);

    /**
     * Automatically sign with sms code review.
     *
     * @param aswv p
     */
    @Post("/storage/contract/sign/cert2/")
    void autoSignWithVerification(AutoSignWithVerification aswv);

    /**
     * Automatically sign with sms code review.
     * 公证存证
     *
     * @param contractId p
     */
    @Post("/storage/contract/lock/")
    void lockContract(ContractId contractId);

    /**
     * Preview contract to specified user[account][phoneNumber]
     *
     * @param pc p
     */
    @Post("/contract/getPreviewURL/")
    String previewContract(PreviewContract pc);
}
