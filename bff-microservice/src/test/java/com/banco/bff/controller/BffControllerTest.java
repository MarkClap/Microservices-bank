package com.banco.bff.controller;

import com.banco.bff.SecurityTestConfig;
import com.banco.bff.dto.ClienteDTO;
import com.banco.bff.dto.ClienteProductoResponse;
import com.banco.bff.dto.ProductoDTO;
import com.banco.bff.exception.ClienteNotFoundException;
import com.banco.bff.service.BffService;
import com.banco.bff.util.EncryptionUtil;
import com.banco.bff.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(BffController.class)
@Import(SecurityTestConfig.class) // <- Opcional: importar configuración de seguridad de prueba
class BffControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BffService bffService;

    @MockitoBean
    private EncryptionUtil encryptionUtil;

    @MockitoBean
    private JwtUtil jwtUtil; // Añadido para mockear JwtUtil

    private String validToken;

    @BeforeEach
    void setUp() {
        // Simular que cualquier token es válido
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("testuser");
        // Generar un token de prueba (o usar uno ficticio)
        validToken = "Bearer test-token";
    }

    @Test
    void obtenerClienteConProductos_deberiaRetornarOk() {
        // Given
        String codigoEncriptado = "encriptado";
        String codigoPuro = "CLI001";
        String trackingId = "test-tracking";

        ClienteDTO clienteDTO = new ClienteDTO(1L, "CLI001", "Juan", "Pérez", "CC", "12345678", "ACTIVO");
        List<ProductoDTO> productos = List.of(
                new ProductoDTO(1L, "CLI001", "Cuenta Ahorros", "Ahorros Plus", BigDecimal.valueOf(5000), "ACTIVO")
        );
        ClienteProductoResponse serviceResponse = new ClienteProductoResponse(clienteDTO, productos, 1);

        when(encryptionUtil.decrypt(codigoEncriptado)).thenReturn(codigoPuro);
        when(bffService.obtenerClienteConProductos(codigoPuro)).thenReturn(Mono.just(serviceResponse));

        // When / Then
        webTestClient.get()
                .uri("/api/clientes/{codigo}", codigoEncriptado)
                .header("Authorization", validToken)
                .header("X-Tracking-Id", trackingId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.trackingId").isEqualTo(trackingId)
                .jsonPath("$.cliente.codigoUnico").isEqualTo("CLI001")
                .jsonPath("$.productos.length()").isEqualTo(1)
                .jsonPath("$.cantidadProductos").isEqualTo(1);
    }

    @Test
    void cuandoClienteNoExiste_deberiaRetornar404() {
        String codigoEncriptado = "encriptado";
        String codigoPuro = "CLI999";

        when(encryptionUtil.decrypt(codigoEncriptado)).thenReturn(codigoPuro);
        when(bffService.obtenerClienteConProductos(codigoPuro))
                .thenReturn(Mono.error(new ClienteNotFoundException("Cliente no encontrado")));

        webTestClient.get()
                .uri("/api/clientes/{codigo}", codigoEncriptado)
                .header("Authorization", validToken)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("error");
    }
}