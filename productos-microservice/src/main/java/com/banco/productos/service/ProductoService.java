package com.banco.productos.service;

import com.banco.productos.dto.ProductoDTO;
import com.banco.productos.dto.ProductoRequest;
import com.banco.productos.entity.Producto;
import com.banco.productos.exception.ProductoNotFoundException;
import com.banco.productos.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtener productos de un cliente
     */
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerPorCodigoCliente(String codigoCliente) {
        log.info("Buscando productos para cliente: {}", codigoCliente);

        List<Producto> productos = productoRepository.findByCodigoCliente(codigoCliente);
        log.debug("Productos encontrados: {}", productos.size());

        return productos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener producto por ID
     */
    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado: {}", id);
                    return new ProductoNotFoundException("Producto no encontrado con ID: " + id);
                });

        return mapToDTO(producto);
    }

    /**
     * Obtener todos los productos
     */
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerTodos() {
        log.info("Obteniendo todos los productos");

        List<Producto> productos = productoRepository.findAll();
        log.debug("Total de productos encontrados: {}", productos.size());

        return productos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear nuevo producto
     */
    public ProductoDTO crear(ProductoRequest request) {
        log.info("Creando nuevo producto: {} para cliente: {}",
                request.getNombre(), request.getCodigoCliente());

        Producto producto = Producto.builder()
                .codigoCliente(request.getCodigoCliente())
                .tipoProducto(request.getTipoProducto())
                .nombre(request.getNombre())
                .saldo(request.getSaldo())
                .estado("ACTIVO")
                .build();

        Producto productoGuardado = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", productoGuardado.getId());

        return mapToDTO(productoGuardado);
    }

    /**
     * Actualizar producto
     */
    public ProductoDTO actualizar(Long id, ProductoRequest request) {
        log.info("Actualizando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        producto.setNombre(request.getNombre());
        producto.setTipoProducto(request.getTipoProducto());
        producto.setSaldo(request.getSaldo());

        Producto productoActualizado = productoRepository.save(producto);
        log.info("Producto actualizado exitosamente: {}", id);

        return mapToDTO(productoActualizado);
    }

    /**
     * Eliminar producto
     */
    public void eliminar(Long id) {
        log.info("Eliminando producto con ID: {}", id);

        if (!productoRepository.existsById(id)) {
            log.error("Producto no encontrado: {}", id);
            throw new ProductoNotFoundException("Producto no encontrado con ID: " + id);
        }

        productoRepository.deleteById(id);
        log.info("Producto eliminado exitosamente: {}", id);
    }
    private ProductoDTO mapToDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .codigoCliente(producto.getCodigoCliente())
                .tipoProducto(producto.getTipoProducto())
                .nombre(producto.getNombre())
                .saldo(producto.getSaldo())
                .estado(producto.getEstado())
                .build();
    }
}