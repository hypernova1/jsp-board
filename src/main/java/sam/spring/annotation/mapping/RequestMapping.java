package sam.spring.annotation.mapping;

import sam.spring.constant.HttpMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface RequestMapping {

    String value() default "";
    HttpMethod method() default HttpMethod.GET;

}
