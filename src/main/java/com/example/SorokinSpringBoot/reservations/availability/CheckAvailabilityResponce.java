package com.example.SorokinSpringBoot.reservations.availability;

public record CheckAvailabilityResponce(
        String message,
        AvailabilityStatus status
) {
}
