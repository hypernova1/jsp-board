package com.board;

import com.spring.annotation.component.Bean;
import com.spring.annotation.component.Component;

@Component
public class BeanSuit {

    @Bean
    public String hello() {
        return "hello, world!";
    }

}
