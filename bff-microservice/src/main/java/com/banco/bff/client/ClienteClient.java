package com.banco.bff.client;

import com.banco.bff.dto.ClienteDTO;
import com.banco.bff.util.ReactiveMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ClienteClient {

    private final WebClient webClient;
    private final String clienteServiceUrl;

    public ClienteClient(WebClient webClient,
                         @Value("${cliente.service.url}") String clienteServiceUrl) {
        this.webClient = webClient;
        this.clienteServiceUrl = clienteServiceUrl;
    }

    public Mono<ClienteDTO> obtenerClientePorCodigo(String codigoUnico) {
        return Mono.deferContextual(ctx -> {
            // Colocar el trackingId en MDC desde el contexto
            MDC.put("trackingId", ctx.getOrDefault("trackingId", ""));
            return webClient.get()
                    .uri(clienteServiceUrl + "/api/clientes/{codigo}", codigoUnico)
                    .retrieve()
                    .bodyToMono(ClienteDTO.class)
                    .doOnSuccess(cliente -> log.info("Cliente obtenido: {}", cliente))
                    .doOnError(error -> log.error("Error al llamar a Cliente MS", error))
                    .doFinally(signal -> MDC.remove("trackingId"));
        });
    }
}