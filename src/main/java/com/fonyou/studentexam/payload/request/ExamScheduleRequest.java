package com.fonyou.studentexam.payload.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExamScheduleRequest {

    @Min(1)
    private Long examId;
    @Min(1)
    private Long studentId;
    @FutureOrPresent
    private LocalDateTime startDateTime;
    @FutureOrPresent
    private LocalDateTime endDateTime;
}
