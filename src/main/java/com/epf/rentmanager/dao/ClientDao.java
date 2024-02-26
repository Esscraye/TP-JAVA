package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ClientDao {
	
	private static ClientDao instance = null;
	private ClientDao() {}
	public static ClientDao getInstance() {
		if(instance == null) {
			instance = new ClientDao();
		}
		return instance;
	}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
	public long create(Client client) throws DaoException {
    long id = 0;
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
		int id = 0;
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(DELETE_CLIENT_QUERY);
			stmt.setLong(1, client.id());
			stmt.executeUpdate();
			id = stmt.getGeneratedKeys().getInt(1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return id;
	}

	public Client findById(long id) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement(FIND_CLIENT_QUERY);
			stmt.setLong(1, id);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				return new Client(
					id,
					resultSet.getString("nom"),
					resultSet.getString("prenom"),
					resultSet.getString("email"),
					resultSet.getDate("naissance").toLocalDate()
				);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public List<Client> findAll() throws DaoException {
		List<Client> clients = new ArrayList<>();
		try {
			Connection connection = ConnectionManager.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery(FIND_CLIENTS_QUERY);
			while (resultSet.next()) {
				Client client = new Client(
					resultSet.getLong("id"),
					resultSet.getString("nom"),
					resultSet.getString("prenom"),
					resultSet.getString("email"),
					resultSet.getDate("naissance").toLocalDate()
				);
				clients.add(client);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return clients;
	}

}
