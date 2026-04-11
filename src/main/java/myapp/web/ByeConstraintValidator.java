package myapp.web;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ByeConstraintValidator implements ConstraintValidator<Bye, String> {

    @Override
    public void initialize(Bye arg0) {
    }

    @Override
    public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
        if (arg0.contains("bye"))
            return true;
        return false;
    }

}