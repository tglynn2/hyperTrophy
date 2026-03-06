package com.example.demo.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/create/set")
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

    @GetMapping("/search/days")
    public ResponseEntity<?> getDays(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        List<Day> days = userService.getDaysForUser(id);
        if(days == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Found no user or days");
        }
        return ResponseEntity.status(HttpStatus.OK).body(days);
    }
    
    @GetMapping("/search/day")
    public ResponseEntity<?> getDay(HttpServletRequest request, @RequestParam(required = true) String dayName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        Day day = userService.getDayForUser(id,dayName);
        if(day == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Found no user or day");
        }
        return ResponseEntity.status(HttpStatus.OK).body(day);
    }

    @GetMapping("/search/workouts")
    public ResponseEntity<?> getWorkouts(HttpServletRequest request, @RequestParam(required = true) String dayName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        List<Workout> workouts = userService.getWorkoutsForUser(id,dayName);
        if(workouts == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Found no user, days, or workouts");
        }
        return ResponseEntity.status(HttpStatus.OK).body(workouts);
    }
    
    @GetMapping("/search/workout")
    public ResponseEntity<?> getWorkout(HttpServletRequest request, @RequestParam(required = true) String dayName, @RequestParam(required = true) String workoutName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        Workout workout = userService.getWorkoutForUser(id,dayName,workoutName);
        if(workout == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Found no user, days, or workout");
        }
        return ResponseEntity.status(HttpStatus.OK).body(workout);
    }

    @GetMapping("/search/sets")
    public ResponseEntity<?> getSets(HttpServletRequest request, @RequestParam(required = true) String dayName, @RequestParam(required = true) String workoutName, @RequestParam Date date){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        List<GymSet> sets = userService.getSetsForUser(id, dayName, workoutName, date);
         if(sets == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Found no user, days, workouts, or sets");
        }
        return ResponseEntity.status(HttpStatus.OK).body(sets);
    }

     @GetMapping("/search/all-sets")
    public ResponseEntity<?> getAllSets(HttpServletRequest request, @RequestParam(required = true) String dayName, @RequestParam(required = true) String workoutName){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("id") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login first");
        }
        UUID id = UUID.fromString(session.getAttribute("id").toString());
        List<GymSet> sets = userService.getAllSetsForUser(id, dayName, workoutName);
         if(sets == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Found no user, days, workouts, or sets");
        }
        return ResponseEntity.status(HttpStatus.OK).body(sets);
    }


}
