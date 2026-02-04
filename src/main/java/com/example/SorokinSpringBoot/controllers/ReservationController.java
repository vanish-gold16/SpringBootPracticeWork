package com.example.SorokinSpringBoot.controllers;

import com.example.SorokinSpringBoot.ReservationService;
import com.example.SorokinSpringBoot.models.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(reservationService.getReservationById(id));
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping("reservations/")
    public ResponseEntity<List<Reservation>> getAllReservations(){
        logger.info("Called getAllReservations");
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @PostMapping("reservations/")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservationToCreate
    ){
        logger.info("Called createReservation " + reservationToCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.createReservation(reservationToCreate));
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<Reservation> editReservation(
            @PathVariable ("id") Long id,
            @RequestBody Reservation reservationToEdit
    ){
        logger.info("Called editReservation " + reservationToEdit);
        try{
            var updated = reservationService.editReservation(id, reservationToEdit);
            return ResponseEntity.ok(updated);
        }catch(NoSuchElementException e){
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable("id") Long id
    ){
        logger.info("Called deleteReservation " + id);
        try{
            reservationService.deleteReservation(id);
            return ResponseEntity.ok().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(404).build();
        }
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
