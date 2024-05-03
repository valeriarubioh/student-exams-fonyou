package com.fonyou.studentexam.payload.response;

import com.fonyou.studentexam.entities.StudentEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ExamScheduleResponse {
    private Long id;
    private StudentEntity student;
    private Long idExam;
    private String examName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
