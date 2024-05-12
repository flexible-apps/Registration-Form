package com.flexible.implementions;

import com.flexible.annotations.Interface;

@Interface
@FunctionalInterface
public interface MFAConsumerObservableValue< V>{
    V accept();
}
