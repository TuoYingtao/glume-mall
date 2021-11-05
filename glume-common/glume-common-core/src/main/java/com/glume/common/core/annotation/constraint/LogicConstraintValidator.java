package com.glume.common.core.annotation.constraint;

import com.glume.common.core.annotation.Logic;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tuoyingtao
 * @create 2021-11-05 13:31
 */
public class LogicConstraintValidator implements ConstraintValidator<Logic,Integer> {

    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(Logic constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            set.add(val);
        }
    }

    /**
     * 判断是否校验成功
     * @param integer 需要校验的值
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
