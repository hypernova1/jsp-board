package com.spring.exception;

public class BeanNotFoundException extends RuntimeException {
    public BeanNotFoundException(String beanName) {
        super("bean not found - beanName: " + beanName);
    }
}
