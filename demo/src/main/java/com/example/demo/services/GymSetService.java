package com.example.demo.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.GymSet;
import com.example.demo.repositories.GymSetRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class GymSetService {
    private final GymSetRepository gymSetRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GymSetService(GymSetRepository gymSetRepository){
        this.gymSetRepository = gymSetRepository;
    }

    public GymSetService(GymSetRepository gymSetRepository, EntityManager entityManager){
        this.gymSetRepository = gymSetRepository;
        this.entityManager = entityManager;
    }

    public GymSet saveGymSet(GymSet gymSet){
        return gymSetRepository.save(gymSet);
    }

    public Optional<GymSet> findById(UUID id){
        return gymSetRepository.findById(id);
    }

    public GymSet updateGymSetById(UUID id, GymSet gymSet){
        Optional<GymSet> opt = gymSetRepository.findById(id);
        if(opt.isEmpty()){
            return null;
        }
        GymSet g = opt.get();
        g.setDate(gymSet.getDate());
        g.setReps(gymSet.getReps());
        g.setWeight(gymSet.getWeight());
        g.setWorkout(gymSet.getWorkout());
        return gymSetRepository.save(g);
    }

}
