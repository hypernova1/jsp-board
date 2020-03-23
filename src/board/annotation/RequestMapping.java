package board.annotation;

import board.constant.HttpMethod;

import javax.servlet.HttpMethodConstraintElement;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface RequestMapping {

    String path() default "";
    HttpMethod method() default HttpMethod.GET;

}
