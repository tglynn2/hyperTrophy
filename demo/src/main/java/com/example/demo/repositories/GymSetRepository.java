package com.example.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.GymSet;

public interface GymSetRepository extends CrudRepository<GymSet, UUID>{
    public Optional<GymSet> findById(UUID id);
    
    public void deleteById(UUID id);
}
