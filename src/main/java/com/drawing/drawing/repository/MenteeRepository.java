package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<Mentee> findOneWithAuthoritiesByEmail(String email);

    Optional<Mentee> findOneByNickname(String nickname);

    Optional<Mentee> findOneByEmail(String email);
}
