package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

	private ClientDao clientDao;
	public static ClientService instance;

	@Autowired
	private ClientService(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	
	public long create(Client client) throws ServiceException {
		// TODO: créer un client
		if(client.nom().isEmpty() || client.prenom().isEmpty()) {
			throw new ServiceException("Le nom et le prénom du client sont obligatoires");
		}
		client = new Client(client.id(), client.nom().toUpperCase(), client.prenom(), client.email(), client.naissance());
		try {
			return clientDao.create(client);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public long delete(Client client) throws ServiceException {
		// TODO: supprimer un client
		try {
			return clientDao.delete(client);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public Client findById(long id) throws ServiceException {
		// TODO: récupérer un client par son id
		try {
			return clientDao.findById(id);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	}

	public List<Client> findAll() throws ServiceException {
		// TODO: récupérer tous les clients
		try {
			return clientDao.findAll();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public int countAllClients() throws ServiceException {
		try {
			return clientDao.countAllClients();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}
