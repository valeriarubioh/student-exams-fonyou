package com.fonyou.studentexam.controllers;

import com.fonyou.studentexam.entities.ExamGradeEntity;
import com.fonyou.studentexam.payload.request.ExamResponsesRequest;
import com.fonyou.studentexam.payload.request.ExamScheduleRequest;
import com.fonyou.studentexam.payload.response.ExamGradeResponse;
import com.fonyou.studentexam.payload.response.ExamScheduleResponse;
import com.fonyou.studentexam.services.ExamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-schedules")
@AllArgsConstructor
public class ExamScheduleController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamScheduleResponse> createExam(@RequestBody @Valid ExamScheduleRequest examScheduleRequest) {
        ExamScheduleResponse examScheduleResponse = examService.createExamSchedule(examScheduleRequest);
        return ResponseEntity.ok(examScheduleResponse);
    }

    @PostMapping("/{examScheduleId}/responses")
    public ResponseEntity<ExamGradeResponse> submitExam(@PathVariable("examScheduleId") @Valid Long examScheduleId,
                                                        @RequestBody @Valid List<ExamResponsesRequest> examResponsesRequestList) {
        ExamGradeResponse responseObj = examService.submitExamResponses(examScheduleId, examResponsesRequestList);
        return ResponseEntity.ok(responseObj);

    }


    @GetMapping("/{examGradeId}/grade")
    public ResponseEntity<ExamGradeResponse> getExamGrade(@PathVariable("examGradeId") Long examGradeId) {
        return ResponseEntity.ok(examService.getExamGrade(examGradeId));
    }

}
