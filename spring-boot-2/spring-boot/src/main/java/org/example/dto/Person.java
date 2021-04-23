package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.Serializable;

/**
 * @author: jinyun
 * @date: 2021/4/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable, ZSetOperations.TypedTuple {
    private String name;
    private Integer age;
    private String job;

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public Double getScore() {
        return Double.valueOf(age);
    }

    /**
     * 负数，说明 0小
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Person) {
            Person p = (Person) o;
            return this.age - p.age;
        }

        return 0;
    }
}
