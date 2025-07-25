package com.example.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.store.entities.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

}
