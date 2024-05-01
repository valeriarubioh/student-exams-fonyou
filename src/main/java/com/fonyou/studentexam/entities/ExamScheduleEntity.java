package com.fonyou.studentexam.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_schedules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "examId", nullable = false)
    private ExamEntity exam;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
    private StudentEntity student;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}