package com.banco.cliente.repository;

import com.banco.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por código único
     */
    Optional<Cliente> findByCodigoUnico(String codigoUnico);

    /**
     * Busca un cliente por número de documento
     */
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
}