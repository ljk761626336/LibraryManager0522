package com.titan.baselibrary.treerecyclerview.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 125C01063144 on 2018/2/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TreeDataType {
    /**
     * 直接绑定itemclass
     *
     * @return
     */
    Class iClass() default Object.class;

    /**
     * @return -1代表不查找
     */
    int value() default -1;
}
