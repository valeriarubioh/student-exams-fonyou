package com.fonyou.studentexam.payload.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExamScheduleRequest {

    @NotNull
    @Positive
    private Long examId;
    @NotNull
    @Positive
    private Long studentId;
    @FutureOrPresent
    private LocalDateTime startDateTime;
    @FutureOrPresent
    private LocalDateTime endDateTime;
}
