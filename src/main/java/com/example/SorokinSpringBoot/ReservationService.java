package com.example.SorokinSpringBoot;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class ReservationService {

    public String getReservationById(){
        return "reservation-1";
    }

}
