package com.banco.bff.controller;

import com.banco.bff.dto.ClienteProductoResponseConTracking;
import com.banco.bff.dto.auth.ErrorResponse;
import com.banco.bff.exception.ClienteNotFoundException;
import com.banco.bff.service.BffService;
import com.banco.bff.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
public class BffController {

    private final BffService bffService;
    private final EncryptionUtil encryptionUtil;

    public BffController(BffService bffService, EncryptionUtil encryptionUtil) {
        this.bffService = bffService;
        this.encryptionUtil = encryptionUtil;
    }

    @GetMapping("/{codigoEncriptado}")
    public Mono<ResponseEntity<Object>> obtenerClienteConProductos(
            @PathVariable String codigoEncriptado,
            @RequestHeader(value = "X-Tracking-Id", required = false) String trackingIdHeader) {

        String trackingId = trackingIdHeader != null ? trackingIdHeader : UUID.randomUUID().toString();

        MDC.put("trackingId", trackingId);
        log.info("Tracking ID: {} - Solicitado cliente encriptado: {}", trackingId, codigoEncriptado);

        String codigoPuro;
        try {
            codigoPuro = encryptionUtil.decrypt(codigoEncriptado);
        } catch (Exception e) {
            log.error("Tracking ID: {} - Error al desencriptar: {}", trackingId, e.getMessage());
            MDC.clear();
            return Mono.just(ResponseEntity.badRequest()
                    .body(new ErrorResponse("error", "Error al desencriptar código")));
        }

        return bffService.obtenerClienteConProductos(codigoPuro)
                .<ResponseEntity<Object>>map(response -> ResponseEntity.ok(
                        new ClienteProductoResponseConTracking(trackingId,
                                response.getCliente(),
                                response.getProductos(),
                                response.getCantidadProductos())))
                .onErrorResume(ClienteNotFoundException.class, e -> {
                    log.error("Tracking ID: {} - Cliente no encontrado: {}", trackingId, e.getMessage());
                    return Mono.just(ResponseEntity.status(404)
                            .body(new ErrorResponse("error", e.getMessage())));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Tracking ID: {} - Error interno: {}", trackingId, e.getMessage());
                    return Mono.just(ResponseEntity.status(500)
                            .body(new ErrorResponse("error", "Error interno")));
                })
                .doFinally(signal -> MDC.clear())
                .contextWrite(Context.of("trackingId", trackingId)); // <-- ¡IMPORTANTE!
    }

    @GetMapping("/test/encrypt/{codigo}")
    public String encrypt(@PathVariable String codigo) {
        return encryptionUtil.encrypt(codigo);
    }

    @GetMapping("/health/check")
    public Mono<String> health() {
        return Mono.just("BFF Microservice está funcionando");
    }

}