package spring.annotation.mapping;

import spring.constant.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RequestMapping(method = HttpMethod.PUT)
public @interface PutMapping {

    String value() default "";

    HttpMethod method() default HttpMethod.PUT;

}
