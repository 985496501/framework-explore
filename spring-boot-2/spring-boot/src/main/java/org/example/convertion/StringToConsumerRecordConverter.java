package org.example.convertion;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.checkerframework.checker.units.qual.C;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * @author: jinyun
 * @date: 2021/4/22
 */
@Component
public class StringToConsumerRecordConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, ConsumerRecord.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }

        String sourceStr = (String) source;
        JSONObject jsonObject = JSONObject.parseObject(sourceStr);
        String name = jsonObject.getString("name");
        return new ConsumerRecord<>("", 1, 1, "data", jsonObject);
    }
}
