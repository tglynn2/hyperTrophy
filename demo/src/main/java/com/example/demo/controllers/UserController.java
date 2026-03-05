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
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired 
    UserService userService;

    //Test for new machine

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
    public ResponseEntity<?> createDay(HttpServletRequest request, @RequestParam(required = true) String name){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        Day day = userService.createDayForUser(id,name);
        if(day == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(day);
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
        Workout workout = userService.createWorkoutForUser(id, dayName, workoutName);
        if(workout == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Day doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(workout);
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
            GymSet g = userService.createSetForUser(id, dayName, workoutName, gymSet);
            if(g == null){
                //This is gross, will eventually throw an exception with information about which wasn't found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User, Day, or Workout doesn't exist");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(g);
               
        }
}
