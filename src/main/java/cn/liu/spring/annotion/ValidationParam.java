package cn.liu.spring.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LIUC022 on 2016/9/9.
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.PARAMETER)
public @interface ValidationParam {
    /**
     * 是否能为空，为true表示不能为空，false表示能够为空
     */
    boolean notNull() default false;

    /**
     * 是否为固定的Integer值
     */
    int[] fixedInteger() default {};

    /**
     * 最小值
     *
     * @return
     */
    int min()

        default -1;

    /**
     * 最大值
     *
     * @return
     */
    int max()

        default -1;

}

