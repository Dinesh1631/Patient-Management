package com.pm.authservice.Repository;

import com.pm.authservice.Modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
