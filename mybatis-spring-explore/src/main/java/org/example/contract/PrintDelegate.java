package org.example.contract;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: jinyun
 * @date: 2021/2/25
 */
@Component
public class PrintDelegate implements PrintService{
    private final List<PrintService> printServices;

    public PrintDelegate(List<PrintService> list) {
        this.printServices = list;
    }

    @Override
    public void print() {
        printServices.forEach(PrintService::print);
    }
}
