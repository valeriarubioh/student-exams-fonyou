package com.fonyou.studentexam.entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "examId", nullable = false)
    private ExamEntity exam;

    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private int correctChoice;
    private int score;
}