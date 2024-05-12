package com.flexible.implementions;

import com.flexible.annotations.Interface;

import java.io.FileNotFoundException;

@Interface
@FunctionalInterface
public interface MFAGetterConsumer<R, T>{
    R get(T t) throws FileNotFoundException;
}
