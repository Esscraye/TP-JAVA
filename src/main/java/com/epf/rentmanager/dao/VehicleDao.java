package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class VehicleDao {

    private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
    private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
    private static final String UPDATE_VEHICLE_QUERY = "UPDATE Vehicle SET constructeur=?, modele=?, nb_places=? WHERE id=?;";
    private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
    private static final String FIND_ALL_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
    private static final String COUNT_ALL_VEHICLES_QUERY = "SELECT COUNT(*) FROM Vehicle;";
    private static final String FIND_VEHICLES_BY_CLIENT_QUERY = """
			SELECT v.id, v.constructeur, v.modele, v.nb_places
			FROM Reservation r JOIN Vehicle v ON r.vehicle_id = v.id
			WHERE r.client_id=?;
		""";
    private VehicleDao() {
    }

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
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public long delete(Vehicle vehicle) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(DELETE_VEHICLE_QUERY);
            stmt.setLong(1, vehicle.id());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Vehicle vehicle) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(UPDATE_VEHICLE_QUERY);
            stmt.setString(1, vehicle.constructeur());
            stmt.setString(2, vehicle.modele());
            stmt.setInt(3, vehicle.nbPlaces());
            stmt.setLong(4, vehicle.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Vehicle> findById(long id) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_VEHICLE_QUERY);
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Vehicle(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Vehicle> findAll() throws DaoException {
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_ALL_VEHICLES_QUERY);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
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
            PreparedStatement stmt = connection.prepareStatement(COUNT_ALL_VEHICLES_QUERY);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
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
            List<Vehicle> vehicles = new ArrayList<>();
            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
                vehicles.add(vehicle);
            }
            return vehicles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
