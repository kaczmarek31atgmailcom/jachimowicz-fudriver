package com.fungisearch.fudriver.validation;



import org.springframework.stereotype.Service;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;



/**
 * Created by marcin on 10.08.15.
 */
@Service
public class HibernateBeanValidator implements BeanValidator {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Override
    public void validate(Object bean) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);
        if (!constraintViolations.isEmpty()) {
            throw new ValidationException(constraintViolations);
        }
    }

}
