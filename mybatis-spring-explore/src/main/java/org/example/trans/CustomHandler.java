package org.example.trans;

import cc.jinyun.contract.external.AbstractSignContract;
import cc.jinyun.contract.external.UserDetail;
import org.springframework.stereotype.Service;

/**
 * @author: jinyun
 * @date: 2021/3/9
 */
@Service
public class CustomHandler extends AbstractSignContract {
    @Override
    public UserDetail getUserDetail(Long userId) {
        return new UserDetail() {
            @Override
            public String getPhoneNumber() {
                return "17862328960";
            }

            @Override
            public String getUserName() {
                return "刘津运";
            }

            @Override
            public String getIdNumber() {
                return "370481199607143214";
            }
        };
    }


    @Override
    public boolean hasRegistered(Long userId) {
        return false;
    }


}
