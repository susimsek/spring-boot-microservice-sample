package io.github.susimsek.account.logging.utils;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import java.util.Map.Entry;

@UtilityClass
public final class HeaderUtils {
    public HttpHeaders toHeaders(Map<String, Collection<String>> feignHeaders) {
        HttpHeaders headers = new HttpHeaders();
        feignHeaders.keySet()
            .forEach(key -> headers.put(key, getHeaderValues(feignHeaders, key)));
        return headers;
    }

    public HttpHeaders toHeaders(final Iterable<Entry<String, String>> entries) {
        HttpHeaders headers = new HttpHeaders();

        for (final Entry<String, String> entry : entries) {
            append(headers, entry);
        }
        return headers;
    }

    private List<String> getHeaderValues(Map<String, Collection<String>> feignHeaders, String headerName) {
        Collection<String> values = feignHeaders.get(headerName);
        if (!CollectionUtils.isEmpty(values)) {
            return new ArrayList<>(values);
        }
        return new ArrayList<>();
    }

    private void append(
        final HttpHeaders headers, final Entry<String, String> entry) {
        var previous = headers.get(entry.getKey());
        if (previous == null) {
            headers.put(entry.getKey(), singletonList(entry.getValue()));
        } else {
            previous.add(entry.getValue());
            headers.put(entry.getKey(), previous);
        }
    }

}