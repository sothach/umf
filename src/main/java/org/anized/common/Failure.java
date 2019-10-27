package org.anized.common;

import java.util.function.Consumer;

public class Failure<V> implements Try<V> {
    private Throwable content;

    public Failure(final Throwable content) {
        this.content = content;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public Try<V> onSuccess(Consumer<V> consumer) {
        return this;
    }

    @Override
    public Try<V> onFailure(Consumer<? super Throwable> consumer) {
        if (content != null) {
            consumer.accept(content);
        }
        return this;
    }
}
