package io.github.susimsek.account.logging.messaging;

import io.github.susimsek.account.logging.core.ChannelType;
import io.github.susimsek.account.logging.core.Message;
import java.io.IOException;
import java.util.Optional;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.messaging.DirectWithAttributesChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

public final class LocalMessage implements Message {

    private final org.springframework.messaging.Message<?> message;

    private final String channel;
    private final String destination;

    private final ChannelType channelType;
    private final byte[] payload;

    public LocalMessage(org.springframework.messaging.Message<?> message,
                        MessageChannel mc,
                        BindingServiceProperties bindingServiceProperties,
                        ChannelType channelType) {
        String channelName = "";
        String destination = "";
        if (mc instanceof DirectWithAttributesChannel directChannel) {
            channelName = directChannel.getFullChannelName();
            destination =
                directChannel.getComponentName() != null
                    ? Optional.ofNullable(
                        bindingServiceProperties.getBindingProperties(
                            directChannel.getComponentName()))
                    .map(BindingProperties::getDestination)
                    .orElse("")
                    : "";
        }
        this.message = message;
        this.channel = channelName;
        this.destination = destination;
        this.channelType = channelType;
        this.payload = (byte[]) message.getPayload();
    }


    @Override
    public MessageHeaders headers() {
        return message.getHeaders();
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public ChannelType getChannelType() {
        return channelType;
    }

    @Override
    public byte[] payload() throws IOException {
        return payload;
    }
}