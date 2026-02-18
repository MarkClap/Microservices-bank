package com.banco.productos.repository;

import com.banco.productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca todos los productos de un cliente
     */
    List<Producto> findByCodigoCliente(String codigoCliente);
}