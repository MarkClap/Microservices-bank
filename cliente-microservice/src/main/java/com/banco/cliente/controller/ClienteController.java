package com.banco.cliente.controller;

import com.banco.cliente.dto.ClienteDTO;
import com.banco.cliente.dto.ClienteRequest;
import com.banco.cliente.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gestionar información de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtener cliente por código único
     */
    @GetMapping("/{codigoUnico}")
    @Operation(
            summary = "Obtener cliente por código único",
            description = "Retorna los datos del cliente basado en su código único"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ClienteDTO> obtenerPorCodigoUnico(
            @Parameter(description = "Código único del cliente")
            @PathVariable String codigoUnico) {

        log.info("GET /api/clientes/{} - Solicitado", codigoUnico);
        ClienteDTO cliente = clienteService.obtenerPorCodigoUnico(codigoUnico);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Obtener cliente por ID
     */
    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<ClienteDTO> obtenerPorId(
            @Parameter(description = "ID del cliente")
            @PathVariable Long id) {

        log.info("GET /api/clientes/id/{} - Solicitado", id);
        ClienteDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Obtener todos los clientes
     */
    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public ResponseEntity<List<ClienteDTO>> obtenerTodos() {
        log.info("GET /api/clientes - Obteniendo lista completa");
        List<ClienteDTO> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Crear nuevo cliente
     */
    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ClienteDTO> crear(
            @RequestBody ClienteRequest request) {

        log.info("POST /api/clientes - Creando nuevo cliente");
        ClienteDTO cliente = clienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    /**
     * Actualizar cliente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente")
    public ResponseEntity<ClienteDTO> actualizar(
            @Parameter(description = "ID del cliente")
            @PathVariable Long id,
            @RequestBody ClienteRequest request) {

        log.info("PUT /api/clientes/{} - Actualizando", id);
        ClienteDTO cliente = clienteService.actualizar(id, request);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Eliminar cliente
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente")
    @ApiResponse(responseCode = "204", description = "Cliente eliminado")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del cliente")
            @PathVariable Long id) {

        log.info("DELETE /api/clientes/{} - Eliminando", id);
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check
     */
    @GetMapping("/health/check")
    @Operation(summary = "Verificar salud del servicio")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Cliente Microservice está funcionando");
    }
}