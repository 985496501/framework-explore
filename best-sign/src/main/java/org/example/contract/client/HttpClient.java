package org.example.contract.client;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.contract.constant.MethodEnum;
import org.example.contract.util.RSAUtils;


/**
 * PersonalRegister pr = new PersonalRegister("17862328960", "刘津运",
 * "1", "1", "370481199607143214");
 * <p>
 * 161391959101000002
 */
@Slf4j
public class HttpClient {

    private static final String SUCCESS = "errno";

    private final String developerId;

    private final String privateKey;

    private final String serverHost;

    private static final String URL_SIGN_PARAM = "?developerId=%s&rtick=%s&signType=rsa&sign=%s";

    public HttpClient(String developerId, String privateKey, String serverHost) {
        this.developerId = developerId;
        this.privateKey = privateKey;
        this.serverHost = serverHost;
    }

    public <T> T post(MethodEnum methodEnum, String jsonStr, Class<T> resultType) {
        String rtick = RSAUtils.getRtick();
        String sign = RSAUtils.calcRsaSign(developerId, privateKey, serverHost,
                MethodEnum.PERSONAL_REGISTER.getMethod(), rtick,
                null, jsonStr);
        String path = String.format(URL_SIGN_PARAM, developerId, rtick, sign);
        String url = serverHost + MethodEnum.PERSONAL_REGISTER.getMethod() + path;
        HttpResponse httpResponse = HttpUtil.createPost(url).body(jsonStr, ContentType.JSON.getValue()).execute();
        String body = httpResponse.body();
        if (httpResponse.getStatus() != HttpStatus.HTTP_OK) {
            log.error(body);
            throw new RuntimeException(body);
        } else {
            JSONObject userObj = JSONUtil.parseObj(body);
            // 第三方的沙雕结构
            if (userObj.getInt(SUCCESS) == 0) {
                JSONObject data = userObj.getJSONObject("data");
                if (data != null) {
                    return JSONUtil.toBean(data, resultType);
                }
            } else {
                log.error(body);
                throw new RuntimeException(body);
            }
        }

        log.error(body);
        throw new RuntimeException(body);
    }

}
