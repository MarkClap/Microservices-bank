package com.banco.productos.controller;

import com.banco.productos.dto.ProductoDTO;
import com.banco.productos.dto.ProductoRequest;
import com.banco.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestionar productos financieros")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Obtener productos de un cliente
     */
    @GetMapping("/cliente/{codigoCliente}")
    @Operation(
            summary = "Obtener productos de un cliente",
            description = "Retorna todos los productos de un cliente basado en su código único"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ProductoDTO>> obtenerPorCodigoCliente(
            @Parameter(description = "Código único del cliente")
            @PathVariable String codigoCliente) {

        log.info("GET /api/productos/cliente/{} - Solicitado", codigoCliente);
        List<ProductoDTO> productos = productoService.obtenerPorCodigoCliente(codigoCliente);
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtener producto por ID
     */
    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ProductoDTO> obtenerPorId(
            @Parameter(description = "ID del producto")
            @PathVariable Long id) {

        log.info("GET /api/productos/id/{} - Solicitado", id);
        ProductoDTO producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * Obtener todos los productos
     */
    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        log.info("GET /api/productos - Obteniendo lista completa");
        List<ProductoDTO> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    /**
     * Crear nuevo producto
     */
    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ProductoDTO> crear(
            @RequestBody ProductoRequest request) {

        log.info("POST /api/productos - Creando nuevo producto");
        ProductoDTO producto = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    /**
     * Actualizar producto
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<ProductoDTO> actualizar(
            @Parameter(description = "ID del producto")
            @PathVariable Long id,
            @RequestBody ProductoRequest request) {

        log.info("PUT /api/productos/{} - Actualizando", id);
        ProductoDTO producto = productoService.actualizar(id, request);
        return ResponseEntity.ok(producto);
    }

    /**
     * Eliminar producto
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto")
            @PathVariable Long id) {

        log.info("DELETE /api/productos/{} - Eliminando", id);
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check
     */
    @GetMapping("/health/check")
    @Operation(summary = "Verificar salud del servicio")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Productos Microservice está funcionando");
    }
}