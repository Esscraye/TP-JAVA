package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleDao vehicleDao;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    public void create_vehicle_with_validData() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle(1L, "Tesla", "Model S", 5);
        when(vehicleDao.create(any(Vehicle.class))).thenReturn(1L);

        long result = vehicleService.create(vehicle);

        assertEquals(1L, result);
        verify(vehicleDao, times(1)).create(vehicle);
    }

    @Test
    public void create_vehicle_with_empty_constructor() {
        Vehicle vehicle = new Vehicle(1L, "", "Model S", 5);

        assertThrows(ServiceException.class, () -> vehicleService.create(vehicle));
    }

    @Test
    public void delete_existing_vehicle() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle(1L, "Tesla", "Model S", 5);
        when(vehicleDao.delete(vehicle)).thenReturn(1L);

        long result = vehicleService.delete(vehicle);

        assertEquals(1L, result);
        verify(vehicleDao, times(1)).delete(vehicle);
    }

    @Test
    public void update_existing_vehicle() throws ServiceException, DaoException {
        doNothing().when(vehicleDao).update(any(Vehicle.class));

        vehicleService.update(new Vehicle(1, "Tesla", "Model S", 5));

        verify(vehicleDao, times(1)).update(any(Vehicle.class));
    }

    @Test
    public void find_vehicle_by_id() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle(1L, "Tesla", "Model S", 5);
        when(vehicleDao.findById(1L)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.findById(1L);

        assertEquals(vehicle, result);
        verify(vehicleDao, times(1)).findById(1L);
    }

    @Test
    public void find_all_vehicles() throws ServiceException, DaoException {
        List<Vehicle> vehicles = Arrays.asList(new Vehicle(1L, "Tesla", "Model S", 5), new Vehicle(2L, "BMW", "i8", 2));
        when(vehicleDao.findAll()).thenReturn(vehicles);

        List<Vehicle> result = vehicleService.findAll();

        assertEquals(vehicles, result);
        verify(vehicleDao, times(1)).findAll();
    }

    @Test
    public void count_all_vehicles() throws ServiceException, DaoException {
        when(vehicleDao.countAllVehicles()).thenReturn(2);

        int result = vehicleService.countAllVehicles();

        assertEquals(2, result);
        verify(vehicleDao, times(1)).countAllVehicles();
    }

    @Test
    public void find_vehicles_by_client() throws ServiceException, DaoException {
        List<Vehicle> vehicles = Arrays.asList(new Vehicle(1L, "Tesla", "Model S", 5), new Vehicle(2L, "BMW", "i8", 2));
        when(vehicleDao.findVehiclesByClient(1L)).thenReturn(vehicles);

        List<Vehicle> result = vehicleService.findVehiclesByClient(1L);

        assertEquals(vehicles, result);
        verify(vehicleDao, times(1)).findVehiclesByClient(1L);
    }

    @Test
    public void vehicle_creation_with_invalid_data_throws_exception() {
        Vehicle vehicle = new Vehicle(1, null, "Model S", 5);
        assertThrows(ServiceException.class, () -> vehicleService.create(vehicle));

        Vehicle vehicle2 = new Vehicle(1, "Tesla", null, 5);
        assertThrows(ServiceException.class, () -> vehicleService.create(vehicle2));

        Vehicle vehicle3 = new Vehicle(1, "Tesla", "Model S", 1);
        assertThrows(ServiceException.class, () -> vehicleService.create(vehicle3));

        Vehicle vehicle4 = new Vehicle(1, "Tesla", "Model S", 10);
        assertThrows(ServiceException.class, () -> vehicleService.create(vehicle4));
    }

    @Test
    public void vehicle_update_with_invalid_data_throws_exception() {
        Vehicle vehicle = new Vehicle(1, null, "Model S", 5);
        assertThrows(ServiceException.class, () -> vehicleService.update(vehicle));

        Vehicle vehicle2 = new Vehicle(1, "Tesla", null, 5);
        assertThrows(ServiceException.class, () -> vehicleService.update(vehicle2));

        Vehicle vehicle3 = new Vehicle(1, "Tesla", "Model S", 1);
        assertThrows(ServiceException.class, () -> vehicleService.update(vehicle3));

        Vehicle vehicle4 = new Vehicle(1, "Tesla", "Model S", 10);
        assertThrows(ServiceException.class, () -> vehicleService.update(vehicle4));
    }
}