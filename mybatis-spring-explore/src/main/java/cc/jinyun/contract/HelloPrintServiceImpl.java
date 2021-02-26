package cc.jinyun.contract;

import org.springframework.stereotype.Service;

/**
 * @author: jinyun
 * @date: 2021/2/25
 */
@Service
public class HelloPrintServiceImpl implements PrintService{
    @Override
    public void print() {
        System.out.println("hello hello hello hello ");
    }
}
