package com.example.demo.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Day;
import com.example.demo.models.GymSet;
import com.example.demo.models.User;
import com.example.demo.models.Workout;
import com.example.demo.services.DayService;
import com.example.demo.services.UserService;
import com.example.demo.services.WorkoutService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired 
    UserService userService;

    @Autowired
    DayService dayService;

    @Autowired
    WorkoutService workoutService;

    @PostMapping("/create")
    public User createUser(@RequestParam(required = true) String username){
        User user = new User();
        user.setUsername(username);
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestParam(required = true) String username,
        HttpServletRequest request){
        User user = userService.getUserByUsername(username).orElse(null);
        if(user != null){
            request.getSession().setAttribute("id", user.getId());
        }
        return user;
    }
    
    @PostMapping("/create/day")
    public ResponseEntity<?> createDay(HttpServletRequest request,
        @RequestParam(required = true) String name){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login First");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        User user = userService.findById(id).orElse(null);
        Day day = new Day();
        day.setName(name);
        day.setUser(user);
        List<Day> days =  user.getDays();
        days.add(day);
        user.setDays(days);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/create/workout")
    public ResponseEntity<?> createWorkout(HttpServletRequest request,
        @RequestParam(required = true) String dayName,
        @RequestParam(required = true) String workoutName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        User user = userService.findById(id).orElse(null);
        Day day = dayService.getDayByName(dayName).orElse(null);
        Workout workout = new Workout();
        workout.setDay(day);
        workout.setName(workoutName);
        day.getWorkouts().add(workout);
        user.getDays().add(day);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @RequestMapping("/create/set")
    public ResponseEntity<?> createSet(HttpServletRequest request, 
        @RequestBody GymSet gymSet, 
        @RequestParam(required = true) String dayName, 
        @RequestParam(required = true) String workoutName){
            HttpSession session = request.getSession(false);
            if(session == null || session.getAttribute("id") == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
            }
            UUID id = UUID.fromString(session.getAttribute("id").toString());
            User user = userService.findById(id).orElse(null);
            Day day = dayService.getDayByName(dayName).orElse(null);
            Workout workout = workoutService.findByName(workoutName).orElse(null);
            gymSet.setWorkout(workout);
            workout.getSets().add(gymSet);
            day.getWorkouts().add(workout);
            user.getDays().add(day);
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);    
        }
}
