package org.example.contract.client;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.example.contract.constant.MethodEnum;
import org.example.contract.pojo.PersonalRegister;
import org.example.contract.util.RSAUtils;
import org.junit.Test;

/**
 * @author: Liu Jinyun
 * @date: 2021/2/21/19:52
 */
public class HttpClientTest {
    /**
     * 开发者ID
     */
    private static final String DEVELOPER_ID = "1613804644012480439";

    /**
     * 开发者私钥
     */
    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJJ6epovD4anigkHzFEQazk7QDIO4qW//L65hEc9XdJMLDgFdA5C68u22Xr2Y2VAyoRNapXOk/Ja2zOm17eJ7pXOyOJUo0RxhvUBuJqv3juDBV0RSdYCBHDeYWWJ/t2IdH8uOwHEnBtJ52nsQ5LR4dS8z2mHDc8Knydd1sFaGC3VAgMBAAECgYBILlDSP9SPz2mnD4+wdr3PwxHcWRd5BEWhGrxDessMAXtc09sf1/xoM5+yB05tq+LgFWIrllOzfvA74MC2ciL+MA+Rt2ZUwXSPhPUppYPVWI++/O8BZ2SzJOQ3rZsraWv/IVjU2WwfLEoevSM89N+rTdKcs3dfpY/o10A5UQwuVQJBAOUKNgS3L5UgLVihQXT+Cs3MM1qBexwQnuMtNFftgWLxAcTUWwYwUulvtU02qnELxcrl4WdKZ6TFtHLikiUDl4cCQQCjuGWQjM56v0jDIDCxum1w3FusCKXN9PEM1JrUxu0NzxOfy7viB2xTfSi3i3fe3IJtrQuPTW01c31XJd2pMq7DAkEAv+oYbUcZhbkahgNIl8EuSFbsSM/p2hBLMkv0aiDGo6MSOl09kCf5sUQ7BGHj2ylkKKkKf8QmqDhJBWK31m/2DQJAFsFpdHbtuAfgQCa4PeuwjHBEWZv2F7NE0ci3IH3psvKmQp6ZpafseYo635AGADbb9gyIP8m+NSCxOiysK3k86wJADhzue6IH5mTq/Via14inulwiUD/awqKLw79IKmAL06YaomQo9SsMd9WXcZIPYJawLDV6TVk5vcdsD38mqqBG6w==";

    /**
     * 服务url
     */
    private static final String SERVER_HOST = "https://openapi.bestsign.info/openapi/v2";

    /**
     * path
     */
    private static final String URL_SIGN_PARAM = "?developerId=%s&rtick=%s&signType=rsa&sign=%s";


    @Test
    public void httpClient() {
        PersonalRegister pr = new PersonalRegister("17862328960", "刘津运",
                "1", "1", "370481199607143214");
        String jsonStr = JSONUtil.toJsonStr(pr);
        String rtick = RSAUtils.getRtick();
        String sign = RSAUtils.calcRsaSign(DEVELOPER_ID, PRIVATE_KEY, SERVER_HOST,
                MethodEnum.PERSONAL_REGISTER.getMethod(), rtick,
                null, jsonStr);
        String path = String.format(URL_SIGN_PARAM, DEVELOPER_ID, rtick, sign);

        String url = SERVER_HOST + MethodEnum.PERSONAL_REGISTER.getMethod() + path;
        HttpResponse httpResponse = HttpUtil.createPost(url).body(jsonStr, ContentType.JSON.getValue()).execute();
        if (httpResponse.getStatus() != 200) {
            System.out.println(httpResponse.body());
        } else {
            // 161391959101000002
            System.out.println();
            System.out.println(httpResponse.toString());
            System.out.println();
            String body = httpResponse.body();
            System.out.println(body);
            JSONObject userObj = JSONUtil.parseObj(body);
            if (userObj.getInt("errno") == 0) {
                JSONObject data = userObj.getJSONObject("data");
                if (data != null) {
                    System.out.println(data.getStr("taskId"));
                }
            } else {
                System.out.println(userObj.getInt("errno") + ":"
                        + userObj.getStr("errmsg"));
            }
        }
    }

}
