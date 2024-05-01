package com.fonyou.studentexam.repositories;

import com.fonyou.studentexam.entities.StudentResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResponseRepository extends JpaRepository<StudentResponseEntity, Long> {
}
