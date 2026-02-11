package com.example.SorokinSpringBoot.reservations.availability;

import com.example.SorokinSpringBoot.reservations.ReservationRepository;
import com.example.SorokinSpringBoot.reservations.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationAvailabilityService {

    private static final Logger log = LoggerFactory.getLogger(ReservationAvailabilityService.class);

    private ReservationRepository repository;

    public ReservationAvailabilityService(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }

    public boolean isReservationAvailable(
            Long roomId,
            LocalDate startDate,
            LocalDate endDate
    ){
        if(!endDate.isAfter(startDate))
            throw new IllegalArgumentException("Start date must be after end date!");

        List<Long> conflictingIds = repository.findConflictReservationsIds(
                roomId, startDate, endDate, ReservationStatus.CONFIRMED
        );

        if(conflictingIds.isEmpty()) return true;
        log.info("Conflict with id's: "  +  conflictingIds);
        return false;
    }

}
