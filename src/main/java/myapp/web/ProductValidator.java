package myapp.web;

import myapp.model.ProductCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import myapp.model.Product;

@Service
public class ProductValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                "product.name");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description",
                "product.description");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type",
                "product.type");

        if (!(product.getPrice() > 0.0)) {
            errors.rejectValue("price", "product.price.too.low");
        }

        ProductCode code = product.getCode();
        if (code != null) {
            if (!code.getBase().matches("[A-Z]")) {
                errors.rejectValue("code", "product.code.base");
            }
            if (!(code.getNumber() >= 1000 && code.getNumber() <= 9999)) {
                errors.rejectValue("code", "product.code.number");
            }
        }
    }



}