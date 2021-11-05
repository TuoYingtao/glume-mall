package com.glume.common.core.annotation.constraint;

import com.glume.common.core.annotation.Logic;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Character 型 Logic验证
 * @author tuoyingtao
 * @create 2021-11-05 17:02
 */
public class CharacterLogicConstraintValidator implements ConstraintValidator<Logic,Character> {

    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(Logic constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (Integer val : vals) {
            set.add(val);
        }
    }

    /**
     * 判断是否校验成功
     * @param character 需要校验的值
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Character character, ConstraintValidatorContext constraintValidatorContext) {
        Integer num = Integer.parseInt(character.toString());
        return set.contains(num);
    }
}
