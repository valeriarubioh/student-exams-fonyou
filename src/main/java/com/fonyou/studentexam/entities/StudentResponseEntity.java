package com.fonyou.studentexam.entities;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "student_responses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "examScheduleId", nullable = false)
    private ExamScheduleEntity examSchedule;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private QuestionEntity question;

    private int studentResponse;
}