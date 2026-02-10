package com.example.SorokinSpringBoot;

import java.time.LocalDateTime;

public record ErrorResponseDTO(String message,
                               String detailedMessage,
                               LocalDateTime errorTime) {



}
