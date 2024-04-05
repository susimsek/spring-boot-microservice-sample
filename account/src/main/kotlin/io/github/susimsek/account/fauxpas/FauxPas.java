package io.github.susimsek.account.fauxpas;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("checkstyle:MethodTypeParameterName")
public class FauxPas {


    public static <T, X extends Throwable> ThrowingConsumer<T, X> throwingConsumer(
        final ThrowingConsumer<T, X> consumer) {
        return consumer;
    }

    public static <T, R, X extends Throwable> ThrowingFunction<T, R, X> throwingFunction(
        final ThrowingFunction<T, R, X> function) {
        return function;
    }


    public static <T, X extends Throwable> ThrowingSupplier<T, X> throwingSupplier(
        final ThrowingSupplier<T, X> supplier) {
        return supplier;
    }
}
