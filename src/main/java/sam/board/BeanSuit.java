package sam.board;

import sam.spring.annotation.component.Bean;
import sam.spring.annotation.component.Component;

@Component
public class BeanSuit {

    @Bean
    public String hello() {
        return "hello, world!";
    }

}
