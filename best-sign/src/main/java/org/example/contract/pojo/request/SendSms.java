package org.example.contract.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendSms {
    private String contractId;
    private String account;

    /**
     * 验证码发送目标手机号码或者目标邮箱
     * 设置此项后，会覆盖掉【设置合同签署参数】接口或【手动签】接口设置vcodeMobile或vcodeMail参数
     */
    private String sendTarget;

    /**
     * 目前支持：sms（手机短信验证码）， voiceSms（手机语音短信验证码），mail（邮箱验证码）
     */
    private String sendType;
}
