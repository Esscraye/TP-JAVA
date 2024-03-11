package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;

public class VehicleDao {
	
	private static VehicleDao instance = null;
	private VehicleDao() {}
	public static VehicleDao getInstance() {
		if(instance == null) {
			instance = new VehicleDao();
		}
		return instance;
	}
	
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String Count_All_Vehicles = "SELECT COUNT(*) FROM Vehicle;";
	private static final String FIND_VEHICLES_BY_CLIENT_QUERY = "SELECT v.id, v.constructeur, v.modele, v.nb_places FROM Reservation r JOIN Vehicle v ON r.vehicle_id = v.id WHERE r.client_id=?;";

	public long create(Vehicle vehicle) throws DaoException {
		long id = 0;
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, vehicle.constructeur());
			stmt.setString(2, vehicle.modele());
			stmt.setInt(3, vehicle.nbPlaces());
			stmt.execute();
			ResultSet resultSet = stmt.getGeneratedKeys();
			if(resultSet.next()) {
				id = resultSet.getLong(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return id;
	}

	public long delete(Vehicle vehicle) throws DaoException {
		long id = 0;
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(DELETE_VEHICLE_QUERY);
			stmt.setLong(1, vehicle.id());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Vehicle findById(long id) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(FIND_VEHICLE_QUERY);
			stmt.setLong(1, id);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next()) {
                return new Vehicle(
						resultSet.getLong(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getInt(4)
				);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public List<Vehicle> findAll() throws DaoException {
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(FIND_VEHICLES_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Vehicle vehicle = new Vehicle(
						resultSet.getLong(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getInt(4)
				);
				vehicles.add(vehicle);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return vehicles;
	}

	public int countAllVehicles() throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(Count_All_Vehicles);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return 0;
	}

	public List<Vehicle> findVehiclesByClient(long clientId) throws DaoException {

		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(FIND_VEHICLES_BY_CLIENT_QUERY);
			stmt.setLong(1, clientId);
			ResultSet resultSet = stmt.executeQuery();
			List<Vehicle> vehicles = new ArrayList<Vehicle>();
			while (resultSet.next()) {
				Vehicle vehicle = new Vehicle(
						resultSet.getLong(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getInt(4)
				);
				vehicles.add(vehicle);
			}
			return vehicles;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
