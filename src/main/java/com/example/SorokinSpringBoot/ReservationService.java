package com.example.SorokinSpringBoot;

import com.example.SorokinSpringBoot.enums.ReservationStatus;
import com.example.SorokinSpringBoot.models.ReservationEntity;
import com.example.SorokinSpringBoot.models.Reservation;
import jakarta.persistence.EntityNotFoundException;
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
    private ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
        this.reservationMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public Reservation getReservationById(Long id){
        ReservationEntity reservationEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Not found reservation: " + id
        ));

        return toDomainReservation(reservationEntity);
    }

    public List<Reservation> findAllReservations() {

        List<ReservationEntity> allEntities = repository.findAll();

        List<Reservation> reservationList = allEntities.stream()
                .map(it ->
                        toDomainReservation(it))
                        .toList();

        return reservationList;
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if(reservationToCreate.id() != null && reservationToCreate.status() != null){
            throw new IllegalArgumentException("ID and status should bew empty!");
        }
        if(reservationToCreate.status() != null)
            throw new IllegalArgumentException("Status should bew empty!");
        var entityToSave = new ReservationEntity(
                null,
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );
        var savedEntity = repository.save(entityToSave);
        return toDomainReservation(savedEntity);
    }

    public Reservation editReservation(Long id, Reservation reservationToEdit) {

        var reservationEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Not found reservation: " + id
        ));

        var reservation = reservationMap.get(id);
        if(reservationEntity.getStatus() != ReservationStatus.PENDING)
            throw new NoSuchElementException("Can't modify reservation. Status=" + reservationEntity.getStatus());
        var editedReservation = new ReservationEntity(
                reservationEntity.getId(),
                reservationToEdit.userId(),
                reservationToEdit.roomId(),
                reservationToEdit.startDate(),
                reservationToEdit.endDate(),
                ReservationStatus.PENDING
        );
        var savedEntity = repository.save(editedReservation);
        return toDomainReservation(savedEntity);

    }

    public void deleteReservation(Long id) {
        if(!repository.existsById(id)) throw new NoSuchElementException("Not found reservation: " + id);
        repository.deleteById(id);
    }

    public Reservation approveReservation(Long id) {
        var reservationEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Not found reservation: " + id
        ));

        if(reservationEntity.getStatus() != ReservationStatus.PENDING)
            throw new NoSuchElementException("Can't approve reservation. Status=" + reservationEntity.getStatus());

        var isConflict = isReservationConflict(reservationEntity);
        if(isConflict){
            throw new NoSuchElementException("Can't approve reservation due to conflict. Status=" + reservationEntity.getStatus());
        }

        reservationEntity.setStatus(ReservationStatus.CONFIRMED);
        repository.save(reservationEntity);

        return toDomainReservation(reservationEntity);
    }

    private boolean isReservationConflict(ReservationEntity reservation){
        for(Reservation existingReservation : reservationMap.values()){
            if(reservation.getId().equals(existingReservation.id())) continue;
            if(reservation.getRoomId().equals(existingReservation.roomId())) continue;
            if(existingReservation.status().equals(ReservationStatus.CONFIRMED)) continue;
            if(reservation.getStartDate().isBefore(existingReservation.endDate())
            && existingReservation.startDate().isBefore(reservation.getEndDate()))
                return true;
        }

        return false;
    }

    private Reservation toDomainReservation(
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

}
