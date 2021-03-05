package org.example.bootrap;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.Bootstrapper;

/**
 * @author: jinyun
 * @date: 2021/3/3
 */
public class CustomBootstrapper implements Bootstrapper {
    @Override
    public void intitialize(BootstrapRegistry registry) {
        System.out.println("org.example.bootrap.CustomBootstrapper.intitialize(BootstrapRegistry registry)");
        System.out.println("Since2.4.0 Spring boot provides bootstrapper to bootstrap the BootstrapContext, but default NOP");
    }
}
