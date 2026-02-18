package com.banco.bff.util;

import org.slf4j.MDC;
import reactor.core.publisher.Mono;
import java.util.function.Function;

public class ReactiveMdcUtil {

    public static <T> Function<Mono<T>, Mono<T>> withMdc(String key) {
        return mono -> Mono.deferContextual(ctx -> {
            String value = ctx.getOrDefault(key, "");
            MDC.put(key, value);
            return mono
                    .doOnEach(signal -> {
                        if (signal.isOnComplete() || signal.isOnError()) {
                            MDC.remove(key);
                        }
                    });
        });
    }
}