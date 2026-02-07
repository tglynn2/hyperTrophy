package com.example.demo.services;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Workout;
import com.example.demo.repositories.WorkoutRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository){
        this.workoutRepository = workoutRepository;
    }

    public WorkoutService(WorkoutRepository workoutRepository, EntityManager entityManager){
        this.workoutRepository = workoutRepository;
        this.entityManager = entityManager;
    }

    public Workout saveWorkout(Workout workout){
        return workoutRepository.save(workout);
    }

    public Optional<Workout> findById(UUID id){
        return workoutRepository.findById(id);
    }

    public Optional<Workout> findByName(String name){
        return workoutRepository.findByName(name);
    }

    public Workout updateWorkoutById(UUID id, Workout workout){
        Optional<Workout> opt = workoutRepository.findById(id);
        if(opt.isEmpty()){
            return null;
        }
        Workout w = opt.get();
        w.setDay(workout.getDay());
        w.setName(workout.getName());
        w.setSets(workout.getSets());
        return workoutRepository.save(w);
    }

    public Boolean deleteWorkoutById(UUID id){
        Optional<Workout> opt = workoutRepository.findById(id);
        if(opt.isEmpty()){
            return false;
        }
        workoutRepository.deleteById(id);
        return true;
    }
    
}
