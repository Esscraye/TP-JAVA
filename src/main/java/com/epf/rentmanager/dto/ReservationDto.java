package com.epf.rentmanager.dto;


import com.epf.rentmanager.model.Vehicle;

import java.time.LocalDate;

public record ReservationDto(long id, long clientId, Vehicle vehicle, LocalDate debut, LocalDate fin) {
    public ReservationDto {
        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
    }
}