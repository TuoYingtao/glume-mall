package com.glume.common.core.annotation;


import com.glume.common.core.annotation.constraint.CharacterLogicConstraintValidator;
import com.glume.common.core.annotation.constraint.IntegerLogicConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 指定数字校验
 * @author tuoyingtao
 * @create 2021-11-05 13:23
 */
@Documented
@Constraint(validatedBy = {IntegerLogicConstraintValidator.class,
        CharacterLogicConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logic {

    String message() default "{com.glume.common.core.annotation.Logic.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}
