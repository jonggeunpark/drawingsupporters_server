package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {
}
