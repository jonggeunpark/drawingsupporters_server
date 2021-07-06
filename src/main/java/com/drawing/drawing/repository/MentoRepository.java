package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Mento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentoRepository extends JpaRepository<Mento, Long> {

    Optional<Mento> findOneByEmail(String email);
}
