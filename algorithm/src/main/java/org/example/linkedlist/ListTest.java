package org.example.linkedlist;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: jinyun
 * @date: 2021/4/21
 */
public class ListTest {
    public static void main(String[] args) {


        List<Integer> linkedList = new LinkedList<>();
    }


    @Test
    public void arrayListTest() {
        // 把底层的 Object[] elementData = {}      size = 0
        // 如果传入 initialCapacity 那么就默认分配 Object[10] elementData
        // 所以构造方法就是创建一个 元素数据的 数组
        List<Integer> arrayList = new ArrayList<>(10);

        // add() 这个方法 ensureCapacityInternal(size + 1)
        // ==> calculateCapacity([], minCapacity)
        //      计算容量这个操作就是 容器数据 和  最小容量判断, 如果是默认构造{} 就返回两者最大的也就是minCapacity
        //      计算最小的容量： DEFAULT_CAPACITY=10 minCapacity 如果不是{} 就返回minCapacity=size+1
        // element[size++] = e  size在这个索引位置设置值 然后size++
        arrayList.add(1);
    }
}
