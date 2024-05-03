package com.fonyou.studentexam.controllers;

import com.fonyou.studentexam.entities.ExamEntity;
import com.fonyou.studentexam.payload.request.ExamQuestionsRequest;
import com.fonyou.studentexam.payload.response.ExamResponse;
import com.fonyou.studentexam.services.ExamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exams")
@AllArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamResponse> createExamQuestions(@RequestBody @Valid ExamQuestionsRequest examQuestionsRequestList) {
        return new ResponseEntity<>(examService.createExamQuestions(examQuestionsRequestList),HttpStatus.CREATED);
    }
}