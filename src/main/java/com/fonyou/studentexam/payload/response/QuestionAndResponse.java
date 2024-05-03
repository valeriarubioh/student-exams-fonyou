package com.fonyou.studentexam.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionAndResponse {
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private int correctChoice;
    private int studentResponse;
    private int score;
}
