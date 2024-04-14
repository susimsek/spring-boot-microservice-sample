package io.github.susimsek.account.logging.core;

import java.io.IOException;

public interface Sink {

    void write(final HttpRequest request) throws IOException;

    void write(final Message message) throws IOException;

    void write(final Message message, final boolean sent) throws IOException;

    void write(final HttpResponse response) throws IOException;
}