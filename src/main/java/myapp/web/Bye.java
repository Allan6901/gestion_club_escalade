package myapp.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ByeConstraintValidator.class)
@Documented
public @interface Bye {

    String message() default "Il manque le 'bye'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}