package com.example.demo.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Day;
import com.example.demo.repositories.DayRepository;

import jakarta.persistence.EntityManager;

@Service
public class DayService {
    private final DayRepository dayRepository;

    @PersistanceContext
    private EntityManager entityManager;

    @Autowired
    public DayService(DayRepository dayRepository){
        this.dayRepository = dayRepository;
    }

    public DayService(DayRepository dayRepository, EntityManager entityManager){
        this.dayRepository = dayRepository;
        this.entityManager = entityManager;
    }

    public Day saveDay(Day day){
        return dayRepository.save(day);
    }

    //To Do: Add error handling based on null return for all services really
    public Optional<Day> getDayById(UUID id){
        return dayRepository.findById(id);
    }

    public Optional<Day> getDayByName(String name){
        return dayRepository.getDayByName(name);
    }

    public Day updateDayById(UUID id, Day day){
        Optional<Day> opt = dayRepository.findById(id);
        if(opt.isEmpty()){
            return null;
        }
        Day d = opt.get();
        d.setName(day.getName());
        d.setUser(day.getUser());
        d.setWorkout(day.getWorkouts());
        return dayRepository.save(d);
    }

    public Boolean deleteDayById(UUID id){
        Optional<Day> opt = dayRepository.findById(id);
        if(opt.isEmpty()){
            return false;
        }
        dayRepository.deleteById(id);
        return true;
    }

}
