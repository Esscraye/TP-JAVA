package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationDaoTest {

    @Mock
    private ReservationDao reservationDao;

    @Test
    public void reservation_creation_returns_expected_id() throws DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        when(reservationDao.create(reservation)).thenReturn(1L);

        long id = reservationDao.create(reservation);

        assertEquals(1L, id);
        verify(reservationDao, times(1)).create(reservation);
    }

    @Test
    public void reservation_deletion_returns_expected_result() throws DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        when(reservationDao.delete(reservation)).thenReturn(1L);

        long result = reservationDao.delete(reservation);

        assertEquals(1, result);
        verify(reservationDao, times(1)).delete(reservation);
    }

    @Test
    public void reservation_update_executes_successfully() throws DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        doNothing().when(reservationDao).update(reservation);

        reservationDao.update(reservation);

        verify(reservationDao, times(1)).update(reservation);
    }

    @Test
    public void reservation_found_by_id_returns_expected_reservation() throws DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        when(reservationDao.findById(1)).thenReturn(Optional.of(reservation));

        Optional<Reservation> result = reservationDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(reservation, result.get());
        verify(reservationDao, times(1)).findById(1);
    }

    @Test
    public void reservation_not_found_by_id_returns_empty() throws DaoException {
        when(reservationDao.findById(1)).thenReturn(Optional.empty());

        Optional<Reservation> result = reservationDao.findById(1);

        assertFalse(result.isPresent());
        verify(reservationDao, times(1)).findById(1);
    }

    @Test
    public void count_all_reservations_returns_expected_count() throws DaoException {
        when(reservationDao.countAllReservations()).thenReturn(5);

        int result = reservationDao.countAllReservations();

        assertEquals(5, result);
        verify(reservationDao, times(1)).countAllReservations();
    }
}