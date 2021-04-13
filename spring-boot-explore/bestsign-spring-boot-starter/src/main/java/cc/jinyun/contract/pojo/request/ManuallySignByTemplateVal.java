package cc.jinyun.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManuallySignByTemplateVal {
    private String contractId;
    private String tid;

    private String varNames; // 模板变量值，可以多个
    private String signer;

    /**
     * 手动签署时是否手绘签名。取值0/1/2。
     * 0点击签名图片不会触发手写面板（禁止手写）。
     * 1点击签名图片能触发手写面板（既可手写也可使用默认签名）；
     * 2强制必须手绘签名（只能手写不允许使用默认签名）
     */
    private String isDrawSignatureImage;

    /**
     * 签名/印章图片。取值default或者指定的印章名称。（手动签都用默认的）
     */
    private String signatureImageName;

    private String pushUrl;
}
