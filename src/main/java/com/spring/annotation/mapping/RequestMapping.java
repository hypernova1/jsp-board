package com.spring.annotation.mapping;

import com.spring.constant.HttpMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface RequestMapping {

    String value() default "";
    HttpMethod method() default HttpMethod.GET;

}
