package com.example.demo.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Day;
import com.example.demo.models.GymSet;
import com.example.demo.models.User;
import com.example.demo.models.Workout;
import com.example.demo.repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserService(UserRepository userRepository, EntityManager entityManager){
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public Optional<User> findById(UUID id){
        return userRepository.findById(id);
    }

    public Boolean deleteUserById(UUID id){
        Optional<User> opt = findById(id);
        if(opt.isEmpty()){
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    public User updateUserById(UUID id, User user){
        Optional<User> opt = findById(id);
        if(opt.isEmpty()){
            return null;
        }
        User u = opt.get();
        u.setDays(user.getDays());
        u.setUsername(user.getUsername());
        return userRepository.save(u);
    }

    public Day createDayForUser(UUID id, String name) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day day = new Day();
        day.setName(name);
        day.setUser(user);
        user.getDays().add(day);
        saveUser(user);
        return user.getDays().getLast();
    }

    public Workout createWorkoutForUser(UUID id, String dayName, String workoutName) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getDays().stream()
        .filter(day -> day.getName().equals(dayName)).findFirst().orElse(null);
        if(foundDay == null){
            return null;
        }
        Workout workout = new Workout();
        workout.setDay(foundDay);
        workout.setName(workoutName);
        foundDay.getWorkouts().add(workout);
        user.getDays().add(foundDay);
        saveUser(user);
        return user.getDays().getLast().getWorkouts().getLast();
    }

    public GymSet createSetForUser(UUID id, String dayName, String workoutName, GymSet gymSet) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getDays().stream()
        .filter(day -> day.getName().equals(dayName)).findFirst().orElse(null);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getWorkouts().stream()
        .filter(workout -> workout.getName().equals(workoutName)).findFirst().orElse(null);
        if(foundWorkout == null){
            return null;
        }
        gymSet.setWorkout(foundWorkout);
        foundWorkout.getSets().add(gymSet);
        foundDay.getWorkouts().add(foundWorkout);
        user.getDays().add(foundDay);
        saveUser(user);
        return user.getDays().getLast().getWorkouts().getLast().getSets().getLast();
    }

    public List<Day> getDaysForUser(UUID id) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        return user.getDays();    
    }

    public Day getDayForUser(UUID id, String dayName) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getDays().stream()
        .filter(day -> day.getName().equals(dayName)).findFirst().orElse(null);
        if(foundDay == null){
            return null;
        }
        return foundDay;
    }

    public List<Workout> getWorkoutsForUser(UUID id, String dayName) {
         User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getDays().stream()
        .filter(day -> day.getName().equals(dayName)).findFirst().orElse(null);
        if(foundDay == null){
            return null;
        }
        return foundDay.getWorkouts();  
    }

    public Workout getWorkoutForUser(UUID id, String dayName, String workoutName) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getDays().stream()
        .filter(day -> day.getName().equals(dayName)).findFirst().orElse(null);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getWorkouts().stream()
        .filter(workout -> workout.getName().equals(workoutName)).findFirst().orElse(null);
        if(foundWorkout == null){
            return null;
        }
        return foundWorkout;
    }

    public List<GymSet> getSetsForUser(UUID id, String dayName, String workoutName, Date date) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getDays().stream()
        .filter(day -> day.getName().equals(dayName)).findFirst().orElse(null);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getWorkouts().stream()
        .filter(workout -> workout.getName().equals(workoutName)).findFirst().orElse(null);
        if(foundWorkout == null){
            return null;
        }
        List<GymSet> foundSets = foundWorkout.getSets().stream()
        .filter(sets -> sets.getDate().equals(date)).collect(Collectors.toList());
        if(foundSets == null){
            return null;
        }
        return foundSets;
        
    }

    public List<GymSet> getAllSetsForUser(UUID id, String dayName, String workoutName) {
         User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getDays().stream()
        .filter(day -> day.getName().equals(dayName)).findFirst().orElse(null);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getWorkouts().stream()
        .filter(workout -> workout.getName().equals(workoutName)).findFirst().orElse(null);
        if(foundWorkout == null){
            return null;
        }
        return foundWorkout.getSets();
    }
    
}
