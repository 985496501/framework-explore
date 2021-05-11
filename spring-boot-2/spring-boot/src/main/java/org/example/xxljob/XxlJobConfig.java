package org.example.xxljob;

import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.client.ExecutorBizClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.glue.GlueTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

/**
 * 主要定义执行器
 *
 *
 * @author: jinyun
 * @date: 2021/5/10
 */
//@Configuration
public class XxlJobConfig {

    private final static String adminAddresses = "http://127.0.0.1:8080/xxl-job-admin";
    private final static String appname = "entryApplication";
    private final static String accessToken = "123465";


    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress("");
        xxlJobSpringExecutor.setIp("");
        xxlJobSpringExecutor.setPort(7080);
        xxlJobSpringExecutor.setAccessToken("123456");
        xxlJobSpringExecutor.setLogPath("");
        xxlJobSpringExecutor.setLogRetentionDays(0);
        return xxlJobSpringExecutor;
    }


    @Test
    public void run(){
        ExecutorBiz executorBiz = new ExecutorBizClient(adminAddresses, accessToken);

        // trigger data
        final TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(1);
        triggerParam.setExecutorHandler("simpleXxlJobHandler");
        triggerParam.setExecutorParams("23135165132132132123");
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setGlueType(GlueTypeEnum.BEAN.name());
        triggerParam.setGlueSource(null);
        triggerParam.setGlueUpdatetime(System.currentTimeMillis());

        triggerParam.setLogId(1);
        triggerParam.setLogDateTime(System.currentTimeMillis());

        // Act
        final ReturnT<String> retval = executorBiz.run(triggerParam);

        // Assert result
        Assertions.assertNotNull(retval);
    }

}
