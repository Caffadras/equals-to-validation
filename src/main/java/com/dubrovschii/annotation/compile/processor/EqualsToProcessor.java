package com.dubrovschii.annotation.compile.processor;

import com.dubrovschii.validation.constraints.EqualsTo;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;


@SupportedAnnotationTypes("com.dubrovschii.validation.constraints.EqualsTo")
public class EqualsToProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(EqualsTo.class)) {
      EqualsTo equalsToAnnotation = element.getAnnotation(EqualsTo.class);
      String fieldName = equalsToAnnotation.fieldName();
      TypeElement classElement = (TypeElement) element.getEnclosingElement();

      boolean fieldExists = classElement.getEnclosedElements().stream()
          .anyMatch(enclosedElement ->
              enclosedElement.getKind().isField() &&
              enclosedElement.getSimpleName().toString().equals(fieldName));

      if (!fieldExists) {
        processingEnv.getMessager().printMessage(Kind.ERROR, String.format(
                "Field %s is not present in class %s",
                fieldName, classElement.getSimpleName().toString()),
            element);
      }
    }

    return true;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}
