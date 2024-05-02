package com.fonyou.studentexam.controllers;

import com.fonyou.studentexam.entities.ExamEntity;
import com.fonyou.studentexam.entities.ExamGradeEntity;
import com.fonyou.studentexam.entities.ExamScheduleEntity;
import com.fonyou.studentexam.entities.StudentResponseEntity;
import com.fonyou.studentexam.payload.request.ExamResponsesRequest;
import com.fonyou.studentexam.payload.request.ExamScheduleRequest;
import com.fonyou.studentexam.services.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-schedules")
public class ExamScheduleController {

    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<ExamScheduleEntity> createExam(@RequestBody @Valid ExamScheduleRequest examScheduleRequest) {
        ExamScheduleEntity examScheduleEntity = examService.createExamSchedule(examScheduleRequest);
        return ResponseEntity.ok(examScheduleEntity);
    }

    @PostMapping("/{examScheduleId}/responses")
    public ResponseEntity<List<StudentResponseEntity>> submitExam(@PathVariable("examScheduleId") @Valid Long examScheduleId,
                                                 @RequestBody @Valid List<ExamResponsesRequest> examResponsesRequestList) {
        List<StudentResponseEntity> responseObj = examService.submitExamResponses(examScheduleId, examResponsesRequestList);
        return ResponseEntity.ok(responseObj);

    }


    @GetMapping("/{examScheduleId}/grade")
    public ResponseEntity<ExamGradeEntity> getExamGrade(@PathVariable("examScheduleId") Long examScheduleId) {
        return ResponseEntity.ok(examService.getExamGrade(examScheduleId));
    }

}
