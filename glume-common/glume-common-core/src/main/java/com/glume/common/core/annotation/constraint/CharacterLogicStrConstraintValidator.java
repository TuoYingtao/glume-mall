package com.glume.common.core.annotation.constraint;

import com.glume.common.core.annotation.LogicStr;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Character 型 LogicStr验证
 * @author tuoyingtao
 * @create 2021-11-06 9:29
 */
public class CharacterLogicStrConstraintValidator implements ConstraintValidator<LogicStr,Character> {

    private Set<String> set = new HashSet();

    @Override
    public void initialize(LogicStr constraintAnnotation) {
        String[] vals = constraintAnnotation.vals();
        for (String val : vals) {
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
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next().toUpperCase();
            String aCase = character.toString().toUpperCase();
            if (aCase.equals(next)) {
                return true;
            }
        }
        return false;
    }
}
