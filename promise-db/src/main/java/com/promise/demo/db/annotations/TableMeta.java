package com.promise.demo.db.annotations;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface TableMeta {

    /**
     * table name
     * @return
     */
    String value() default "";

}
