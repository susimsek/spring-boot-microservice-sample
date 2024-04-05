package io.github.susimsek.account.logging.core;

import java.net.URI;

public interface HttpRequest extends HttpMessage {

    String getRemote();

    String getMethod();

    URI getRequestUri();


    String getPath();

    String getQuery();
}