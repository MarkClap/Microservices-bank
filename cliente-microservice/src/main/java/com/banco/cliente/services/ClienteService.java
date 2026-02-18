package com.banco.cliente.services;

import com.banco.cliente.dto.ClienteDTO;
import com.banco.cliente.dto.ClienteRequest;
import com.banco.cliente.entity.Cliente;
import com.banco.cliente.exception.ClienteNotFoundException;
import com.banco.cliente.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Obtener cliente por código único
     */
    public ClienteDTO obtenerPorCodigoUnico(String codigoUnico) {
        log.info("Buscando cliente con código: {}", codigoUnico);

        Cliente cliente = clienteRepository.findByCodigoUnico(codigoUnico)
                .orElseThrow(() -> {
                    log.error("Cliente no encontrado: {}", codigoUnico);
                    return new ClienteNotFoundException("Cliente no encontrado con código: " + codigoUnico);
                });

        log.debug("Cliente encontrado: {} {}", cliente.getNombres(), cliente.getApellidos());
        return mapToDTO(cliente);
    }

    /**
     * Obtener cliente por ID
     */
    public ClienteDTO obtenerPorId(Long id) {
        log.info("Buscando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente no encontrado: {}", id);
                    return new ClienteNotFoundException("Cliente no encontrado con ID: " + id);
                });

        return mapToDTO(cliente);
    }

    /**
     * Obtener todos los clientes
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerTodos() {
        log.info("Obteniendo todos los clientes");

        List<Cliente> clientes = clienteRepository.findAll();
        log.debug("Total de clientes encontrados: {}", clientes.size());

        return clientes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear nuevo cliente
     */
    public ClienteDTO crear(ClienteRequest request) {
        log.info("Creando nuevo cliente: {} {}", request.getNombres(), request.getApellidos());

        // Validar que no exista otro cliente con el mismo código único
        if (clienteRepository.findByCodigoUnico(request.getCodigoUnico()).isPresent()) {
            log.error("Ya existe un cliente con código: {}", request.getCodigoUnico());
            throw new IllegalArgumentException("Ya existe un cliente con este código único");
        }

        // Validar que no exista otro cliente con el mismo documento
        if (clienteRepository.findByNumeroDocumento(request.getNumeroDocumento()).isPresent()) {
            log.error("Ya existe un cliente con documento: {}", request.getNumeroDocumento());
            throw new IllegalArgumentException("Ya existe un cliente con este número de documento");
        }

        Cliente cliente = Cliente.builder()
                .codigoUnico(request.getCodigoUnico())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .tipoDocumento(request.getTipoDocumento())
                .numeroDocumento(request.getNumeroDocumento())
                .estado("ACTIVO")
                .build();

        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getId());

        return mapToDTO(clienteGuardado);
    }

    /**
     * Actualizar cliente
     */
    public ClienteDTO actualizar(Long id, ClienteRequest request) {
        log.info("Actualizando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));

        cliente.setNombres(request.getNombres());
        cliente.setApellidos(request.getApellidos());
        cliente.setTipoDocumento(request.getTipoDocumento());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        log.info("Cliente actualizado exitosamente: {}", id);

        return mapToDTO(clienteActualizado);
    }

    /**
     * Eliminar cliente
     */
    public void eliminar(Long id) {
        log.info("Eliminando cliente con ID: {}", id);

        if (!clienteRepository.existsById(id)) {
            log.error("Cliente no encontrado: {}", id);
            throw new ClienteNotFoundException("Cliente no encontrado con ID: " + id);
        }

        clienteRepository.deleteById(id);
        log.info("Cliente eliminado exitosamente: {}", id);
    }

    private ClienteDTO mapToDTO(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .codigoUnico(cliente.getCodigoUnico())
                .nombres(cliente.getNombres())
                .apellidos(cliente.getApellidos())
                .tipoDocumento(cliente.getTipoDocumento())
                .numeroDocumento(cliente.getNumeroDocumento())
                .estado(cliente.getEstado())
                .build();
    }
}