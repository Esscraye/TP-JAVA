package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientDao clientDao;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void create_client_with_valid_data() throws ServiceException, DaoException {
        Client client = new Client(1, "DOE", "John", "john.doe@example.com", LocalDate.now().minusYears(20));
        when(clientDao.create(any(Client.class))).thenReturn(1L);

        long result = clientService.create(client);

        assertEquals(1L, result);
        verify(clientDao, times(1)).create(client);
    }

    @Test
    public void delete_existing_client() throws ServiceException, DaoException {
        Client client = new Client(1L, "Doe", "John", "john.doe@example.com", LocalDate.now().minusYears(20));
        when(clientDao.delete(client)).thenReturn(1L);

        long result = clientService.delete(client);

        assertEquals(1L, result);
        verify(clientDao, times(1)).delete(client);
    }

    @Test
    public void update_existing_client() throws ServiceException, DaoException {
        doNothing().when(clientDao).update(any(Client.class));

        clientService.update(new Client(1L, "Doe", "John", "johjohn.doe@example.com", LocalDate.now().minusYears(20)));

        verify(clientDao, times(1)).update(any(Client.class));
    }

    @Test
    public void find_client_by_id() throws ServiceException, DaoException {
        Client client = new Client(1L, "Doe", "John", "john.doe@example.com", LocalDate.now().minusYears(20));
        when(clientDao.findById(1L)).thenReturn(Optional.of(client));

        Client result = clientService.findById(1L);

        assertEquals(client, result);
        verify(clientDao, times(1)).findById(1L);
    }

    @Test
    public void find_all_clients() throws ServiceException, DaoException {
        List<Client> clients = Arrays.asList(new Client(1L, "Doe", "John", "john.doe@example.com", LocalDate.now().minusYears(20)), new Client(2L, "Smith", "Jane", "jane.smith@example.com", LocalDate.now().minusYears(20)));
        when(clientDao.findAll()).thenReturn(clients);

        List<Client> result = clientService.findAll();

        assertEquals(clients, result);
        verify(clientDao, times(1)).findAll();
    }

    @Test
    public void count_all_clients() throws ServiceException {
        when(clientDao.countAllClients()).thenReturn(2);

        int result = clientService.countAllClients();

        assertEquals(2, result);
        verify(clientDao, times(1)).countAllClients();
    }

    @Test
    public void create_client_fails_for_underage_client() {
        Client underageClient = new Client(1, "John", "Doe", "john.doe@example.com", LocalDate.now().minusYears(17));
        assertThrows(ServiceException.class, () -> clientService.create(underageClient));
    }

    @Test
    public void create_client_fails_for_duplicate_email() throws ServiceException, DaoException {
        Client client2 = new Client(2, "Jane", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));

        when(clientService.create(client2)).thenThrow(new DaoException("Email is already in use."));

        assertThrows(ServiceException.class, () -> clientService.create(client2));
    }

    @Test
    public void create_client_fails_for_short_name() {
        Client clientWithShortName = new Client(1, "Jo", "Doe", "john.doe@example.com", LocalDate.now().minusYears(20));
        assertThrows(ServiceException.class, () -> clientService.create(clientWithShortName));
}
}