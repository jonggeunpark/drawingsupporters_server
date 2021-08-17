package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.DrawingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {

    List<Drawing> findAllByOrderById();

    List<Drawing> findAllByDrawingStatusOrderById(DrawingStatus status);
}
