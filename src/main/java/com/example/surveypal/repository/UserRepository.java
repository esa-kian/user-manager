package com.example.surveypal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.surveypal.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
