package com.fonyou.studentexam.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponseResponse {
    private String question;
    private int studentResponse;
}
