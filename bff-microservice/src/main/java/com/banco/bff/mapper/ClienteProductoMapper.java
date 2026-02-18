package com.banco.bff.mapper;

import com.banco.bff.dto.ClienteDTO;
import com.banco.bff.dto.ProductoDTO;
import com.banco.bff.dto.ClienteProductoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteProductoMapper {

    @Mapping(target = "cantidadProductos", expression = "java(productos.size())")
    ClienteProductoResponse toResponse(
            ClienteDTO cliente,
            List<ProductoDTO> productos
    );
}
