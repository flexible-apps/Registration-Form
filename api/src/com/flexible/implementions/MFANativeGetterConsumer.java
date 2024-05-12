package com.flexible.implementions;

import com.flexible.annotations.Interface;

import java.io.FileNotFoundException;

@Interface
@FunctionalInterface
public interface MFANativeGetterConsumer<T> {
    T get() throws NullPointerException;
}
