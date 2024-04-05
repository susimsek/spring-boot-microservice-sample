package io.github.susimsek.account.logging.feign;

import feign.Request;
import io.github.susimsek.account.logging.core.HttpRequest;
import io.github.susimsek.account.logging.core.Origin;
import io.github.susimsek.account.logging.utils.HeaderUtils;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

public record LocalRequest(
    URI uri,
    Request.HttpMethod httpMethod,
    HttpHeaders headers,
    byte[] body

) implements HttpRequest {

    public static LocalRequest create(Request request) {
        return new LocalRequest(
            URI.create(request.url()),
            request.httpMethod(),
            HeaderUtils.toHeaders(request.headers()),
            request.body()
        );
    }

    @Override
    public String getRemote() {
        return "localhost";
    }

    @Override
    public String getMethod() {
        return httpMethod.toString();
    }

    @Override
    public URI getRequestUri() {
        return uri;
    }

    @Override
    public String getPath() {
        return uri.getPath() == null ? "" : uri.getPath();
    }

    @Override
    public String getQuery() {
        return uri.getQuery() == null ? "" : uri.getQuery();
    }

    @Override
    public String getProtocolVersion() {
        return "HTTP/1.1";
    }

    @Override
    public Origin getOrigin() {
        return Origin.LOCAL;
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Nullable
    @Override
    public MediaType getContentType() {
        return headers.getContentType();
    }

    @Override
    public byte[] body() {
        return body != null ? body : new byte[0];
    }
}