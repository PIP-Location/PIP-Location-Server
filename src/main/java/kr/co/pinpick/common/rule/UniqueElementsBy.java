package kr.co.pinpick.common.rule;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Documented
@Constraint(validatedBy = UniqueElementsByValidator.class)
@Target( {ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueElementsBy {
    String message() default "is duplicated";
    String name() default "";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}

class UniqueElementsByValidator implements ConstraintValidator<UniqueElementsBy, List<? extends UniqueByAble>> {

    @Override
    public void initialize(UniqueElementsBy rule) {
    }

    @Override
    public boolean isValid(List<? extends UniqueByAble> list, ConstraintValidatorContext constraintValidatorContext) {
        if (list == null) return true;
        var distinct = list.stream().map(UniqueByAble::getUniqueKey).collect(Collectors.toSet());
        return distinct.size() == list.size();
    }
}
