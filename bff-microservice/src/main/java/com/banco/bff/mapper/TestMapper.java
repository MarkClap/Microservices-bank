package com.banco.bff.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {

    default String test() {
        return "OK";
    }
}
