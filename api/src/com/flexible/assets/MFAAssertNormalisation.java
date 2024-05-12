package com.flexible.assets;

import com.flexible.annotations.Controller;
import com.flexible.implementions.MFANativeGetterConsumer;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Controller
public class MFAAssertNormalisation {
    public static <T> void notNull(T o, Consumer<T> consumer) {
        checker(o, consumer, Objects.nonNull(o), "Maybe the entry value is null.");
    }

    public static <T> boolean notNull(T o) {
        return Objects.nonNull(o);
    }

    public static <T> boolean notNull(MFANativeGetterConsumer<T> t) {
        try {
            return Objects.nonNull(t.get());
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void isNull(T o, Consumer<T> consumer) {
        checker(o, consumer, Objects.isNull(o), "Maybe the entry value is not null.");
    }

    public static <T> boolean isNull(T o) {
        return Objects.isNull(o);
    }

    public static <T> boolean isNull(MFANativeGetterConsumer<T> t) {
        try {
            return Objects.isNull(t.get());
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> boolean isEmpty(List<T> items) {
        return items.isEmpty();
    }

    public static <T> boolean isNotEmpty(List<T> items) {
        return !isEmpty(items);
    }

    private static <T> void checker(T o, Consumer<T> consumer, boolean condition, String exceptionMessage) {
        if (condition) {
            consumer.accept(o);
        } else {
            throw new NullPointerException(exceptionMessage);
        }
    }
}
