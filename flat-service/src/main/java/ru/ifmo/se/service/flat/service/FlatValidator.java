package ru.ifmo.se.service.flat.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import ru.ifmo.se.common.entity.Flat;
import ru.ifmo.se.common.exception.BadEntityException;
import ru.ifmo.se.common.exception.InvalidEntityIdException;
import ru.ifmo.se.common.exception.NoEntitiesException;
import ru.ifmo.se.service.flat.repository.FlatRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlatValidator {
    private final ValidatorFactory validatorFactory;
    private final FlatRepository flatRepository;

    public FlatValidator(ValidatorFactory validatorFactory,
                         FlatRepository flatRepository) {
        this.validatorFactory = validatorFactory;
        this.flatRepository = flatRepository;
    }

    public void checkFields(Flat flat) {
        ValidationResult validationResult = validate(flat);

        if (validationResult.hasErrors()) {
            throw new BadEntityException(validationResult.getErrors());
        }

    }

    public void checkId(Flat flat) {
        checkId(flat.getId());
    }

    public void checkId(Long flatId) {
        Long count = flatRepository.countById(flatId);

        if (count == 0) {
            throw new InvalidEntityIdException("flat with id " + flatId + " doesn't exist");
        }
    }

    public void checkFlatsExists() {
        if (IterableUtils.size(flatRepository.findAll()) == 0) {
            throw new NoEntitiesException("No flats found");
        }
    }

    private ValidationResult validate(Flat flat) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

        if (constraintViolations.isEmpty()) {
            return ValidationResult.ok();
        }

        List<String> errors = constraintViolations.stream()
                .map(this::violationToString)
                .collect(Collectors.toList());

        return new ValidationResult(errors);
    }

    private String violationToString(ConstraintViolation<Flat> violation) {
        return violation.getPropertyPath().toString() + " " + violation.getMessage();
    }

    @AllArgsConstructor
    private static class ValidationResult {
        @Getter
        private List<String> errors;

        public static ValidationResult ok() {
            return new ValidationResult(Collections.emptyList());
        }

        public boolean hasErrors() {
            return errors.size() > 0;
        }
    }
}
