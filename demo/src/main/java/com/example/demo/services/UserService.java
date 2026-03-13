package com.example.demo.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Day;
import com.example.demo.models.GymSet;
import com.example.demo.models.User;
import com.example.demo.models.Workout;
import com.example.demo.repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

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

    public User updateUserById(UUID id, String newUsername){
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        User doesThisAlreadyExist = getUserByUsername(newUsername).orElse(null);
        if(doesThisAlreadyExist == null){
            user.setUsername(newUsername);
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("This username is already taken");
    }

    public User createUser(String username) {
        User user = getUserByUsername(username).orElse(null);
        if(user == null){
            User newUser = new User();
            newUser.setUsername(username);
            saveUser(newUser);
            return newUser;
        }      
        return null;
    }

    public Day createDayForUser(UUID id, String name) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("A user with this name does not exist");
        }
        Day doesThisExist = user.getParticularDay(name);
        if(doesThisExist==null){
            Day day = new Day();
            day.setName(name);
            day.setUser(user);
            user.getDays().add(day);
            saveUser(user);
            return user.getDays().getLast();
        }  
        throw new IllegalArgumentException("A day with this name already exists");
    }

    public Workout createWorkoutForUser(UUID id, String dayName, String workoutName) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout doesThisExist = foundDay.getParticularWorkout(workoutName);
        if(doesThisExist == null){
            Workout workout = new Workout();
            workout.setDay(foundDay);
            workout.setName(workoutName);
            foundDay.getWorkouts().add(workout);
            user.getDays().add(foundDay);
            saveUser(user);
            return user.getDays().getLast().getWorkouts().getLast();
        }
        throw new IllegalArgumentException("A workout with this name already exists");
    }

    public GymSet createSetForUser(UUID id, String dayName, String workoutName, GymSet gymSet) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            throw new NullPointerException("A workout with this name does not exist");
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
            throw new NullPointerException("This user does not exist");
        }
        return user.getDays();    
    }

    public Day getDayForUser(UUID id, String dayName) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        return foundDay;
    }

    public List<Workout> getWorkoutsForUser(UUID id, String dayName) {
         User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        return foundDay.getWorkouts();  
    }

    public Workout getWorkoutForUser(UUID id, String dayName, String workoutName) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            throw new NullPointerException("A workout with this name does not exist");
        }
        return foundWorkout;
    }

    public List<GymSet> getSetsForUser(UUID id, String dayName, String workoutName, Date date) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            throw new NullPointerException("A workout with this name does not exist");
        }
        return foundWorkout.getSetsByDate(date);
    }

    public List<GymSet> getAllSetsForUser(UUID id, String dayName, String workoutName) {
         User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            throw new NullPointerException("A workout with this name does not exist");
        }
        return foundWorkout.getSets();
    }

    public Day updateDayForUser(UUID id, String oldDayName, String newDayName) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(oldDayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Day doesThisExistAlready = user.getParticularDay(newDayName);
        if(doesThisExistAlready == null){
            foundDay.setName(newDayName);
            user.getDays().add(foundDay);
            saveUser(user);
            return user.getDays().getLast();
        }
        throw new IllegalArgumentException("A day with this name already exists");
    }

    public Workout updateWorkoutForUser(UUID id, String dayName, String oldWorkoutName, String newWorkoutName) {
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout foundWorkout = foundDay.getParticularWorkout(oldWorkoutName);
        if(foundWorkout == null){
            throw new NullPointerException("A workout with this name does not exist");
        }
        Workout doesThisAlreadyExist = foundDay.getParticularWorkout(newWorkoutName);
        if(doesThisAlreadyExist == null){
            foundWorkout.setName(newWorkoutName);
            foundDay.getWorkouts().add(foundWorkout);
            user.getDays().add(foundDay);
            saveUser(user);
            return foundWorkout;
        }
        throw new IllegalArgumentException("A workout with this name already exists");
    }

    public GymSet updateGymsetForUser(UUID userId, String dayName, String workoutName, UUID setId, GymSet gymSet){
        User user = findById(userId).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            throw new NullPointerException("A workout with this name does not exist");
        }
        GymSet foundSet = foundWorkout.getSetById(setId);
        if(foundSet == null){
            throw new NullPointerException("A set with this id does not exist");
        }
        foundSet.setDate(gymSet.getDate());
        foundSet.setReps(gymSet.getReps());
        foundSet.setWeight(gymSet.getWeight());
        foundWorkout.getSets().add(foundSet);
        foundDay.getWorkouts().add(foundWorkout);
        user.getDays().add(foundDay);
        saveUser(user);
        return foundSet;    
    }
    
    @Transactional
    public Boolean deleteUser(UUID id){
        User user = findById(id).orElse(null);
        if(user == null){
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Boolean deleteDay(UUID id, String dayName){
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day dayToDelete = user.getParticularDay(dayName);
        if(dayToDelete == null){
            throw new NullPointerException("This day does not exist");
        }
        return user.getDays().remove(dayToDelete);
    }

    @Transactional
    public Boolean deleteWorkout(UUID id, String dayName, String workoutName){
        User user = findById(id).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day dayWithWorkout = user.getParticularDay(dayName);
        if(dayWithWorkout == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout workoutToDelete = dayWithWorkout.getParticularWorkout(workoutName);
        if(workoutToDelete == null){
            throw new NullPointerException("A workout with this name does not exist");
        }
        return dayWithWorkout.getWorkouts().remove(workoutToDelete);
    }

    @Transactional
    public Boolean deleteSet(UUID userId, String dayName, String workoutName, UUID setId){
        User user = findById(userId).orElse(null);
        if(user == null){
            throw new NullPointerException("This user does not exist");
        }
        Day dayWithWorkout = user.getParticularDay(dayName);
        if(dayWithWorkout == null){
            throw new NullPointerException("A day with this name does not exist");
        }
        Workout workoutWithSet = dayWithWorkout.getParticularWorkout(workoutName);
        if(workoutWithSet == null){
            throw new NullPointerException("A workout with this name does not exist");
        }
        GymSet setToDelete = workoutWithSet.getSetById(setId);
        if(setToDelete == null){
            throw new NullPointerException("A set with this id does not exist");
        }
        return workoutWithSet.getSets().remove(setToDelete);
    }
}