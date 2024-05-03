package com.fonyou.studentexam.repositories;

import com.fonyou.studentexam.entities.ExamGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamGradeRepository extends JpaRepository<ExamGradeEntity, Long> {
    Optional<ExamGradeEntity> findByExamScheduleId(Long examScheduleId);
}
