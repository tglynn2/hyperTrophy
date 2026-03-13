package com.example.demo.controllers;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import jakarta.validation.Valid;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired 
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestParam(required = true) String username){
        User user = userService.createUser(username);
        if(user == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with that username already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam(required = true) String username,
        HttpServletRequest request){
        User user = userService.getUserByUsername(username).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");   
        }
        request.getSession().setAttribute("id", user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/create/day")
    public ResponseEntity<?> createDay(HttpServletRequest request, @RequestParam(required = true) String name){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Day day = userService.createDayForUser(id,name);
            return ResponseEntity.status(HttpStatus.CREATED).body(day);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
        try{
            Workout workout = userService.createWorkoutForUser(id, dayName, workoutName);
            return ResponseEntity.status(HttpStatus.CREATED).body(workout);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch(NullPointerException e){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/create/set")
    public ResponseEntity<?> createSet(HttpServletRequest request, 
        @RequestBody @Valid GymSet gymSet, 
        @RequestParam(required = true) String dayName, 
        @RequestParam(required = true) String workoutName){
            HttpSession session = request.getSession(false);
            if(session == null || session.getAttribute("id") == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
            }
            UUID id = UUID.fromString(session.getAttribute("id").toString());
            try{
                GymSet g = userService.createSetForUser(id, dayName, workoutName, gymSet);
                return ResponseEntity.status(HttpStatus.CREATED).body(g);
            }
            catch(NullPointerException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }  
        }

    @GetMapping("/search/days")
    public ResponseEntity<?> getDays(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            List<Day> days = userService.getDaysForUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(days);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/search/day")
    public ResponseEntity<?> getDay(HttpServletRequest request, @RequestParam(required = true) String dayName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Day day = userService.getDayForUser(id,dayName);
            return ResponseEntity.status(HttpStatus.OK).body(day);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/workouts")
    public ResponseEntity<?> getWorkouts(HttpServletRequest request, @RequestParam(required = true) String dayName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            List<Workout> workouts = userService.getWorkoutsForUser(id,dayName);
            return ResponseEntity.status(HttpStatus.OK).body(workouts);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/search/workout")
    public ResponseEntity<?> getWorkout(HttpServletRequest request,
        @RequestParam(required = true) String dayName, 
        @RequestParam(required = true) String workoutName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Workout workout = userService.getWorkoutForUser(id,dayName,workoutName);
            return ResponseEntity.status(HttpStatus.OK).body(workout);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/sets")
    public ResponseEntity<?> getSets(HttpServletRequest request,
         @RequestParam(required = true) String dayName, 
         @RequestParam(required = true) String workoutName,
         @RequestParam Date date){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            List<GymSet> sets = userService.getSetsForUser(id, dayName, workoutName, date);
            return ResponseEntity.status(HttpStatus.OK).body(sets);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/all-sets")
    public ResponseEntity<?> getAllSets(HttpServletRequest request, 
        @RequestParam(required = true) String dayName,
        @RequestParam(required = true) String workoutName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            List<GymSet> sets = userService.getAllSetsForUser(id, dayName, workoutName);
            return ResponseEntity.status(HttpStatus.OK).body(sets);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/user")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestParam(required = true) String newUsername){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            User updatedUser = userService.updateUserById(id, newUsername);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/day")
    public ResponseEntity<?> updateDay(HttpServletRequest request,
        @RequestParam(required = true) String oldDayName,
        @RequestParam(required = true) String newDayName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Day updatedDay = userService.updateDayForUser(id, oldDayName, newDayName);
            return ResponseEntity.status(HttpStatus.OK).body(updatedDay);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/workout")
    public ResponseEntity<?> updateWorkout(HttpServletRequest request,
        @RequestParam(required = true) String dayName,
        @RequestParam(required = true) String oldWorkoutName, 
        @RequestParam(required = true) String newWorkoutName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Workout updatedWorkout = userService.updateWorkoutForUser(id, dayName, oldWorkoutName, newWorkoutName);
            return ResponseEntity.status(HttpStatus.OK).body(updatedWorkout);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/set/{setId}")
    public ResponseEntity<?> updateSet(HttpServletRequest request,
        @RequestParam(required = true) String dayName, 
        @RequestParam(required = true) String workoutName, 
        @RequestBody(required = true) @Valid GymSet gymSet,
        @PathVariable("setId") UUID setId){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            GymSet updatedSet = userService.updateGymsetForUser(id, dayName, workoutName,setId,gymSet);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSet);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        Boolean result = userService.deleteUser(id);
        if(!result){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successful");
    }

    @DeleteMapping("/delete/day")
    public ResponseEntity<?> deleteDay(HttpServletRequest request, @RequestParam(required = true) String dayName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Boolean result = userService.deleteDay(id, dayName);
            if(result){
                return ResponseEntity.status(HttpStatus.OK).body("Delete Successful");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete Failed");
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/workout")
    public ResponseEntity<?> deleteWorkout(HttpServletRequest request,
        @RequestParam(required = true) String dayName,
        @RequestParam(required = true) String workoutName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Boolean result = userService.deleteWorkout(id, dayName,workoutName);
            if(result){
                return ResponseEntity.status(HttpStatus.OK).body("Delete Successful");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete Failed");
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/set/{setId}")
    public ResponseEntity<?> deleteSet(HttpServletRequest request,
        @RequestParam(required = true) String dayName,
        @RequestParam(required = true) String workoutName,
        @PathVariable("setId") UUID setId){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        try{
            Boolean result = userService.deleteSet(id, dayName,workoutName,setId);
            if(result){
                return ResponseEntity.status(HttpStatus.OK).body("Delete Successful");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete Failed");
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}