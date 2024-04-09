package io.github.susimsek.card.logging.graphql;


import io.github.susimsek.card.logging.core.HttpRequest;
import io.github.susimsek.card.logging.core.Origin;
import java.net.URI;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class RemoteRequest implements HttpRequest {

    private final WebGraphQlRequest request;

    private final HttpHeaders headers;

    private final Object body;

    private final URI uri;

    public RemoteRequest(final WebGraphQlRequest request) {
        this.request = request;
        this.headers = request.getHeaders();
        this.uri = request.getUri().toUri();
        this.body = request.toMap();
    }

    @Override
    public Origin getOrigin() {
        return Origin.REMOTE;
    }

    @Override
    public String getRemote() {
        return "0:0:0:0:0:0:0:1";
    }

    @Override
    public String getMethod() {
        return HttpMethod.POST.name();
    }

    @Override
    public URI getRequestUri() {
        return uri;
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }


    @Override
    public byte[] body() {
        return new byte[0];
    }

    @Override
    public Object getBodyAsObject() {
        return body;
    }
}