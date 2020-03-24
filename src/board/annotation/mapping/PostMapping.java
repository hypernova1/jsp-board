package board.annotation.mapping;

import board.constant.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RequestMapping(method = HttpMethod.POST)
public @interface PostMapping {

    String value() default "";

    HttpMethod method() default HttpMethod.POST;

}
