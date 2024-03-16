package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleDao vehicleDao;

    @Autowired
    private VehicleService(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    public long create(Vehicle vehicle) throws ServiceException {
        if (vehicle.constructeur().isEmpty() || vehicle.nbPlaces() < 1) {
            throw new ServiceException("Le constructeur et le nombre de places du véhicule sont obligatoires");
        }
        try {
            return vehicleDao.create(vehicle);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public long delete(Vehicle vehicle) throws ServiceException {
        try {
            return vehicleDao.delete(vehicle);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void update(Long id, String constructeur, String modele, int nb_places) throws ServiceException {
        try {
            vehicleDao.update(id, constructeur, modele, nb_places);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Vehicle findById(long id) throws ServiceException {
        try {
            Optional<Vehicle> vehicleOpt = vehicleDao.findById(id);
            if (vehicleOpt.isEmpty()) {
                throw new ServiceException("Vehicle with id " + id + " does not exist.");
            }
            return vehicleOpt.get();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Vehicle> findAll() throws ServiceException {
        try {
            return vehicleDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public int countAllVehicles() throws ServiceException {
        try {
            return vehicleDao.countAllVehicles();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Vehicle> findVehiclesByClient(long clientId) throws ServiceException {
        try {
            return vehicleDao.findVehiclesByClient(clientId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
