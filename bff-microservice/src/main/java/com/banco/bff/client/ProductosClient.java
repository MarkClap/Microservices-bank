package com.banco.bff.client;

import com.banco.bff.dto.ProductoDTO;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Slf4j
@Component
public class ProductosClient {

    private final WebClient webClient;
    private final String productosServiceUrl;

    public ProductosClient(WebClient webClient,
                           @Value("${productos.service.url}") String productosServiceUrl) {
        this.webClient = webClient;
        this.productosServiceUrl = productosServiceUrl;
    }

    public Flux<ProductoDTO> obtenerProductosPorCodigoCliente(String codigoCliente) {
        return Flux.deferContextual(ctx -> {
            MDC.put("trackingId", ctx.getOrDefault("trackingId", ""));
            return webClient.get()
                    .uri(productosServiceUrl + "/api/productos/cliente/{codigoCliente}", codigoCliente)
                    .retrieve()
                    .bodyToFlux(ProductoDTO.class)
                    .doOnComplete(() -> log.debug("Productos obtenidos correctamente"))
                    .doOnError(error -> log.error("Error al llamar a Productos MS", error))
                    .doFinally(signal -> MDC.remove("trackingId"));
        });
    }
}