package com.example.SorokinSpringBoot.reservations;

import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public Reservation toDomain(
            ReservationEntity reservation
    ){
        return new Reservation(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getRoomId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus()
        );
    }

    public Reservation toEntity(
            Reservation reservation
    ){
        return new Reservation(
                reservation.id(),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                reservation.status()
        );
    }

}
