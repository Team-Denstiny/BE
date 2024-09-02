package com.example.domain.dentist.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalizedDentLocDto {

    @NotNull
    private String day;
    @NotNull
    @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalTime localTime;
    @NotNull
    private String gu;
    @NotBlank
    private Double latitude;
    @NotBlank
    private Double longitude;
}
