package board.annotation.mapping;

import board.constant.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RequestMapping(method = HttpMethod.PUT)
public @interface PutMapping {

    String path() default "";

    HttpMethod method() default HttpMethod.PUT;

}
