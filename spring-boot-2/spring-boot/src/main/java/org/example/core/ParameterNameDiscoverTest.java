package org.example.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 *
 *
 *
 * @author: jinyun
 * @date: 2021/4/25
 */
public class ParameterNameDiscoverTest {

    /**
     * 看到同时使用的EL表达式 用来定义锁的key的获取 然后就自己搞了一下这个问题;
     */
    @Test
    public void parameterNameDiscoverTest() {
//        org.springframework.data.repository.util.ClassUtils
        Method method = ClassUtils.getMethod(MethodHolder.class, "getMethod", DedicatedParam.class);
        ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);

        SpelExpressionParser parser = new SpelExpressionParser();
        System.out.println(Arrays.toString(parameterNames));

        StandardEvaluationContext context = new StandardEvaluationContext();
        DedicatedParam param = new DedicatedParam("13213132");
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], param);
        }

        String expressionStr = "#dedicatedParam.getOrderNum()";
        Expression expression = parser.parseExpression(expressionStr);
        String value = expression.getValue(context, String.class);
        System.out.println(value);
    }


    @Test
    public void getMethodTest() {
        Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(MethodHolder.class);
//        System.out.println(declaredMethods);

    }

    @Test
    public void getMethod2Test() {
        ClassUtils.getMethod(MethodHolder.class, "getMethod", Map.class);
    }

    static class MethodHolder {
        public String getMethod(DedicatedParam dedicatedParam) {
            return "";
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class DedicatedParam {
        private String orderNum;
    }
}
