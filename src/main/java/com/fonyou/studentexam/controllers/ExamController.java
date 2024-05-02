package com.fonyou.studentexam.controllers;

import com.fonyou.studentexam.entities.ExamEntity;
import com.fonyou.studentexam.payload.request.ExamQuestionsRequest;
import com.fonyou.studentexam.services.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<ExamEntity> createExamQuestions(@RequestBody @Valid ExamQuestionsRequest examQuestionsRequestList) {
        examService.createExamQuestions(examQuestionsRequestList);
        return new ResponseEntity<>(examService.createExamQuestions(examQuestionsRequestList),HttpStatus.CREATED);
    }
}