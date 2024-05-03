package com.fonyou.studentexam.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fonyou.studentexam.entities.QuestionEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ExamResponse {
    private Long id;
    private String examName;
    @JsonIgnoreProperties("exam")
    private List<QuestionEntity> questions;
}
