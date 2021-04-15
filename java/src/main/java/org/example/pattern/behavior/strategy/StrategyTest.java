package org.example.pattern.behavior.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式的实现
 *
 * @author: jinyun
 * @date: 2021/3/1
 */
public class StrategyTest {

    interface IParameter {

    }

    interface IChosenParam extends IParameter {
        int getStrategy();
    }

    interface IStrategy {
        void chooseOneDoSth(IParameter parameter);
    }

    static class FirstStrategy implements IStrategy {
        @Override
        public void chooseOneDoSth(IParameter parameter) {
            // todo；
        }
    }

    static class SecondStrategy implements IStrategy {
        @Override
        public void chooseOneDoSth(IParameter parameter) {
            // todo；
        }
    }


    static class StrategyContext {
        Map<Integer, IStrategy> map = new HashMap<>();

        void chooseOneDoSth(IChosenParam chosenParam) {
            map.get(chosenParam.getStrategy()).chooseOneDoSth(chosenParam);
        }
    }
}

