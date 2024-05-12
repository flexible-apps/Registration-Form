package com.flexible.annotations;


import org.intellij.lang.annotations.Language;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface RegularExpiration {
    @Language("RegExp")
    String pattern();
}

