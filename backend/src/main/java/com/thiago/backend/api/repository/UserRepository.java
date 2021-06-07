package com.thiago.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thiago.backend.api.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

    public User findByEmail(String email); // O SpringData já reconhece e implementa esse método automaticamente
}
