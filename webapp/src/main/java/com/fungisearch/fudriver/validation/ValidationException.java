package com.fungisearch.fudriver.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created by marcin on 10.08.15.
 */
public class ValidationException extends RuntimeException{

    private final Set<ConstraintViolation<Object>> constraintViolations;

    public ValidationException(Set<ConstraintViolation<Object>> constraintViolations) {
        this.constraintViolations = constraintViolations;
    }

    public Set<ConstraintViolation<Object>> getConstraintViolations() {
        return constraintViolations;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValidationException{constraintViolations=[\n" );
        for(ConstraintViolation violation: constraintViolations) {
            sb.append(violation.toString());
            sb.append(",\n");
        }
        sb.append("]}");
        return sb.toString();
    }

}
