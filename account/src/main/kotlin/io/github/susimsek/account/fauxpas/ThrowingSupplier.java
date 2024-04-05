package io.github.susimsek.account.fauxpas;

import java.util.function.Supplier;
import lombok.SneakyThrows;

@FunctionalInterface
public interface ThrowingSupplier<T, X extends Throwable> extends Supplier<T> {

    T tryGet() throws X;

    @Override
    @SneakyThrows
    default T get() {
        return tryGet();
    }

}