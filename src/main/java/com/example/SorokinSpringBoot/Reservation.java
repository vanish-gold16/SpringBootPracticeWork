package com.example.SorokinSpringBoot;

import com.example.SorokinSpringBoot.enums.ReservationStatus;

import java.time.LocalDate;

record Reservation(
        Long id,
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {



}
