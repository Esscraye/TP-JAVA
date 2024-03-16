package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleDaoTest {

    @Mock
    private VehicleDao vehicleDao;

    @Test
    public void vehicle_creation_returns_expected_id() throws DaoException {
        Vehicle vehicle = new Vehicle(1, "Tesla", "Model S", 5);
        when(vehicleDao.create(vehicle)).thenReturn(1L);

        long id = vehicleDao.create(vehicle);

        assertEquals(1L, id);
        verify(vehicleDao, times(1)).create(vehicle);
    }

    @Test
    public void vehicle_deletion_returns_expected_result() throws DaoException {
        Vehicle vehicle = new Vehicle(1, "Tesla", "Model S", 5);
        when(vehicleDao.delete(vehicle)).thenReturn(1L);

        long result = vehicleDao.delete(vehicle);

        assertEquals(1, result);
        verify(vehicleDao, times(1)).delete(vehicle);
    }

    @Test
    public void vehicle_update_executes_successfully() throws DaoException {
        Vehicle vehicle = new Vehicle(1, "Tesla", "Model S", 5);
        doNothing().when(vehicleDao).update(vehicle);

        vehicleDao.update(vehicle);

        verify(vehicleDao, times(1)).update(vehicle);
    }

    @Test
    public void vehicle_found_by_id_returns_expected_vehicle() throws DaoException {
        Vehicle vehicle = new Vehicle(1, "Tesla", "Model S", 5);
        when(vehicleDao.findById(1)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = vehicleDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(vehicle, result.get());
        verify(vehicleDao, times(1)).findById(1);
    }

    @Test
    public void vehicle_not_found_by_id_returns_empty() throws DaoException {
        when(vehicleDao.findById(1)).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleDao.findById(1);

        assertFalse(result.isPresent());
        verify(vehicleDao, times(1)).findById(1);
    }

    @Test
    public void count_all_vehicles_returns_expected_count() throws DaoException {
        when(vehicleDao.countAllVehicles()).thenReturn(5);

        int result = vehicleDao.countAllVehicles();

        assertEquals(5, result);
        verify(vehicleDao, times(1)).countAllVehicles();
    }
}
