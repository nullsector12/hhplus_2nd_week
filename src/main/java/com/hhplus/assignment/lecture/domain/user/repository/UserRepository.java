package com.hhplus.assignment.lecture.domain.user.repository;

import com.hhplus.assignment.lecture.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
