package io.github.susimsek.account.constants;

import java.util.Random;

public final class Constants {

    private Constants() {
        // restrict instantiation
    }

    public static final Random RANDOM = new Random();

    public static final String SPRING_PROFILE_DEVELOPMENT = "local";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
}
