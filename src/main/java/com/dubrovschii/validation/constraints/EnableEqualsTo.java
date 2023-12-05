package com.dubrovschii.validation.constraints;

import com.dubrovschii.validation.constraints.validator.EnableEqualsToValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EnableEqualsToValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableEqualsTo {
  String message() default "Field equality constraints violated";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
