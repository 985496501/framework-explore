package org.example.beans.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.asm.ClassReader;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Facility for AbstractBeanFactory... 这个类专门用于服务这个抽象Bean工厂的;
 * {@link org.springframework.beans.factory.support.AbstractBeanFactory}
 * <p>
 * 这个委派类真了不得, 专门用于处理 PostProcessor;  它维护了 BeanPostProcessor;
 *
 * @author: jinyun
 * @date: 2021/4/20
 */
@Slf4j
public class PostProcessorRegistrationDelegateTest {

    /**
     * 首先执行 bean定义中心 后置处理器
     * see {@link ConfigurationClassPostProcessor} 是 BeanDefinitionRegistryPostProcessor
     * 的直接子类, 同时 也是BeanFactoryPostProcessor的实现类;
     */
    @Test
    public void invokeBeanDefinitionRegistryPostProcessorsTest() {
        DefaultListableBeanFactory beanDefinitionRegistry = new DefaultListableBeanFactory();

        // 手动创建一个BeanDefinition
        BeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(Configuraer.class).getBeanDefinition();
        // 生成bean的名字
        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, beanDefinitionRegistry);
        // 这个对象里面就有beanDefinitionMap;
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);

        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();

        // 上面这个方法主要就是执行这个方法：
        // postProcessor.postProcessBeanDefinitionRegistry(beanDefinitionRegistry);
        // 实际就是执行下面这个方法。 build and validate a class annotated @Configuration
        // 处理注解配置的 bean 定义;
        postProcessor.processConfigBeanDefinitions(beanDefinitionRegistry);
        // 看下个这个处理配置的bean定义究竟做了些啥  注意下面的所有操作都是基于 @Configuration 注解标识的;
        // ==>  这个类又使用了一个Util,  ConfigurationUtils.checkConfigurationClassCandidate() 内部类不让用;
        //      这个方法的目的就是检查是不是被@Configuration加上, 然后根据这个proxyMode=?true
        //      在beanDefinition加入 attribute <String, String>
        //      ConfigurationClassPostProcessor.configurationClass, full || lite
        //      如果有order注解 也会设置属性 ConfigurationClassPostProcessor.order = x

        // 然后封装成bfHolder, 通过order sort
        // 解析每个@Configuration的class 了这要 引入了一个类： ConfigurationClassParser 配置类解析器
        // ConfigurationClassParser.processConfigurationClass(ConfigurationClass, filter)

        // 这里又一个重点：递归处理 配置类 和 它的父类; sourceClass = asSourceClass(ConfigurationClass, SourceClass)
        // doProcessConfigurationClass(ConfigurationClass, SourceClass, filter)
        // 1 todo: 递归解析 内部成员类 主要标识@Component
        // 2 @PropertySource.class 这个没怎么用到过先不看
        // 3 @ComponentScan 主要是解析 Scan Scans 的value定义的属性 主要就是获取classpath的相对路径 先不看
        // 4 @Import 这个注解应该看一下, 我们经常在源码中看到 @Import(xx.class) @Configuration
        // ==>  ConfigurationClassParser.processImports(ConfigurationClass, SourceClass, Collection<SourceClass>, Predicate)
        //      Set<SourceClass> getImports() 把import类引入class获取然后
        //      判断 isAssignalbe(ImportSelector.class) 这个先不看 好像也遇到过再说
        //      然后在看 isAssignable(ImportBeanDefinitionRegistrar.class) 嗯这个常用这个是 可以import一个然后批量注入beanDefinitions
        //      最后把默认的 bean定义注册进去;
        // 5 @ImportResource 没怎么遇到过先不看
        // 6 @Bean 这个又是重中之重了 这个是在beanDefinition层面所以没有做多少事 仅仅是封装了BeanMethod对象往ConfigurationClass里面一塞
        //   每个@Configuration的配置的class对象都会封装成 ConfigurationClass 对象

    }


    /**
     * 这个类就是专门用来解析 @Configuration
     *
     * 并且创建这个对象的需要传入(MetadataReaderFactory) 它可以生产reader, 这个reader使用asm算法
     * 来load类, 在jvm深层解析class.  这个ASM实现避免了大量的反射 和 热类加载, 为了有效的处理 lazy class;
     *
     * todo: 这里还有一个类 ConditionEvaluator 这个类用于前置判断 @Conditional
     */
    @Test
    public void configurationClassParserTest() {

    }





    // --------如下内容 复习一下简单的IO 和 类加载---------------------------------------

    /**
     * <h3>元数据读取器</h3>
     * 这个类就是负责加载原始资源的, 通过 ClassLoader 加载Resource
     * 然后分别解析成 3个大类接口：  Resource, ClassMetadata, AnnotationMetadata;
     * 有个专门的工厂 MetadataReaderFactory 可以 创建我们的 MetadataReader;
     *
     * see {@link MetadataReader} 解析是通过asm加载字节码文件进行加载的; 每一个class文件就对应一个
     * metadataReader. 如果已经有的就不用在加载了, 所以需要进行缓存设计  map<name, MetadataReader>
     *     see {@link ConcurrentReferenceHashMap} soft or weak 包装k v
     * 但是最终通过ClassLoader进行加载的.
     *
     * <ul>
     *     <li>{@link Resource}</li>
     *     <li>{@link ClassMetadata}</li>
     *     <li>{@link AnnotationMetadata}</li>
     * </ul>
     *
     * see {@link MetadataReaderFactory}
     *
     *
     */
    @Test
    public void MetadataReaderTest() {

    }

    /**
     * 这个是 类读取器； 这个是Spring封装的 asm 读取类
     */
    @Test
    public void classReaderTest() throws IOException {
        ClassReader classReader = new ClassReader("org.example.beans.bean.register.DefaultBeanLifecycle");
        // 通过类加载器 加载ClassPath下的class文件; 通过classLoader获取读取的能力; URL.openConnection().getInputstream();
        // InputStream inputStream = ClassLoader.getSystemResourceAsStream(className.replace(".", "/") + ".class", true)
        // readStream(inputStream, autoClosable): 读取最终的byte[] 关闭流资源
    }

    // java IO的设计就是用各种模板方法 和 包装设计模式, 一方面定义了操作流程, 使用顶级接口就可以完成真实对象的操作 便利于抽象方法的定义
    // 同时使用了 注入的方式 对一些方法进行了增强

    /**
     * see {@link ByteArrayInputStream} 这个就是把byte[] 转成我们需要的输入流
     * see {@link ByteArrayOutputStream} 这个是把byte[] 一些写到它内置的byte[]里面, 也是把byte[]转成outputStream;
     * <p>
     * 这两个对象都是面向内存进行输入输出, 不会出现未关闭资源的情况
     * <p>
     * <p>
     * 这个对象有什么用呢? 我们
     * <p>
     * byteArrayOutputStream:
     */
    @Test
    public void byteArrayInAndOutputStreamTest() throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // 在这个输出流里创建一个存储 byte[];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        // write(byte[]) 就相当于把传入的这个byte[] 读取到内置的那个byte[]
        byteArrayOutputStream.write(bytes); // 往内存中写其实没多大问题, 就是往文件系统 或者 socket缓冲区里面写会出现IO异常

        // 一次性写出, 但是相对于内存来说没有任何操作
        byteArrayOutputStream.flush();

        byte[] bytes1 = byteArrayOutputStream.toByteArray();
        // byte1的数据就是写入的数据;
    }


    /**
     * 我们都知道这个是读取流
     * 把文件系统中的文件加载到内存中 都是byte[]
     * 我们看下源代码的具体实现;
     * 这个是File的封装, 基于bytes读取, 那么也就是可以读取 图片等
     * see {@link FileInputStream}
     * compare {@link java.io.FileReader}
     * compare {@link java.net.SocketInputStream} not public;
     * compare {@link ByteArrayInputStream}: 把字节数组转成一个输入流, 这个字节数组就是暴露需要读取的数据, 相当于对于内存读取；用于类型;
     * compare {@link ByteArrayOutputStream}: 这个可以随着写入自动扩容;
     */
    @Test
    public void inputStreamTest() throws IOException {
        // 通过osPath先File 获取Fd, 看看这个文件有没有读取权限 然后创建一个Fd, 调用了本地方法 open0() 让JVM调用系统内核,
        // 就是因为这个系统连接 所以需要close, 最好使用try(Resource [Closable]) { 编译器可以帮我们在执行完 然后再调用reource.close }
        try (InputStream fileInputStream = new FileInputStream("E:\\workspace\\1.txt")) {
            // 打开文件就可以看到这个字符编码集： UTF-8, 微软默认txt就是UTF-8 我没找到可以修改编码的入口
            // 调用本地方法read0() 就是每次读取一个byte, 8bit. 有符号数：0111 1111 = 2^7 - 1 = 255, 1byte = 0-255, -1代表空了.
            // byte 字节, java最小的单位了; 封装了硬件了8个二进制位
            // 同一个inputStream每次调用read() 就会自动推移index;  int read = inputStream.read(), 单个字节读取

            // 批量读bytes, 先获取总的可以获取byte的数量 一个英文和标点是一个， 一个汉字UTF-8编码占3个
            // 这个方法注释一定看好, 源码: It's never correct to use this value of this method to allocate buffer intended to
            // hold all data in this stream.
            int available = fileInputStream.available();
            System.out.println("可读字节数大约有： " + available);
            // 把文件系统的bytes读取到  JVM堆内存(主存) 同时copy一份到工作内存;
            // 批量直接把文件系统的bytes, 通过我们的byte[] 进行 batchRead() 到我们的byte[]中;
            // 因为我们最终需要的资源对象byte[], 输入流仅仅是一个工具;
//            byte[] bytes = new byte[available];
//            fileInputStream.read(bytes, 0, available);

            // 正确使用的方法 32 默认是32, 我们一写一扩容
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8);
            byte[] temp = new byte[8];
            int len;
            while ((len = fileInputStream.read(temp)) != -1) {  // read(byte[]) == read(byte[], 0, len)
                byteArrayOutputStream.write(temp, 0, len);
            }

            // 所有写满byte[] 之后最好调用一下flush() 虽然这里没啥作用 但是也要调用一下
            byteArrayOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            // -----------------byte[] 转换成 Inputstream --------------------------------------------------------
            // 这个就是把 我们源数据 byte[] 转换成了 输入流对象, 供其他读取 面向内存读取;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            byte[] dump = new byte[byteArrayInputStream.available()];
            byteArrayInputStream.read(dump); // 最终都是一个一个的copy
            System.out.println(new String(dump));
            // -----------------byte[] 转换成 Inputstream ---------------------------------------------------------

            System.out.println(new String(bytes, StandardCharsets.UTF_8));
        }
    }

    /**
     * reader专门用来读取 character;
     * <p>
     * see {@link FileReader}:  Convenience class for reading characters;
     * 出来提供一个默认的构造方法, 其实可以直接使用 InputStreamReader(new FileInputStream(""))
     */
    @Test
    public void fileReaderTest() {
        // 这不就是包装设计模式了哦 使用底层工作对象FileInputStream它是最基层的工作对象负责从OS读取bytes, 然后通过reader, 进行编解码转成char
        // 可以指定字符编码集，可以把读取到的 bytes ==> characters ==> 直接就可以获取 string
        // FileReader fileReader = new FileReader("E:\\workspace\\1.txt") 这个也是一样的渐变方法;
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("E:\\workspace\\1.txt"),
                StandardCharsets.UTF_8)) {
            // 我们不知道char的大小啊 只能自己看着写了 len读取的char个数 可以存汉字，具体取决于 charset;
            char[] chars = new char[4]; // 每次读取4个字符

            // 我们把读取的数据通过这个对象存入这个输出流的byte[8]里面 这个值 默认是32bytes 这是一个经验值; 不够没关系可以自动扩容
            // see ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8);
            CharArrayWriter charArrayWriter = new CharArrayWriter(); // 也是32
            int len;
            // 我们通过一个中间byte[] 完成copy 这个byte主要就是我们不知道这个读取的值有多少, 只能用一个业务经验值，不断循环copy到我们的最终的byte[]
            while ((len = inputStreamReader.read(chars)) != -1) {
                charArrayWriter.write(chars, 0, len); // 相当于把读到的数组一直append到我们最重的数组上;
            }

            System.out.println(charArrayWriter.toCharArray()); // 一把获取我们需要的chars
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * new File("systemObjectPath") 负责与OS的文件系统建立连接, 封装了访问权限
     * 它里面有一个平台相关的 File System. 通过这个文件系统 规范化 这个path,
     * <p>
     * File: 提供了这个文件的属性访问能力;
     *
     * @throws IOException
     */
    @Test
    public void fileTest() throws IOException {
        // E:\workspace\1.txt  \转义字符
        File file = new File("E:\\workspace\\1.txt");

        boolean isFile = file.isFile();
        boolean canRead = file.canRead();
        boolean canWrite = file.canWrite();
        String name = file.getName();
        long totalSpace = file.getTotalSpace();
        String absolutePath = file.getAbsolutePath();
        String canonicalPath = file.getCanonicalPath();

        log.info("\nisFile: {}, \nwrite: {}, \nread: {}, \nname: {}, \ntotalSpace: {}, \nabsolutePath: {}, \nnanonicalPath: {}",
                isFile, canRead, canWrite, name, totalSpace, absolutePath, canonicalPath);
        //isFile: true,
        //write: true,
        //read: true,
        //name: 1.txt,
        //totalSpace: 104857595904,
        //absolutePath: E:\workspace\1.txt,
        //nanonicalPath: E:\workspace\1.txt
    }

}
