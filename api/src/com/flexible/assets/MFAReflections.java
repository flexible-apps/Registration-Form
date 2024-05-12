package com.flexible.assets;

import com.flexible.implementions.MFAAnnotationsAsset;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class MFAReflections  {
    public static <C, E, R> AnnotationBuilder<C, E, R> getDeclarationAnnotations(C[] declarationAnnotations) {
        return new AnnotationBuilder<>(declarationAnnotations);
    }

    @SuppressWarnings("unchecked")
    public static class AnnotationBuilder<C, E, R> implements MFAAnnotationsAsset<AnnotationBuilder<C, E, R>, R> {
        private final C[] declarationAnnotations;

        private String fieldName;

        private Class<E> entryClass;

        public AnnotationBuilder(C[] declarationAnnotations) {
            this.declarationAnnotations = declarationAnnotations;
        }

        @Override
        public AnnotationBuilder<C, E, R> ifEqualDeclarationFieldName(String declarationField) {
            this.fieldName = declarationField;
            return this;
        }

        @Override
        public AnnotationBuilder<C, E, R> ifEqualController(Class<?> controller) {
            this.entryClass = (Class<E>) controller;
            return this;
        }

        @Override
        public R get() {
            AtomicReference<R> findClass = new AtomicReference<>();
            for (C t : declarationAnnotations) {
                if (t.getClass().equals(Field.class)) {
                    Field field = (Field) t;
                    field.setAccessible(true);
                    if (field.getName().equals(fieldName)) {
                        Arrays.stream(field.getDeclaredAnnotations())
                                .filter(annotation -> annotation.annotationType().equals(entryClass))
                                .map(annotation -> (R) annotation)
                                .findFirst().ifPresent(findClass::set);

                    }
                } else if (t.getClass().equals(Constructor.class)) {
                    Constructor<?> constructor = (Constructor<?>) t;
                    constructor.setAccessible(true);
                    if (constructor.getName().equals(fieldName)) {
                        Arrays.stream(constructor.getDeclaredAnnotations())
                                .filter(annotation -> annotation.annotationType().equals(entryClass))
                                .map(annotation -> (R) annotation)
                                .findFirst().ifPresent(findClass::set);
                    }

                } else {
                    Method method = (Method) t;
                    method.setAccessible(true);
                    if (method.getName().equals(fieldName)) {
                        Arrays.stream(method.getDeclaredAnnotations())
                                .filter(annotation -> annotation.annotationType().equals(entryClass))
                                .map(annotation -> (R) annotation)
                                .findAny().ifPresent(findClass::set);
                    }
                }
            }

            return findClass.get();
        }

        public  MFAAssertBuilder.Builder<R> toAssertBuilder() {
            return MFAAssertBuilder.getBuilder(get());
        }
    }

}
