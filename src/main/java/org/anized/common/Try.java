package org.anized.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Try<V> {
    boolean isSuccess();
    Try<V> onSuccess(Consumer<V> consumer);
    Try<V> onFailure(Consumer<? super Throwable> consumer);

    static <T> Try<T> apply(Supplier<T> body) {
        try {
            return new Success<>(body.get());
        } catch(final RuntimeException t) {
            return new Failure<>(t);
        }
    }
}
