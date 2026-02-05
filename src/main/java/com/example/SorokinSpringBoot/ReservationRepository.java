package com.example.SorokinSpringBoot;

import com.example.SorokinSpringBoot.models.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

}
