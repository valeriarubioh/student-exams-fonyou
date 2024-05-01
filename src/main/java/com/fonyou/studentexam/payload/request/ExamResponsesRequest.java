package com.fonyou.studentexam.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExamResponsesRequest {
    @NotEmpty
    private Long questionId;
    @NotEmpty
    private int studentResponse;
}
