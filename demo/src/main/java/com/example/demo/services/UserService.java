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
        Optional<User> opt = findById(id);
        if(opt.isEmpty()){
            return null;
        }
        User u = opt.get();
        u.setUsername(newUsername);
        return userRepository.save(u);
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
            return null;
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
        return null;
    }

    public Workout createWorkoutForUser(UUID id, String dayName, String workoutName) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            return null;
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
        return null;
    }

    public GymSet createSetForUser(UUID id, String dayName, String workoutName, GymSet gymSet) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
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
        Day foundDay = user.getParticularDay(dayName);
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
        Day foundDay = user.getParticularDay(dayName);
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
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
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
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            return null;
        }
        List<GymSet> foundSets = foundWorkout.getSetsByDate(date);
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
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            return null;
        }
        return foundWorkout.getSets();
    }

    public Day updateDayForUser(UUID id, String oldDayName, String newDayName) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getParticularDay(oldDayName);
        if(foundDay == null){
            return null;
        }
        foundDay.setName(newDayName);
        user.getDays().add(foundDay);
        saveUser(user);
        return user.getDays().getLast();
    }

    public Workout updateWorkoutForUser(UUID id, String dayName, String oldWorkoutName, String newWorkoutName) {
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getParticularWorkout(oldWorkoutName);
        if(foundWorkout == null){
            return null;
        }
        foundWorkout.setName(newWorkoutName);
        foundDay.getWorkouts().add(foundWorkout);
        user.getDays().add(foundDay);
        saveUser(user);
        return foundWorkout;
    }

    public GymSet updateGymsetForUser(UUID userId, String dayName, String workoutName, UUID setId, GymSet gymSet){
        User user = findById(userId).orElse(null);
        if(user == null){
            return null;
        }
        Day foundDay = user.getParticularDay(dayName);
        if(foundDay == null){
            return null;
        }
        Workout foundWorkout = foundDay.getParticularWorkout(workoutName);
        if(foundWorkout == null){
            return null;
        }
        GymSet foundSet = foundWorkout.getSetById(setId);
        if(foundSet == null){
            return null;
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
            return null;
        }
        Day dayToDelete = user.getParticularDay(dayName);
        if(dayToDelete == null){
            return null;
        }
        Boolean result = user.getDays().remove(dayToDelete);
        return result;
    }

    @Transactional
    public Boolean deleteWorkout(UUID id, String dayName, String workoutName){
        User user = findById(id).orElse(null);
        if(user == null){
            return null;
        }
        Day dayWithWorkout = user.getParticularDay(dayName);
        if(dayWithWorkout == null){
            return null;
        }
        Workout workoutToDelete = dayWithWorkout.getParticularWorkout(workoutName);
        if(workoutToDelete == null){
            return null;
        }
        Boolean result = dayWithWorkout.getWorkouts().remove(workoutToDelete);
        return result;
    }

    @Transactional
    public Boolean deleteSet(UUID userId, String dayName, String workoutName, UUID setId){
        User user = findById(userId).orElse(null);
        if(user == null){
            return null;
        }
        Day dayWithWorkout = user.getParticularDay(dayName);
        if(dayWithWorkout == null){
            return null;
        }
        Workout workoutWithSet = dayWithWorkout.getParticularWorkout(workoutName);
        if(workoutWithSet == null){
            return null;
        }
        GymSet setToDelete = workoutWithSet.getSetById(setId);
        if(setToDelete == null){
            return null;
        }
        Boolean result = workoutWithSet.getSets().remove(setToDelete);
        return result;
    }
}
