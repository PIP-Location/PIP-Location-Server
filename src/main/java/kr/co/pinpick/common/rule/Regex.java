package kr.co.pinpick.common.rule;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.Setter;

import java.lang.annotation.*;
import java.util.regex.Pattern;

@Documented
@Constraint(validatedBy = RegexValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {
    String message() default "invalid email";
    String pattern();
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}

class RegexValidator implements ConstraintValidator<Regex, String> {
    @Setter(AccessLevel.PROTECTED)
    private String pattern;

    @Override
    public void initialize(Regex contactNumber) {
        setPattern(contactNumber.pattern());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches(pattern, s);
    }
}
