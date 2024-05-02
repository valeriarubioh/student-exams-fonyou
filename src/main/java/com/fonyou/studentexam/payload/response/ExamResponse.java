package com.fonyou.studentexam.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ExamResponse {
    private Long id;
    private String examName;
    private List<QuestionResponse> questions;
}
