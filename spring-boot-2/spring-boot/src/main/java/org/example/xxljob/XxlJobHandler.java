package org.example.xxljob;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * @author: jinyun
 * @date: 2021/5/10
 */
//@Component
public class XxlJobHandler {


    /**
     * value: 自定义jobHandler的名称
     */
    @XxlJob(value = "simpleXxlJobHandler")
    public void simpleXxlJobHandler() {
        try {
            String jobParam = XxlJobHelper.getJobParam();
            System.out.println(jobParam);


            // default success;
            XxlJobHelper.handleSuccess();
        } catch (Exception e){
            XxlJobHelper.handleFail();
        }
    }


}
