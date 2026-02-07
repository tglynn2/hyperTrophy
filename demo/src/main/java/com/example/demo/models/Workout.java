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
import jakarta.persistence.Table;

@Entity
@Table(name = "workouts")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @ManyToOne
    @JoinColumn(name = "d_id")
    @JsonBackReference
    Day day;
    String name;
    @OneToMany(mappedBy = "workout",cascade = CascadeType.ALL)
    @JsonManagedReference
    List<GymSet> sets;

    public Workout(){
        
    }

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public Day getDay(){
        return day;
    }

    public void setDay(Day day){
        this.day = day;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<GymSet> getSets(){
        return sets;
    }

    public void setSets(List<GymSet> sets){
        this.sets = sets;
    }
}
