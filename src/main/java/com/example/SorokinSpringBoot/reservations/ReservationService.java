package com.example.SorokinSpringBoot.reservations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final AtomicLong idCounter;
    private ReservationRepository repository;

    private final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
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

        if(reservationToCreate.status() != null)
            throw new IllegalArgumentException("Status should bew empty!");
        if(!reservationToCreate.endDate().isAfter(reservationToCreate.startDate()))
            throw new IllegalArgumentException("Start date must be after end date!");
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

        if(reservationEntity.getStatus() != ReservationStatus.PENDING)
            throw new NoSuchElementException("Can't modify reservation. Status=" + reservationEntity.getStatus());
        if(!reservationToEdit.endDate().isAfter(reservationToEdit.startDate()))
            throw new IllegalArgumentException("Start date must be after end date!");

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

    @Transactional
    public void cancelReservation(Long id) {
        var reservation = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found reservation: " + id));

        if(reservation.getStatus().equals(ReservationStatus.CONFIRMED))
            throw new IllegalStateException("Cannot cancel approved reservation! Please, contact the manager");
        if(reservation.getStatus().equals(ReservationStatus.CANCELED))
            throw new IllegalStateException("Cannot cancel cancelled reservation!");

        repository.setStatus(id, ReservationStatus.CANCELED);
        logger.info("Successfully cancelled reservation " + id);
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
        var allReservations = repository.findAll();
        for(ReservationEntity existingReservation : allReservations){
            if(reservation.getId().equals(existingReservation.getId())) continue;
            if(!reservation.getRoomId().equals(existingReservation.getRoomId())) continue;
            if(existingReservation.getStatus() != ReservationStatus.CONFIRMED) continue;
            if(reservation.getStartDate().isBefore(existingReservation.getEndDate())
            && existingReservation.getStartDate().isBefore(reservation.getEndDate()))
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
