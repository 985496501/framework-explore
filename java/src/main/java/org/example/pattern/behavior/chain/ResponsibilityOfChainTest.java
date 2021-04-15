package org.example.pattern.behavior.chain;

/**
 * responsibility of chain: 铁索连环
 * <ul>
 *     <li>一个链 上面的每个节点都有自己独有的功能</li>
 *     <li>考虑到 我们定义这个联调的功能, 比如：
 *          <ol>
 *              <li>权限过滤: 从头结点开始, 顺序向后执行每个节点, 但凡有一个节点没有通过就跳出整个链</li>
 *              <li>资源修改: 从头开始对原始的对象进行修改</li>
 *              <li>责任划分: 可能有一个匹配规则, 只有匹配上的才会进行逻辑, 没有匹配上的就流转给下个节点</li>
 *          </ol>
 *     </li>
 *     <li>
 *         节点的执行数量:
 *         <ol>
 *             <li>所有节点无论如何都执行一遍, 每个节点担任一部分责任</li>
 *             <li>有且仅有一个节点执行, 从头开始只要有一个节点处理了(有相应的匹配规则), 就结束整个联调</li>
 *             <li>有不确定的节点执行, 有匹配规则, 符合匹配规则的进行逻辑处理</li>
 *         </ol>
 *     </li>
 *     <li>
 *         逻辑结果的实现:
 *         <ol>
 *             <li>使用数组</li>
 *             <li>使用链表</li>
 *             <li>更加复杂的情况可以使用 数组 + 链表, 数组是最外层的链, 然后每个单位又是一个 链表的头节点构成了一整个链条</li>
 *         </ol>
 *     </li>
 * </ul>
 *
 * <pre>
 *      +------+  prev +-----+       +-----+
 * head |      | <---- |     | <---- |     |  tail
 *      +------+       +-----+       +-----+
 * </pre>
 * @author: jinyun
 * @date: 2021/3/3
 */
public class ResponsibilityOfChainTest {

    /**
     * Node also alias for filter, interceptor...
     *
     * 具体的使用 Spring MVC, springcloud-gateway
     */
    public interface Node {

    }





}
