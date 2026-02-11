package com.example.SorokinSpringBoot.reservations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable("id") Long id
    ){
        logger.info("Called getReservationById " + id);
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationById(id));
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
            @RequestParam("roomId") Long roomId,
            @RequestParam("userId") Long userId,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ){
        logger.info("Called getAllReservations");
        var filter = new ReservationSearchFilter(
                roomId,
                userId,
                pageSize,
                pageNumber
        );
        return ResponseEntity.ok(reservationService.searchAllByFilter(
               filter
        ));
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservationToCreate
    ){
        logger.info("Called createReservation " + reservationToCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.createReservation(reservationToCreate));
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Reservation> editReservation(
            @PathVariable ("id") Long id,
            @RequestBody Reservation reservationToEdit
    ){
        logger.info("Called editReservation " + reservationToEdit);
        var updated = reservationService.editReservation(id, reservationToEdit);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable("id") Long id
    ){
        logger.info("Called deleteReservation " + id);
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(
            @PathVariable("id") Long id
    ){
        logger.info("Called approveReservation " + id);
        var reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(reservation);
    }
}
