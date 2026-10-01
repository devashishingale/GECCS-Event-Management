package com.geccs.eventmanagement.repository;

import com.geccs.eventmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCollegeEmail(String collegeEmail);

}