package com.fonyou.studentexam.services;

import com.fonyou.studentexam.entities.StudentEntity;
import com.fonyou.studentexam.payload.request.StudentRequest;
import com.fonyou.studentexam.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentEntity createStudent(StudentRequest studentRequest) {
        StudentEntity studentEntity = StudentEntity.builder()
                .name(studentRequest.getName())
                .age(studentRequest.getAge())
                .city(studentRequest.getCity())
                .cityTimeZone(studentRequest.getCityTimeZone()).build();
        return studentRepository.save(studentEntity);
    }
}
