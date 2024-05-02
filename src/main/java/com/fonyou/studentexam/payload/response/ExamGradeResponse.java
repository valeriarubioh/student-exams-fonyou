package com.fonyou.studentexam.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExamGradeResponse {
    private ExamResponse exam;
    private List<StudentResponseResponse> response;
    private int grade;
}
