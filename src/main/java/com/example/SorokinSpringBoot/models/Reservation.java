package com.example.SorokinSpringBoot.models;

import com.example.SorokinSpringBoot.enums.ReservationStatus;

import java.time.LocalDate;

public record Reservation(
        Long id,
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {



}
