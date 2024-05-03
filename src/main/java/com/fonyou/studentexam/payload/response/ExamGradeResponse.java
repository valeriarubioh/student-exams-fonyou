package com.fonyou.studentexam.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExamGradeResponse {
    private Long examGradeId;
    private int grade;
    private String examName;
    private List<QuestionAndResponse> questionsAndResponses;
}
