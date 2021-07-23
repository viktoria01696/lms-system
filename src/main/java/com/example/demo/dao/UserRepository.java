package com.example.demo.dao;

import com.example.demo.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("from User u " +
      "where u.id not in ( " +
      "select u.id " +
      "from User u " +
      "left join u.courses c " +
      "where c.id = :courseId)")
  List<User> findUsersNotAssignedToCourse(@Param("courseId") long courseId);

}
