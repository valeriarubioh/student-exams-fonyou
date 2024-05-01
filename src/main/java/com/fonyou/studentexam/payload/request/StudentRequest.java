package com.fonyou.studentexam.payload.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentRequest {
    @NotEmpty
    private String name;
    @NotNull
    @Min(value = 1, message = "Age should not be less than 1")
    private int age;
    @NotEmpty
    private String city;
    @NotEmpty
    private String cityTimeZone;
}
