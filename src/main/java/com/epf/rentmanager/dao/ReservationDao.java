package com.epf.rentmanager.dao;

import com.epf.rentmanager.dto.ReservationDto;
import com.epf.rentmanager.dto.ReservationFullDto;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {

    private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
    private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
    private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET client_id=?, vehicle_id=?, debut=?, fin=? WHERE id=?;";
    private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
    private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
    private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
    private static final String FIND_UNIQUE_RESERVATION_QUERY = "SELECT * FROM Reservation WHERE id=?;";
    private static final String FIND_RESERVATIONS_FULL_QUERY = """
                SELECT * FROM Reservation r
                JOIN Vehicle v  ON r.vehicle_id = v.id
                JOIN Client c ON r.client_id = c.id;
            """;
    private static final String COUNT_ALL_RESERVATION = "SELECT COUNT(*) FROM Reservation;";
    private static final String COUNT_ALL_RESERVATION_BY_CLIENT = "SELECT COUNT(*) FROM Reservation WHERE client_id=?;";
    private static final String COUNT_DISTINCT_VEHICLES_BY_CLIENT_QUERY = "SELECT COUNT(DISTINCT vehicle_id) FROM Reservation WHERE client_id=?;";
    private static final String FIND_RESERVATIONS_BY_CLIENT_WITH_VEHICLE = """
                SELECT r.id, r.client_id, r.vehicle_id, v.constructeur, v.modele, v.nb_places, r.debut, r.fin
                FROM Reservation r JOIN Vehicle v ON r.vehicle_id = v.id
                WHERE r.client_id=?;
            """;
    private static final String CHECK_VEHICLE_RESERVED_SAME_DAY_QUERY = """
        SELECT COUNT(*)\s
        FROM Reservation\s
        WHERE vehicle_id = ?\s
        AND (
            (debut BETWEEN ? AND ?)\s
            OR (fin BETWEEN ? AND ?)\s
            OR (debut <= ? AND fin >= ?)\s
            OR (debut = ?)\s
            OR (fin = ?)\s
            OR (debut = ?)\s
            OR (fin = ?)
        )
            """;

    private ReservationDao() {
    }

    private int countById(long clientId, String countAllReservationByClient) {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(countAllReservationByClient);
            stmt.setLong(1, clientId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public long create(Reservation reservation) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();

            if (checkVehicleReservedSameDay(reservation)) {
                throw new DaoException("Vehicle already reserved for the same day");
            }

            else if (checkVehicleReservedConsecutiveDaysBySameClient(reservation)) {
                throw new DaoException("Vehicle cannot be reserved for more than 7 consecutive days by the same client");
            }

            else  if (checkVehicleReservedConsecutiveDaysWithoutBreak(reservation)) {
                throw new DaoException("Vehicle cannot be reserved for 30 consecutive days without a break");
            }

            else  return createReservation(connection, reservation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkVehicleReservedSameDay(Reservation reservation) throws SQLException, DaoException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(CHECK_VEHICLE_RESERVED_SAME_DAY_QUERY);
        stmt.setLong(1, reservation.vehicleId());
        stmt.setDate(2, Date.valueOf(reservation.debut()));
        stmt.setDate(3, Date.valueOf(reservation.fin()));
        stmt.setDate(4, Date.valueOf(reservation.debut()));
        stmt.setDate(5, Date.valueOf(reservation.fin()));
        stmt.setDate(6, Date.valueOf(reservation.debut()));
        stmt.setDate(7, Date.valueOf(reservation.fin()));
        stmt.setDate(8, Date.valueOf(reservation.debut()));
        stmt.setDate(9, Date.valueOf(reservation.fin()));
        stmt.setDate(10, Date.valueOf(reservation.debut()));
        stmt.setDate(11, Date.valueOf(reservation.fin()));
        ResultSet resultSet = stmt.executeQuery();
        return resultSet.next() && resultSet.getInt(1) > 0;
    }

    public boolean checkVehicleReservedConsecutiveDaysBySameClient(Reservation reservation) throws DaoException {
        return ChronoUnit.DAYS.between(reservation.debut(), reservation.fin()) > 7;
    }

    public boolean checkVehicleReservedConsecutiveDaysWithoutBreak(Reservation reservation) throws SQLException, DaoException {
        List<Reservation> reservations = findResaByVehicleId(reservation.vehicleId());
        reservations.add(reservation);
        reservations.sort(Comparator.comparing(Reservation::debut));
        int count = 0;
        for (int i = 0; i < reservations.size() - 1; i++) {
            if (reservations.get(i+1).debut().isEqual(reservations.get(i ).fin().plusDays(1))) {
                count += (int) reservations.get(i).debut().until(reservations.get(i).fin(), ChronoUnit.DAYS);
            } else {
                count = 0;
            }
            if (count > 30) {
                return true;
            }
        }
        return false;
    }

    private long createReservation(Connection connection, Reservation reservation) throws SQLException {
        long id = 0;
        PreparedStatement stmt = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, reservation.clientId());
        stmt.setLong(2, reservation.vehicleId());
        stmt.setDate(3, Date.valueOf(reservation.debut()));
        stmt.setDate(4, Date.valueOf(reservation.fin()));
        stmt.executeUpdate();
        ResultSet resultSet = stmt.getGeneratedKeys();
        if (resultSet.next()) {
            id = resultSet.getLong(1);
        }
        return id;
    }

    public long delete(Reservation reservation) throws DaoException {
        if (reservation == null) {
            throw new DaoException("Cannot delete null reservation");
        }
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(DELETE_RESERVATION_QUERY);
            stmt.setLong(1, reservation.id());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Reservation reservation) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(UPDATE_RESERVATION_QUERY);
            stmt.setLong(1, reservation.clientId());
            stmt.setLong(2, reservation.vehicleId());
            stmt.setDate(3, Date.valueOf(reservation.debut()));
            stmt.setDate(4, Date.valueOf(reservation.fin()));
            stmt.setLong(5, reservation.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reservation> findResaByClientId(long clientId) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);
            stmt.setLong(1, clientId);
            ResultSet resultSet = stmt.executeQuery();
            List<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(new Reservation(resultSet.getLong("id"), clientId, resultSet.getLong("vehicle_id"), resultSet.getDate("debut").toLocalDate(), resultSet.getDate("fin").toLocalDate()));
            }
            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);
            stmt.setLong(1, vehicleId);
            ResultSet resultSet = stmt.executeQuery();
            List<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(new Reservation(resultSet.getLong("id"), resultSet.getLong("client_id"), vehicleId, resultSet.getDate("debut").toLocalDate(), resultSet.getDate("fin").toLocalDate()));
            }
            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reservation> findAll() throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(FIND_RESERVATIONS_QUERY);
            while (resultSet.next()) {
                Reservation reservation = new Reservation(resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3), resultSet.getDate(4).toLocalDate(), resultSet.getDate(5).toLocalDate());
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    public List<ReservationFullDto> findAllFull() throws DaoException {
        List<ReservationFullDto> reservations = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(FIND_RESERVATIONS_FULL_QUERY);
            while (resultSet.next()) {
                Reservation reservation = new Reservation(resultSet.getLong("id"), resultSet.getLong("client_id"), resultSet.getLong("vehicle_id"), resultSet.getDate("debut").toLocalDate(), resultSet.getDate("fin").toLocalDate());
                Vehicle vehicle = new Vehicle(resultSet.getLong("vehicle_id"), resultSet.getString("constructeur"), resultSet.getString("modele"), resultSet.getInt("nb_places"));
                Client client = new Client(resultSet.getLong("client_id"), resultSet.getString("nom"), resultSet.getString("prenom"), resultSet.getString("email"), resultSet.getDate("naissance").toLocalDate());
                reservations.add(new ReservationFullDto(reservation, vehicle, client));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    public Optional<Reservation> findById(long id) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_UNIQUE_RESERVATION_QUERY);
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Reservation(resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3), resultSet.getDate(4).toLocalDate(), resultSet.getDate(5).toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public int countAllReservations() throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(COUNT_ALL_RESERVATION);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int countAllReservationsByClient(long clientId) throws DaoException {
        return countById(clientId, COUNT_ALL_RESERVATION_BY_CLIENT);
    }

    public int countDistinctVehiclesByClientId(long clientId) throws DaoException {
        return countById(clientId, COUNT_DISTINCT_VEHICLES_BY_CLIENT_QUERY);
    }

    public List<ReservationDto> findResaByClientWithVehicle(long clientId) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_WITH_VEHICLE);
            stmt.setLong(1, clientId);
            ResultSet resultSet = stmt.executeQuery();
            List<ReservationDto> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(new ReservationDto(resultSet.getLong("id"), resultSet.getLong("client_id"), new Vehicle(resultSet.getLong("vehicle_id"), resultSet.getString("constructeur"), resultSet.getString("modele"), resultSet.getInt("nb_places")), resultSet.getDate("debut").toLocalDate(), resultSet.getDate("fin").toLocalDate()));
            }
            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
