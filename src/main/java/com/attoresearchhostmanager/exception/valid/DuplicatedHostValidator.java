package com.attoresearchhostmanager.exception.valid;

/**
 * @author Taewoo
 */


import com.attoresearchhostmanager.repository.HostRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DuplicatedHostValidator implements ConstraintValidator<DuplicatedHostConstraint, String> {

    private final HostRepository hostRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value == null || value.isEmpty() || !hostRepository.existsByName(value));
    }
}
