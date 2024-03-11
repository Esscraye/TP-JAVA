package com.epf.rentmanager.dto;


import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;

public record ReservationFullDto(Reservation reservation, Vehicle vehicle, Client client) {}