package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Jpa service for obtain user data from db
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * The method to obtain user data by specific uuid
     *
     * @param uuid - unique identifier of user
     * @return optional data
     * @see User
     */
    Optional<User> findByUuid(String uuid);
}
