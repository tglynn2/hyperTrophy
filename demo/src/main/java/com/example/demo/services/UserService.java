package com.example.demo.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.User;
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
    
}
