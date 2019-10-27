package org.anized.common;

import java.util.function.Consumer;

public class Success<V> implements Try<V> {
    private V content;

    public Success(final V content) {
        this.content = content;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public Try<V> onSuccess(Consumer<V> consumer) {
        if (content != null) {
            consumer.accept(content);
        }
        return this;
    }

    @Override
    public Try<V> onFailure(Consumer<? super Throwable> consumer) {
        return this;
    }

}
