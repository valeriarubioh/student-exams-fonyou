package com.fonyou.studentexam.repositories;

import com.fonyou.studentexam.entities.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

}