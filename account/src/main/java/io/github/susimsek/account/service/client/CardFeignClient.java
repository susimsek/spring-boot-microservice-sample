package io.github.susimsek.account.service.client;

import io.github.susimsek.account.dto.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="card", url = "http://card:9000", fallback = CardFallback.class)
public interface CardFeignClient {

    @GetMapping(value = "/api/card",consumes = "application/json")
    CardDTO fetchCardDetails(
        @RequestHeader("X-Correlation-Id") String correlationId,
        @RequestParam String mobileNumber);

}