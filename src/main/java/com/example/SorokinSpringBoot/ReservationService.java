package com.example.SorokinSpringBoot;

import com.example.SorokinSpringBoot.enums.ReservationStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Service
public class ReservationService {

    public Reservation getReservationById(Long id){
        return new Reservation(
                id,
                1L,
                40L,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                ReservationStatus.CONFIRMED
        );
    }

}
