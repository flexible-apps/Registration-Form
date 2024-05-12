package com.flexible.assets;

import com.flexible.annotations.Controller;
import com.flexible.implementions.MFAGetterConsumer;
import com.flexible.implementions.MFANativeGetterConsumer;

import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.function.Consumer;

@Controller
public class MFAAssertBuilder<V> extends MFAAssertNormalisation {
    private boolean isEqual = false;
    private final V value;

    public MFAAssertBuilder(V value) {
        this.value = value;
    }

    public MFAAssertBuilder<V> asValue(V value) {
        return new MFAAssertBuilder<>(value);
    }

    public MFAAssertBuilder<V> asValue(MFANativeGetterConsumer<V> value) {
        try {
            return new MFAAssertBuilder<>(value.get());
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public <I> MFAAssertBuilder<V> equal(I entry) {
        isEqual = Objects.equals(value, entry);
        return this;
    }

    public <I> MFAAssertBuilder<V> noneEqual(I entry) {
        isEqual = !equals(entry);
        return this;
    }

    public MFAAssertBuilder<V> equal(MFAGetterConsumer<Boolean, V> consumerAsValue) {
        try {
            isEqual = consumerAsValue.get(value);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public boolean get() {
        return isEqual;
    }

    public static <T> Builder<T> getBuilder(MFANativeGetterConsumer<T> value) {
        try {
            return new Builder<>(value.get());
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Builder<T> getBuilder(T value) {
        return new Builder<>(value);
    }

    public static class Builder<T> {
        private final T value;

        public Builder(T value) {
            this.value = value;
        }

        public <R> Builder<R> map(MFAGetterConsumer<R, T> mapTo) {
            try {
                return new Builder<>(mapTo.get(value));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public MFAFilterBuilder<T> filter(MFAGetterConsumer<Boolean, T> function) {
            try {
                return new MFAFilterBuilder<>(value, function.get(value));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public Builder<T> print(Consumer<T> function) {
            function.accept(this.value);
            return this;
        }
    }

}
