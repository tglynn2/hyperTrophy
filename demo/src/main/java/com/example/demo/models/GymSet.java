package com.example.demo.models;

import java.sql.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class GymSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @ManyToOne
    @JoinColumn(name = "w_id")
    @JsonBackReference
    Workout workout;
    int reps;
    float weight;
    Date date;

    public GymSet(){
        
    }

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public Workout getWorkout(){
        return workout;
    }

    public void setWorkout(Workout workout){
        this.workout = workout;
    }

    public int getReps(){
        return reps;
    }

    public void setReps(int reps){
        this.reps = reps;
    }

    public float getWeight(){
        return weight;
    }

    public void setWeight(float weight){
        this.weight = weight;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }
}
