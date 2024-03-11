package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dto.ReservationDto;
import com.epf.rentmanager.dto.ReservationFullDto;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private ReservationDao reservationDao;
    public static ReservationService instance;

    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public long create(Reservation reservation) throws ServiceException {
        if(reservation.debut().isAfter(reservation.fin())) {
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

    // findAll
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

    // findResaByVehicleId
    public List<Reservation> findResaByVehicleId(long vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    // findResaByClientId
    public List<Reservation> findResaByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Reservation findById(long id) throws ServiceException {
        try {
            return reservationDao.findById(id);
        } catch (DaoException e) {
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
