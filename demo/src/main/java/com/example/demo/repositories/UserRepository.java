package com.example.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.User;

public interface UserRepository extends CrudRepository<User,UUID> {
    public Optional<User> getUserByUsername(String username);

    public Optional<User> findById(UUID id);

    public void deleteById(UUID id);
}
