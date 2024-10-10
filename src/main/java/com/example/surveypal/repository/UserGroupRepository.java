package com.example.surveypal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.surveypal.model.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
