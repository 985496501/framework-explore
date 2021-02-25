package org.example;

import cn.hutool.json.JSONUtil;
import org.example.client.RadicalClient;
import org.example.contract.PrintDelegate;
import org.example.contract.api.ContractActionCallback;
import org.example.contract.api.ContractApi;
import org.example.contract.pojo.reponse.NotifyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.servlet.http.HttpServletRequest;

/**
 * SpringBoot Entry.
 *
 *
 *
 * @author: jinyun
 * @date: 2021/2/8
 */

@EnableWebSocket
@RestController
@SpringBootApplication
public class EntryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryApplication.class, args);
    }

    @Autowired
    private RadicalClient radicalClient;
    @Autowired
    ContractActionCallback contractActionCallback;

    @Autowired
    ContractApi contractApi;

    @Autowired
    PrintDelegate printDelegate;

    @GetMapping
    public String getMsg() {
        return radicalClient.exchange();
    }


    @PostMapping("callback")
    public void signContractCallback(HttpServletRequest request, @RequestBody NotifyResult notifyResult) {
        System.out.println(request.getHeader("rtick"));
        System.out.println(request.getHeader("sign"));
        System.out.println(JSONUtil.toJsonPrettyStr(notifyResult));

        contractApi.unifiedNotifyHandler(notifyResult, "", "");
    }
}
