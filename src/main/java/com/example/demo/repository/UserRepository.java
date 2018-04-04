package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa service to obtain user data from db
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
