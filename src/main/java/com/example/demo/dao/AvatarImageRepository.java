package com.example.demo.dao;


import com.example.demo.domain.AvatarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarImageRepository extends JpaRepository<AvatarImage, Long> {

    @Query("from AvatarImage ai " +
            "where ai.user.username = :username")
    Optional<AvatarImage> findUserByUsername(@Param("username") String username);

    @Query("from AvatarImage ai " +
            "where ai.course.id = :id")
    Optional<AvatarImage> findCourseById(@Param("id") Long id);

}
