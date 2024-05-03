package com.fonyou.studentexam.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentRequest {
    @NotEmpty
    @Schema(example = "Pepe Perez")
    private String name;
    @NotNull
    @Min(value = 1, message = "Age should not be less than 1")
    @Schema(example = "18")
    private int age;
    @NotEmpty
    @Schema(example = "Bogota")
    private String city;
    @NotEmpty
    @ValidTimeZone
    @Schema(example = "America/Bogota")
    private String cityTimeZone;
}
