package com.banco.bff.service;

import com.banco.bff.client.ClienteClient;
import com.banco.bff.client.ProductosClient;
import com.banco.bff.dto.ClienteDTO;
import com.banco.bff.dto.ClienteProductoResponse;
import com.banco.bff.dto.ProductoDTO;
import com.banco.bff.exception.ClienteNotFoundException;
import com.banco.bff.mapper.ClienteProductoMapper;
import com.banco.bff.util.ReactiveMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class BffService {

    private final ClienteClient clienteClient;
    private final ProductosClient productosClient;
    private final ClienteProductoMapper mapper;

    public BffService(ClienteClient clienteClient,
                      ProductosClient productosClient,
                      ClienteProductoMapper mapper) {
        this.clienteClient = clienteClient;
        this.productosClient = productosClient;
        this.mapper = mapper;
    }

    public Mono<ClienteProductoResponse> obtenerClienteConProductos(String codigoCliente) {
        return Mono.deferContextual(ctx -> {
            MDC.put("trackingId", ctx.getOrDefault("trackingId", ""));
            log.info("BFF: Orquestando llamadas para cliente: {}", codigoCliente);

            Mono<ClienteDTO> clienteMono = clienteClient.obtenerClientePorCodigo(codigoCliente)
                    .switchIfEmpty(Mono.error(new ClienteNotFoundException("Cliente no encontrado: " + codigoCliente)));

            Mono<List<ProductoDTO>> productosMono = productosClient.obtenerProductosPorCodigoCliente(codigoCliente)
                    .collectList()
                    .defaultIfEmpty(List.of());

            return Mono.zip(clienteMono, productosMono)
                    .map(tuple -> {
                        ClienteDTO cliente = tuple.getT1();
                        List<ProductoDTO> productos = tuple.getT2();
                        log.debug("Integrando respuesta. Cliente: {}, Productos: {}", cliente.getNombres(), productos.size());
                        return mapper.toResponse(cliente, productos);
                    })
                    .doFinally(signal -> MDC.remove("trackingId"));
        });
    }
}