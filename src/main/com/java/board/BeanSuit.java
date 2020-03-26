package java.board;

import spring.annotation.component.Bean;
import spring.annotation.component.Component;

@Component
public class BeanSuit {

    @Bean
    public String hello() {
        return "hello, world!";
    }

}
