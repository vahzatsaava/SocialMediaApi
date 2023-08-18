package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findAllByName(String name);
}
