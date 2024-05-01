package com.fonyou.studentexam.services;

import com.fonyou.studentexam.payload.request.StudentRequest;
import com.fonyou.studentexam.entities.StudentEntity;
import com.fonyou.studentexam.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository; // Assuming a repository class for student data access

    public StudentEntity createStudent(StudentRequest studentRequest) {
        StudentEntity studentEntity = StudentEntity.builder()
                .name(studentRequest.getName())
                .age(studentRequest.getAge())
                .city(studentRequest.getCity())
                .cityTimeZone(studentRequest.getCityTimeZone()).build();
        return studentRepository.save(studentEntity);
    }
}
