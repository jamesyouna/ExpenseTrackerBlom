package com.BLOM.expensetrackerapi.repository;

import com.BLOM.expensetrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);


    List<User> findByEmailContainingIgnoreCase(String email);
}
