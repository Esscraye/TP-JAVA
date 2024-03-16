package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientDao clientDao;

    @Autowired
    private ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public long create(Client client) throws ServiceException, DaoException {
        // Check if the client is at least 18 years old
        if (Period.between(client.naissance(), LocalDate.now()).getYears() < 18) {
            throw new ServiceException("Client must be at least 18 years old.");
        }

        // Check if the email is already in use
        List<Client> allClients = clientDao.findAll();
        String clientEmail = client.email();
        if (allClients.stream().anyMatch(c -> c.email().equals(clientEmail))) {
            throw new ServiceException("Email is already in use.");
        }

        // Check if the name and surname are at least 3 characters long
        if (client.nom().length() < 3 || client.prenom().length() < 3) {
            throw new ServiceException("Name and surname must be at least 3 characters long.");
        }

        client = new Client(client.id(), client.nom().toUpperCase(), client.prenom(), client.email(), client.naissance());
        try {
            return clientDao.create(client);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public long delete(Client client) throws ServiceException {
        try {
            return clientDao.delete(client);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void update(Client client) throws ServiceException {
        try {
            clientDao.update(client);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Client findById(long id) throws ServiceException {
        try {
            Optional<Client> clientOpt = clientDao.findById(id);
            if (!clientOpt.isPresent()) {
                throw new ServiceException("Client with id " + id + " does not exist.");
            }
            return clientOpt.get();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Client> findAll() throws ServiceException {
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
