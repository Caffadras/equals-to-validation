package com.dubrovschii.validation.constraints.validator;

import com.dubrovschii.validation.constraints.EnableEqualsTo;
import com.dubrovschii.validation.constraints.EqualsTo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class EnableEqualsToValidator implements ConstraintValidator<EnableEqualsTo, Object> {

  @Override
  public void initialize(EnableEqualsTo constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
    if (Objects.isNull(o)) {
      return true;
    }

    Class<?> clazz = o.getClass();
    boolean isValid = true;

    for (Field declaredField : clazz.getDeclaredFields()) {
      if (declaredField.isAnnotationPresent(EqualsTo.class)) {
        declaredField.setAccessible(true);
        EqualsTo equalsToAnnotation = declaredField.getAnnotation(EqualsTo.class);
        String equalsToFieldName = equalsToAnnotation.fieldName();
        try {
          Field equalsToField = clazz.getDeclaredField(equalsToFieldName);
          equalsToField.setAccessible(true);

          Object declaredFieldValue = declaredField.get(o);
          Object equalsToFieldValue = equalsToField.get(o);

          if (!checkIfEquals(declaredFieldValue, equalsToFieldValue)){
            isValid = false;
            String errorMessage = equalsToAnnotation.message();

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                .buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(declaredField.getName())
                .addConstraintViolation();
          }
        } catch (Exception e) {
          throw new RuntimeException("Could not complete validation for " + o, e);
        }
      }
    }

    return isValid;
  }

  private boolean checkIfEquals(Object value, Object otherValue) {
    return Objects.nonNull(value) ? value.equals(otherValue) : Objects.isNull(otherValue);
  }
}
