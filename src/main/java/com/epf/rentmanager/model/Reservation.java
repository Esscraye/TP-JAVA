package com.epf.rentmanager.model;

import java.time.LocalDate;

public record Reservation(long id, long clientId, long vehicleId, LocalDate debut, LocalDate fin) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Reservation other = (Reservation) obj;
        return id == other.id && clientId == other.clientId && vehicleId == other.vehicleId
                && debut.equals(other.debut) && fin.equals(other.fin);
    }
}

