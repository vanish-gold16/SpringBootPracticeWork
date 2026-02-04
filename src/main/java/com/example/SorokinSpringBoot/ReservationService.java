package com.example.SorokinSpringBoot;

import com.example.SorokinSpringBoot.enums.ReservationStatus;
import com.example.SorokinSpringBoot.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap;
    private final AtomicLong idCounter;

    public ReservationService() {
        this.reservationMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public Reservation getReservationById(Long id){

        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if(reservationToCreate.id() != null && reservationToCreate.status() != null){
            throw new IllegalArgumentException("ID and status should bew empty!");
        }
        var newReservation = new Reservation(
                idCounter.incrementAndGet(),
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );
        reservationMap.put(newReservation.id(), newReservation);
        return newReservation;
    }

    public Reservation editReservation(Long id, Reservation reservationToEdit) {
        if(!reservationMap.containsKey(id)) throw new NoSuchElementException("This reservation doe not exist");
        var reservation = reservationMap.get(id);
        if(reservation.status() != ReservationStatus.PENDING)
            throw new NoSuchElementException("Can't modify reservation. Status=" + reservation.status());
        var editedReservation = new Reservation(
                reservation.id(),
                reservationToEdit.userId(),
                reservationToEdit.roomId(),
                reservationToEdit.startDate(),
                reservationToEdit.endDate(),
                ReservationStatus.PENDING
        );
        reservationMap.put(reservation.id(), editedReservation);
        return editedReservation;

    }

    public void deleteReservation(Long id) {
        if(!reservationMap.containsKey(id)) throw new NoSuchElementException("This reservation doe not exist");
        reservationMap.remove(id);
    }

    public Reservation approveReservation(Long id) {
        if(!reservationMap.containsKey(id)) throw new NoSuchElementException("This reservation doe not exist");

        var reservation = reservationMap.get(id);

        if(reservation.status() != ReservationStatus.PENDING)
            throw new NoSuchElementException("Can't approve reservation. Status=" + reservation.status());

        var isConflict = isReservationConflict(reservation);
        if(isConflict){
            throw new NoSuchElementException("Can't approve reservation due to conflict. Status=" + reservation.status());
        }
        var approvedReservation = new Reservation(
                reservation.id(),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.CONFIRMED
        );
        reservationMap.put(reservation.id(), approvedReservation);
        return approvedReservation;
    }

    private boolean isReservationConflict(Reservation reservation){
        for(Reservation existingReservation : reservationMap.values()){
            if(reservation.id().equals(existingReservation.id()) &&
               !reservation.roomId().equals(existingReservation.roomId()) &&
               !existingReservation.status().equals(ReservationStatus.CONFIRMED))
                continue;
            if(reservation.startDate().isBefore(existingReservation.endDate())
            && existingReservation.startDate().isBefore(reservation.endDate()))
                return true;
        }

        return false;
    }

}
