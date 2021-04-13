package org.example.bootrap.environment;

import org.example.EntryApplication;
import org.junit.Test;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Environment: 是对变量的抽象，
 * offers a convenient service.
 *
 * system environment variables, JVM system properties, configuration files, external configuration registry, command line parameters.
 *
 * @author: jinyun
 * @date: 2021/3/4
 */
public class EnvironmentTest {

    @Test
    public void mutablePropertySourcesTest() {

    }

    @Test
    public void scanTest() {
        AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(EntryApplication.class);
        annotationMetadata.getAnnotationTypes().forEach(System.out::println);
        System.out.println(annotationMetadata.hasAnnotation("org.springframework.boot.autoconfigure.SpringBootApplication"));
    }
}
