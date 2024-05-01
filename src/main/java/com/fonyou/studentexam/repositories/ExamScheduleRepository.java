package com.fonyou.studentexam.repositories;

import com.fonyou.studentexam.entities.ExamScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamScheduleRepository extends JpaRepository<ExamScheduleEntity, Long> {
}
