package com.example.SorokinSpringBoot.repositories;

import com.example.SorokinSpringBoot.enums.ReservationStatus;
import com.example.SorokinSpringBoot.models.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    //List<ReservationEntity> findAllByStatusIs(ReservationStatus status);

    @Query("select r from ReservationEntity r where r.status = :status")
    List<ReservationEntity> findAllByStatusIs(ReservationStatus status);

    @Query("select r from ReservationEntity r where r.roomId = :roomId")
    List<ReservationEntity> findAllByRoomId(@Param("roomId") Long roomId);

    @Transactional
    @Modifying
    @Query("""
             update ReservationEntity r
             set r.userId = :userId,
                 r.roomId = :roomId,
                 r.startDate = :startDate,
                 r.endDate = :endDate,
                 r.status = :status
                 where r.id = :id                                                        
                        """)
    int updateAllFields(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") ReservationStatus status
            );
}
