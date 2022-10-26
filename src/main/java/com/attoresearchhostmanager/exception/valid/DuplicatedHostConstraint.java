package com.attoresearchhostmanager.exception.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Taewoo
 */

@Constraint(validatedBy = DuplicatedHostValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedHostConstraint {
    String message() default "호스트 이름이 중복되었습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}