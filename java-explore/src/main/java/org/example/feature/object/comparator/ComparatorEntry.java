package org.example.feature.object.comparator;

import java.util.Arrays;

/**
 * comparable: 直接用比较的类 实现这个接口即可 那么就是数据和相应的逻辑整合在一起
 *
 * comparator: 是一个专用的比较器 实现了数据和逻辑分离
 *
 *
 * @author: jinyun
 * @date: 2021/2/24
 */
public class ComparatorEntry {


    static class SortedOne implements Comparable<SortedOne> {
        private Integer i;

        public SortedOne(Integer i) {
            this.i = i;
        }

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        @Override
        public String toString() {
            return "SortedOne{" +
                    "i=" + i +
                    '}';
        }

        /**
         * 定义这个对象和传入对象的次序问题
         * int = 0
         * int > 0
         * int < 0
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(SortedOne o) {
            return this.i - o.i;
        }
    }


    public static void main(String[] args) {
        SortedOne one = new SortedOne(1);
        SortedOne two = new SortedOne(2);
        SortedOne minus = new SortedOne(-2);

        SortedOne[] arr = new SortedOne[]{one, two, minus};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
    }




}
