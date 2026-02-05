package com.example.SorokinSpringBoot;

import com.example.SorokinSpringBoot.models.ReservaionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservaionEntity, Long> {

}
