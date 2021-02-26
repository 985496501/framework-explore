package cc.jinyun.contract;

import org.springframework.stereotype.Service;

/**
 * @author: jinyun
 * @date: 2021/2/25
 */
@Service
public class HiPrintServiceImpl implements PrintService{
    @Override
    public void print() {
        System.out.println("Hi HI Hi Hi Hi  Hi Hi");
    }
}
