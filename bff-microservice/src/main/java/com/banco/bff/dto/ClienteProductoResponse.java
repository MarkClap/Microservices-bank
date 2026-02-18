package com.banco.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteProductoResponse {

    private com.banco.bff.dto.ClienteDTO cliente;
    private List<com.banco.bff.dto.ProductoDTO> productos;
    private Integer cantidadProductos;
}