package com.attoresearchhostmanager.exception.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Taewoo
 */

@Constraint(validatedBy = DuplicatedIpValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedIpConstraint {
    String message() default "IP 주소가 중복되었습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}