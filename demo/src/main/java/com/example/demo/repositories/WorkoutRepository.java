package com.example.demo.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.Workout;

public interface WorkoutRepository extends CrudRepository<Workout,UUID> {
    
}
