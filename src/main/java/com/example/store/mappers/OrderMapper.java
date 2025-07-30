package com.example.store.mappers;

import org.mapstruct.Mapper;

import com.example.store.dtos.OrderDto;
import com.example.store.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
