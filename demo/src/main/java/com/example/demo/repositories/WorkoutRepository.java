package com.example.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.Workout;

public interface WorkoutRepository extends CrudRepository<Workout,UUID> {
    public Optional<Workout> findById(UUID id);

    public Optional<Workout> findByName(String name);

    public void deleteById(UUID id);
}
