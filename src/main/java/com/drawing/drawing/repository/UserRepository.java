package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;


public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Optional<User> findOneByEmail(String email);

    @Query(value = "SELECT dtype FROM user WHERE email = :email", nativeQuery = true)
    String getDtypeByEmail(@Param("email") String email);

    Optional<Mentee> findOneByNickname(String nickname);

}
