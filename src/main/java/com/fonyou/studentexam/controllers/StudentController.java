package com.fonyou.studentexam.controllers;

import com.fonyou.studentexam.entities.StudentEntity;
import com.fonyou.studentexam.payload.request.StudentRequest;
import com.fonyou.studentexam.services.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentEntity> createStudent(@RequestBody @Valid StudentRequest student) {
        StudentEntity createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }
}