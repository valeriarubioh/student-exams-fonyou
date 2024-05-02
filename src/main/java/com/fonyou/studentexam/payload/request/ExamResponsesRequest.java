package com.fonyou.studentexam.payload.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExamResponsesRequest {
    @Positive
    @NotNull
    private Long questionId;
    @Positive
    @Min(1)
    @Max(4)
    private int studentResponse;
}
