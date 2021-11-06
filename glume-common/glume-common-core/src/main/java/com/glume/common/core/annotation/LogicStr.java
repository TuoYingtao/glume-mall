package com.glume.common.core.annotation;

import com.glume.common.core.annotation.constraint.CharacterLogicStrConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 指定字母校验
 * @author tuoyingtao
 * @create 2021-11-06 9:27
 */
@Documented
@Constraint(validatedBy = {CharacterLogicStrConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStr {
    String message() default "{com.glume.common.core.annotation.Logic.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] vals() default {};
}
