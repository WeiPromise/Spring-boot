package com.promise.demo.db.annotations;

import java.lang.annotation.*;

/**
 * FieldMeta
 *
 * @author yewt
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface FieldMeta {

    String fieldName() default "";

    /**
     * column name in table
     *
     * @return columnName
     */
    String columnName() default "";

    boolean idField() default false;
}
