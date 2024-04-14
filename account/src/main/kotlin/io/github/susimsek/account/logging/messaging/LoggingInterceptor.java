package io.github.susimsek.account.logging.messaging;

import io.github.susimsek.account.logging.core.ChannelType;
import io.github.susimsek.account.logging.core.Sink;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@RequiredArgsConstructor
@Slf4j
public class LoggingInterceptor implements ChannelInterceptor {

    private final Sink sink;
    private final BindingServiceProperties bindingServiceProperties;

    @SneakyThrows
    @Override
    public void postSend(
        @NonNull Message<?> msg,
        @NonNull MessageChannel mc,
        boolean sent) {
        final LocalMessage message = new LocalMessage(msg, mc,
            bindingServiceProperties, ChannelType.OUTGOING);
        sink.write(message, sent);
    }

    @SneakyThrows
    @Override
    public Message<?> postReceive(
        @NonNull Message<?> msg,
        @NonNull MessageChannel mc) {
        final LocalMessage message = new LocalMessage(msg, mc,
            bindingServiceProperties, ChannelType.INCOMING);
        sink.write(message);
        return msg;
    }
}