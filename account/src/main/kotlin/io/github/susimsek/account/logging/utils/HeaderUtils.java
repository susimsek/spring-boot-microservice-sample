package io.github.susimsek.account.logging.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;

@UtilityClass
public final class HeaderUtils {
    public HttpHeaders toHeaders(Map<String, Collection<String>> feignHeaders) {
        HttpHeaders headers = new HttpHeaders();
        feignHeaders.keySet()
            .forEach(key -> headers.put(key, getHeaderValues(feignHeaders, key)));
        return headers;
    }

    private List<String> getHeaderValues(Map<String, Collection<String>> feignHeaders, String headerName) {
        Collection<String> values = feignHeaders.get(headerName);
        if (!CollectionUtils.isEmpty(values)) {
            return new ArrayList<>(values);
        }
        return new ArrayList<>();
    }
}