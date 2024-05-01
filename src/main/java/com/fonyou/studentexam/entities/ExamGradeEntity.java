package com.fonyou.studentexam.entities;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "exam_grades")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamGradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "examScheduleId", nullable = false)
    private ExamScheduleEntity examSchedule;

    private int grade;
}