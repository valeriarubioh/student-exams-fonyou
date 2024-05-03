package com.fonyou.studentexam.repositories;

import com.fonyou.studentexam.entities.ExamEntity;
import com.fonyou.studentexam.entities.ExamScheduleEntity;
import com.fonyou.studentexam.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamScheduleRepository extends JpaRepository<ExamScheduleEntity, Long> {
    List<ExamScheduleEntity> findByExamAndStudent(ExamEntity examEntity, StudentEntity studentEntity);
}
