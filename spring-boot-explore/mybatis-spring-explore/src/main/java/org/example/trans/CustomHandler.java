package org.example.trans;

import cc.jinyun.contract.external.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *     contract-template-list:
 *       - index: 0
 *         tid: 161500019701000001
 *         title: 货主年签合同
 *         variables:
 *           - variable:
 *               -
 *       - index: 1
 *         tid: 161500019701000001
 *         title: 司机年签合同
 *       - index: 2
 *         tid: 161542926301000001
 *         title: 委托代收协议
 */
@Service
public class CustomHandler extends BaseSignBestSignContractAdapter implements BestSignUserDetailService, BestSignContractTemplateService {

    @Override
    public TemplateDetail getContractDetail(String tid, Map<String, Object> extras) {
        return new TemplateDetail() {
            @Override
            public String getTid() {
                return "161500019701000001";
            }

            @Override
            public String getTitle() {
                return "货主年签合同";
            }

            @Override
            public Map<String, String> getVariablesOfPlaceHolder() {
                Map<String, String> map = new HashMap<>(4);
                map.put("userName", "金云柳");
                map.put("companyName", "谷歌科技公司");
                return map;
            }

            @Override
            public List<List<String>> getVariablesOfCharter() {
                List<String> list = Arrays.asList("company", "companyDate");
                List<String> list2 = Arrays.asList("user", "userDate");
                return Arrays.asList(list, list2);
            }
        };
    }

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
    public boolean registered(Long userId) {
        return false;
    }

}
