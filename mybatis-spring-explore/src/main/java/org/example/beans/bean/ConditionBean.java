package org.example.beans.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;

/**
 * @author: jinyun
 * @date: 2021/3/17
 */
@ConditionalOnMissingBean
@ConditionalOnMissingClass
@ConditionalOnBean
@ConditionalOnClass
public class ConditionBean {
}
