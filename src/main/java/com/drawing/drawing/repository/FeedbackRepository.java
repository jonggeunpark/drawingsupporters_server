package com.drawing.drawing.repository;

import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByOrderById();
}
