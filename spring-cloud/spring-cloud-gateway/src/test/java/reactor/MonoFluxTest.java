package reactor;

import org.junit.Test;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: jinyun
 * @date: 2021/5/26
 */
public class MonoFluxTest {

    @Test
    public void oneTest() {
//        Flux<String> stringFlux =
                Mono.justOrEmpty("1,2,4,5").flatMapMany(item -> {
            Set<String> delimitedListToSet = StringUtils.commaDelimitedListToSet(item);
            return Flux.fromIterable(delimitedListToSet);
        }).subscribeOn(Schedulers.boundedElastic()).subscribe(System.out::println);

//        stringFlux.subscribe(System.out::println);

//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    @Test
    public void twoTest() {
        List<String> list = new ArrayList<>();
        list.add("liu");
        list.add("jin");
        list.add("yun");
        // 同步获取
        Flux<String> flux = Flux.defer(() -> Flux.fromIterable(list)).subscribeOn(Schedulers.boundedElastic());

        flux.subscribe(System.out::println);
    }




}
