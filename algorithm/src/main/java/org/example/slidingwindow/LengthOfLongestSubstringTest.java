package org.example.slidingwindow;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 滑动窗口的例题1：
 * <p>
 * 求字符串的最大子串。 字符串就是一个有序的字符队列一样。
 * <p>Longest length of substring without repeatable characters.
 *
 * [a, b) 左闭右开的区间进行滑动,  b-a+1
 *
 * <p>
 * <p>
 * Step:
 * <p>1. [a,b) 左开右闭, 每次使用b判断是否在[a,b)存在
 * <p>2. 如果存在是否属于[a,b), 如果在区间内就挤掉它
 * <p>3. 循环n次, 每次都进行最长子串的比较, 直到最后一次计算出最长的子串长度
 *
 * @author: jinyun
 * @date: 2021/3/1
 */
public class LengthOfLongestSubstringTest {
    // 没有特殊说明 都是看 算法的时间复杂度 可以采用其他的数据结构

    @Test
    public void lengthOfLongestSubstringTest() {
        // 自己的想法就是双指针的操作
        String str = "pwwkew";
        String str1 = "bbbbb";
        String str2 = "abba";
//        System.out.println(rowLengthOfLongestSubstring(str));
        System.out.println(rowLengthOfLongestSubstring(str));
        System.out.println(rowLengthOfLongestSubstring(str1));
        System.out.println(rowLengthOfLongestSubstring(str2));
    }

    /**
     * 使用最长的不重复子串的长度
     *
     * @param s source string
     * @return l
     */
    private int optimizedLengthOfLongestSubstring(String s) {
        if (s == null) return 0;
        int longest = 0;
        int left = 0;
        Map<Character, Integer> map = new HashMap<>(s.length());
        for (int i = 0; i < s.length(); i++) {
            // if any. i represents excluded.
            Integer index = map.get(s.charAt(i));
            // mark left. if a exists, get(a) is not in limitation of [left, right)
            if (index != null && index >= left) left = index + 1;
            // override index.
            map.put(s.charAt(i), i);
            // per loop compute.
            longest = Math.max(i - left + 1, longest);
        }

        return longest;
    }


    /**
     * bug.
     *
     * @param s
     * @return
     */
    private int rowLengthOfLongestSubstring(String s) {
        if (s == null) return 0;
        if (s.length() == 1) return 1;

        int longest = 0;
        int left = 0, right = 0;
        // tail ---> [head, tail) tail是否包含啊 包含的索引是什么？
        while (right < s.length()) {
            // right moving. 要酌情右移 str.substring() --> new str.
            // 这里需要查找 在子串中这个字符的坐标 这个能不能优化呢?
            // Ask for HashMap to optimize this loop search.
            int index = getIndexOfSpecifiedSection(s, left, right);
            int difference;
            if (index == -1) {
                // [a, c)
                difference = right - left + 1;
                right++;
            } else {
                left = index + 1;
                difference = right - left + 1;
            }

            longest = Math.max(difference, longest);
        }

        return longest;
    }


    /**
     * Search for c in this specified source string --> [head, tail).
     *
     * @param str  the source str.
     * @param head the head of specified source string, included.
     * @param tail the tail of specified source string, excluded.
     *             <p>@param c target character.
     * @return <p>-1: not searching.
     * <p>n(n != -1): the index of the target character of the specified source string.
     */
    private int getIndexOfSpecifiedSection(String str, int head, int tail) {
        char c = str.charAt(tail);
        for (int i = head; i < tail; i++) {
            if (str.charAt(i) == c) {
                return i;
            }
        }

        return -1;
    }

    @Test
    public void stringTest() {
        String str = "pwwkew";
        String substring = str.substring(0, 1);
        System.out.println(substring);
        char c = str.charAt(1);
        System.out.println(c);

        int i = substring.indexOf(c);
        System.out.println(i);

        int w = getIndexOfSpecifiedSection(str, 0, 2);
        System.out.println(w);
    }
}
