package com.challenge.spotify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private int status;
}
