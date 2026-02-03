package com.example.SorokinSpringBoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(
            @PathVariable("id") Long id
    ){
        logger.info("Called getReservationById " + id);
        return reservationService.getReservationById(id);
    }

    @GetMapping()
    public List<Reservation> getAllReservations(){
        logger.info("Called getAllReservations");
        return reservationService.findAllReservations();
    }
}
