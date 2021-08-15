package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.DrawingStatus;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {

    List<Drawing> findAllByOrderById();

    List<Drawing> findAllByDrawingStatusOrderById(DrawingStatus status);
}
