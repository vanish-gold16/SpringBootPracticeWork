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

    public Reservation updateReservation(Long id, Reservation reservationToEdit) {
        return null;
    }

    public void deleteReservation(Long id) {
        if(!reservationMap.containsKey(id)) throw new NoSuchElementException("This reservation doe not exist");
        reservationMap.remove(id);
    }
}
