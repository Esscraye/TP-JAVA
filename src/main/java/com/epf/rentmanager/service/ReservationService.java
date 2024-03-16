package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dto.ReservationDto;
import com.epf.rentmanager.dto.ReservationFullDto;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    @Autowired
    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public long create(Reservation reservation) throws ServiceException {
        if (reservation.debut().isAfter(reservation.fin())) {
            throw new ServiceException("La date de début doit être avant la date de fin");
        }
        try {
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public long delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void update(Reservation reservation) throws ServiceException {
        if (reservation.debut().isAfter(reservation.fin())) {
            throw new ServiceException("La date de début doit être avant la date de fin");
        }
        try {
            reservationDao.update(reservation);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<ReservationFullDto> findAllFull() throws ServiceException {
        try {
            return reservationDao.findAllFull();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Reservation> findResaByVehicleId(long vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Reservation> findResaByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Reservation findById(long id) throws ServiceException {
        try {
            Optional<Reservation> reservationOpt = reservationDao.findById(id);
            if (reservationOpt.isEmpty()) {
                throw new ServiceException("Reservation with id " + id + " does not exist.");
            }
            return reservationOpt.get();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public int countAllReservations() throws ServiceException {
        try {
            return reservationDao.countAllReservations();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public int countAllReservationsByClient(long clientId) throws ServiceException {
        try {
            return reservationDao.countAllReservationsByClient(clientId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public int countDistinctVehiclesByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.countDistinctVehiclesByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<ReservationDto> findResaByClientWithVehicle(long clientId) throws ServiceException {
        try {
            return reservationDao.findResaByClientWithVehicle(clientId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
