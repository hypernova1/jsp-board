package board.annotation.mapping;

import board.constant.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RequestMapping(method = HttpMethod.GET)
public @interface GetMapping {

    String value();

    HttpMethod method() default HttpMethod.GET;
}
