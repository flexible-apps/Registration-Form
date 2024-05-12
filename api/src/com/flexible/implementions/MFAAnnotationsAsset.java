package com.flexible.implementions;

import com.flexible.annotations.Interface;

@Interface
public interface MFAAnnotationsAsset<C, R> {

    C ifEqualDeclarationFieldName(String declarationField);

    C ifEqualController(Class<?> controller);

    R get();
}
