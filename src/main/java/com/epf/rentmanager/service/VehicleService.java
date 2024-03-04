package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;

public class VehicleService {

	private VehicleDao vehicleDao;
	public static VehicleService instance;
	
	private VehicleService() {
		this.vehicleDao = VehicleDao.getInstance();
	}
	
	public static VehicleService getInstance() {
		if (instance == null) {
			instance = new VehicleService();
		}
		
		return instance;
	}
	
	
	public long create(Vehicle vehicle) throws ServiceException {
		// TODO: créer un véhicule
		if(vehicle.constructeur().isEmpty() || vehicle.nbPlaces() < 1) {
			throw new ServiceException("Le constructeur et le nombre de places du véhicule sont obligatoires");
		}
		try {
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public long delete(Vehicle vehicle) throws ServiceException {
		// TODO: supprimer un véhicule
		try {
			return vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public Vehicle findById(long id) throws ServiceException {
		// TODO: récupérer un véhicule par son id
		try {
			return vehicleDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public List<Vehicle> findAll() throws ServiceException {
		// TODO: récupérer tous les clients
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
