package com.glume.common.validator.config;

import lombok.Data;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author tuoyingtao
 * @create 2021-11-04 9:31
 */
@Data
@ConfigurationProperties(prefix = "common.validator")
@Configuration
public class validatorConfig {

    /**
     * 快速失败模式
     */
    private Boolean failFast = true;

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(failFast)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

}
