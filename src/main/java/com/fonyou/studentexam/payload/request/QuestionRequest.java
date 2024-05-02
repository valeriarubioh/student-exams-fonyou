package com.fonyou.studentexam.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionRequest {
    @NotEmpty
    private String question;
    @NotEmpty
    private String choice1;
    @NotEmpty
    private String choice2;
    @NotEmpty
    private String choice3;
    @NotEmpty
    private String choice4;
    @Min(value = 1, message = "Choice selected not found")
    @Max(value = 4, message = "Choice selected not found")
    private int correctChoice;
    @Positive
    @Min(1)
    @Max(99)
    private int score;
}
