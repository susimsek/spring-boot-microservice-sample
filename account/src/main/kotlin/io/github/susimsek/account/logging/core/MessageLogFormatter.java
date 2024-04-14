package io.github.susimsek.account.logging.core;

import java.io.IOException;

public interface MessageLogFormatter {

    String format(final Message message) throws IOException;

    String format(final Message message, final boolean sent) throws IOException;
}