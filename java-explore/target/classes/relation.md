###### 类与类的关系
1. 依赖关系 Dependency.
就是一个的改变影响了另外的一个类  用带**虚线的箭头**指向这个被影响的类
A类是B类的成员变量 或者 一个方法的参数  或者 B可以向A发送通知改变A
就说B依赖了A
@Bean
   
2. 关联关系 Association.
两个相对独立的类 但是 一个类和另外一个类的实例存在特定的某种关系的时候 用实线箭头指向被知道的对象
@Import   

