package com.banco.productos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_cliente", nullable = false)
    private String codigoCliente;

    @Column(name = "tipo_producto", nullable = false)
    private String tipoProducto;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false)
    private String estado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (estado == null) {
            estado = "ACTIVO";
        }
    }
}