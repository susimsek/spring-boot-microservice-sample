package io.github.susimsek.account.fauxpas;

import java.util.function.Consumer;
import lombok.SneakyThrows;

@FunctionalInterface
public interface ThrowingConsumer<T, X extends Throwable> extends Consumer<T> {

    void tryAccept(T t) throws X;

    @Override
    @SneakyThrows
    default void accept(final T t) {
        tryAccept(t);
    }

}