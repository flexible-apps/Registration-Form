package com.flexible.assets;

import com.flexible.implementions.MFAGetterConsumer;
import com.flexible.implementions.MFANativeConsumer;

import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.function.Consumer;

public class MFAFilterBuilder<T> {
    private final T value;

    private final boolean match;

    public MFAFilterBuilder(T value, boolean match) {
        this.value = value;
        this.match = match;
    }

    public MFAFilterBuilder<T> peek(Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

    public  <E> MFAFilterBuilder<E> map(MFAGetterConsumer<E,T> mapper) {
        if (Objects.isNull(value)) {
            return new MFAFilterBuilder<>(null, false);
        }
        try {
            return new MFAFilterBuilder<>(mapper.get(value), match);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void ifPresentOrElse(Consumer<T> ifPresentConsumer, MFANativeConsumer orElseConsumer) {
        if (match) {
            ifPresentConsumer.accept(value);
        } else {
            orElseConsumer.accept();
        }
    }

    public MFAFilterBuilder<T> ifPresent(Consumer<T> ifPresentConsumer) {
        if (match) {
            ifPresentConsumer.accept(value);
        }
        return this;
    }

    public T get() {
        if (match) {
            return value;
        } else {
            throw new RuntimeException("Value is null.");
        }
    }

    public MFAFilterBuilder<T> orElse(MFANativeConsumer orElseConsumer) {
        if (!match) {
            orElseConsumer.accept();
        }
        return this;
    }

    public <R> R ifPresentReduce(MFAGetterConsumer<R, T> ifPresentConsumer) {
        if (match) {
            try {
                return ifPresentConsumer.get(value);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
