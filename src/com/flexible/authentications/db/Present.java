package com.flexible.authentications.db;

import com.flexible.implementions.MFANativeConsumer;

import java.util.function.Consumer;

public interface Present {
    void ifPresentOrElse(Consumer<ClientCreator> ifPresent, MFANativeConsumer orElse);
}
