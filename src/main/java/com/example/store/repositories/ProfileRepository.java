package com.example.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.store.entities.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

}
