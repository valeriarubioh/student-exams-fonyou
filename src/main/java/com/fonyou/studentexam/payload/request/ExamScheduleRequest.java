package com.fonyou.studentexam.payload.request;

import com.fonyou.studentexam.entities.ExamEntity;
import com.fonyou.studentexam.entities.StudentEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExamScheduleRequest {

    @NotEmpty
    private Long examId;
    @NotEmpty
    private Long studentId;
    @NotEmpty
    private LocalDateTime startDateTime;
    @NotEmpty
    private LocalDateTime endDateTime;
}
