package com.example.demo.models;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "u_id")
    @JsonBackReference
    private User user;
    private String name;
    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Workout> workouts;

    public Day(){
        
    }

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Workout> getWorkouts(){
        return workouts;
    }

    public void setWorkout(List<Workout> workouts){
        this.workouts = workouts;
    }
}
