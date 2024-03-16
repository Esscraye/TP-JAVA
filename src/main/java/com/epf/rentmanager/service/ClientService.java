package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientDao clientDao;

    @Autowired
    private ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public long create(Client client) throws ServiceException {
        if (client.nom().isEmpty() || client.prenom().isEmpty()) {
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
        try {
            return clientDao.delete(client);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void update(Long id, String nom, String prenom, String email, LocalDate naissance) throws ServiceException {
        try {
            clientDao.update(id, nom, prenom, email, naissance);
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
