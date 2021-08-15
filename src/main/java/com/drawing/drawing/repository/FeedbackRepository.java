package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByOrderById();

    List<Feedback> findAllByStatusOrderById(FeedbackStatus status);

    List<Feedback> findAllByUserIdAndStatusOrderById(Long userId, FeedbackStatus status);
}
