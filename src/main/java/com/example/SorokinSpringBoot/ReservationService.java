package com.example.SorokinSpringBoot;

import com.example.SorokinSpringBoot.enums.ReservationStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap =  Map.of(
            1L, new Reservation(
                    1L,
                    1L,
                    40L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.CONFIRMED
            ),
            2L, new Reservation(
                    2L,
                    10L,
                    20L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.CONFIRMED
            ),
            3L, new Reservation(
                    3L,
                    4L,
                    100L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.CONFIRMED
            )
    );

    public Reservation getReservationById(Long id){

        if(!reservationMap.containsKey(id)){
            throw new NoSuchElementException("Reservation " + id + " not found");
        }

        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    }
}
