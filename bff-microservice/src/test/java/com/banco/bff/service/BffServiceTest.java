package com.banco.bff.service;

import com.banco.bff.client.ClienteClient;
import com.banco.bff.client.ProductosClient;
import com.banco.bff.dto.ClienteDTO;
import com.banco.bff.dto.ClienteProductoResponse;
import com.banco.bff.dto.ProductoDTO;
import com.banco.bff.exception.ClienteNotFoundException;
import com.banco.bff.mapper.ClienteProductoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BffServiceTest {

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private ProductosClient productosClient;

    @Mock
    private ClienteProductoMapper mapper;

    @InjectMocks
    private BffService bffService;

    @Test
    void obtenerClienteConProductos_cuandoExiste_deberiaRetornarRespuesta() {
        String codigoCliente = "CLI001";
        ClienteDTO clienteDTO = new ClienteDTO(1L, "CLI001", "Juan", "Pérez", "CC", "12345678", "ACTIVO");
        List<ProductoDTO> productos = List.of(
                new ProductoDTO(1L, "CLI001", "Cuenta Ahorros", "Ahorros Plus", BigDecimal.valueOf(5000), "ACTIVO")
        );
        ClienteProductoResponse expectedResponse = new ClienteProductoResponse(clienteDTO, productos, 1);

        when(clienteClient.obtenerClientePorCodigo(codigoCliente)).thenReturn(Mono.just(clienteDTO));
        when(productosClient.obtenerProductosPorCodigoCliente(codigoCliente)).thenReturn(Flux.fromIterable(productos));
        when(mapper.toResponse(clienteDTO, productos)).thenReturn(expectedResponse);

        StepVerifier.create(bffService.obtenerClienteConProductos(codigoCliente))
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void obtenerClienteConProductos_cuandoClienteNoExiste_deberiaLanzarExcepcion() {
        String codigoCliente = "CLI999";
        when(clienteClient.obtenerClientePorCodigo(codigoCliente)).thenReturn(Mono.empty());

        // Importante: mockear productos para que retorne un Flux vacío, no null
        when(productosClient.obtenerProductosPorCodigoCliente(codigoCliente)).thenReturn(Flux.empty());

        StepVerifier.create(bffService.obtenerClienteConProductos(codigoCliente))
                .expectError(ClienteNotFoundException.class)
                .verify();
    }
}