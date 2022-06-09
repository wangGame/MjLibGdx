package com.kw.gdx.csvanddata;

import com.kw.gdx.annotation.AnnotationInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Interpreter {
    public static String iteratorAnnotations(Field field) {
        Value annotation = AnnotationInfo.checkFeildAnnotation(field, Value.class);
        return annotation == null ? field.getName() : annotation.value();
    }
}