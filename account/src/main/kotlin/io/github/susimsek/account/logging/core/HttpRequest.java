package io.github.susimsek.account.logging.core;

import java.net.URI;
import java.util.Optional;

public interface HttpRequest extends HttpMessage {

    String getRemote();

    String getMethod();

    URI getRequestUri();

    default String getHost() {
        return Optional.of(getRequestUri())
            .map(URI::getHost)
            .orElse("");
    }


    String getPath();

    String getQuery();
}