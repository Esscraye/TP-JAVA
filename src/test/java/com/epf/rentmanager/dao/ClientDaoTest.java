package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientDaoTest {

    @Mock
    private ClientDao clientDao;

    @Test
    public void client_creation_returns_expected_id() throws DaoException {
        Client client = new Client(1, "John", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));
        when(clientDao.create(client)).thenReturn(1L);

        long id = clientDao.create(client);

        assertEquals(1L, id);
        verify(clientDao, times(1)).create(client);
    }

    @Test
    public void client_deletion_returns_expected_result() throws DaoException {
        Client client = new Client(1, "John", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));
        when(clientDao.delete(client)).thenReturn(1L);

        long result = clientDao.delete(client);

        assertEquals(1L, result);
        verify(clientDao, times(1)).delete(client);
    }

    @Test
    public void client_update_executes_successfully() throws DaoException {
        Client client = new Client(1, "John", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));
        doNothing().when(clientDao).update(client);

        clientDao.update(client);

        verify(clientDao, times(1)).update(client);
    }

    @Test
    public void client_found_by_id_returns_expected_client() throws DaoException {
        Client client = new Client(1, "John", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));
        when(clientDao.findById(1)).thenReturn(Optional.of(client));

        Optional<Client> result = clientDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
        verify(clientDao, times(1)).findById(1);
    }

    @Test
    public void client_not_found_by_id_returns_empty() throws DaoException {
        when(clientDao.findById(1)).thenReturn(Optional.empty());

        Optional<Client> result = clientDao.findById(1);

        assertFalse(result.isPresent());
        verify(clientDao, times(1)).findById(1);
    }

    @Test
    public void count_all_clients_returns_expected_count() throws DaoException {
        when(clientDao.countAllClients()).thenReturn(5);

        int result = clientDao.countAllClients();

        assertEquals(5, result);
        verify(clientDao, times(1)).countAllClients();
    }

    @Test
    public void client_creation_fails_for_underage_client() throws DaoException {
        Client underageClient = new Client(1, "John", "Doe", "john.doe@example.com", LocalDate.now().minusYears(17));
        when(clientDao.create(underageClient)).thenThrow(new DaoException("Client is underage."));

        assertThrows(DaoException.class, () -> clientDao.create(underageClient));
    }

    @Test
    public void client_creation_fails_for_duplicate_email() throws DaoException {
        Client client2 = new Client(2, "Jane", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));

        when(clientDao.create(client2)).thenThrow(new DaoException("Email is already in use."));

        assertThrows(DaoException.class, () -> clientDao.create(client2));
    }

    @Test
    public void client_creation_fails_for_short_name() throws DaoException {
        Client clientWithShortName = new Client(1, "Jo", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));
        when(clientDao.create(clientWithShortName)).thenThrow(new DaoException("Name and surname must be at least 3 characters long."));

        assertThrows(DaoException.class, () -> clientDao.create(clientWithShortName));
    }
}