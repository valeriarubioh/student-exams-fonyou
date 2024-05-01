package com.fonyou.studentexam.payload.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Builder
@Data
public class ExamQuestionsRequest {
    @NotEmpty
    private String examName;
    @NotEmpty
    @NotNull
    private List<QuestionRequest> questions;
}
