package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientDao {

    private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
    private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
    private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
    private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
    private static final String COUNT_ALL_CLIENTS_QUERY = "SELECT COUNT(*) FROM Client;";
    private static final String UPDATE_CLIENT_QUERY = "UPDATE Client SET nom=?, prenom=?, email=?, naissance=? WHERE id=?;";
    private ClientDao() {
    }

    public long create(Client client) throws DaoException {
        long id;
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, client.nom());
            stmt.setString(2, client.prenom());
            stmt.setString(3, client.email());
            stmt.setDate(4, Date.valueOf(client.naissance()));
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            } else {
                throw new DaoException("Creating client failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public long delete(Client client) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(DELETE_CLIENT_QUERY);
            stmt.setLong(1, client.id());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Long id, String nom, String prenom, String email, LocalDate naissance) throws DaoException {
        try {
            Optional<Client> existingClientOpt = findById(id);
            if (!existingClientOpt.isPresent()) {
                throw new DaoException("Client with id " + id + " does not exist.");
            }

            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(UPDATE_CLIENT_QUERY);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setDate(4, Date.valueOf(naissance));
            stmt.setLong(5, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Client> findById(long id) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_CLIENT_QUERY);
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Client(id, resultSet.getString("nom"), resultSet.getString("prenom"), resultSet.getString("email"), resultSet.getDate("naissance").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Client> findAll() throws DaoException {
        List<Client> clients = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(FIND_CLIENTS_QUERY);
            while (resultSet.next()) {
                Client client = new Client(resultSet.getLong("id"), resultSet.getString("nom"), resultSet.getString("prenom"), resultSet.getString("email"), resultSet.getDate("naissance").toLocalDate());
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    public int countAllClients() {
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(COUNT_ALL_CLIENTS_QUERY);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}