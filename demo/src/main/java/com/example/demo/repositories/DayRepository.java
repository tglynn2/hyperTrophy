package com.example.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.Day;

public interface DayRepository extends CrudRepository<Day, UUID> {
    public Optional<Day> findById(UUID id);

    public Optional<Day> getDayByName(String name);

    public void deleteById(UUID id);
    
}
