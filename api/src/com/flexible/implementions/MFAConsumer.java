package com.flexible.implementions;

import com.flexible.annotations.Interface;

@Interface
@FunctionalInterface
public interface MFAConsumer<R, V>{
    R accept(V v);
}
