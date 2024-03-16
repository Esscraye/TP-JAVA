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

    @Test
    public void create_reservation_same_vehicle_same_dates() throws DaoException {
        Reservation reservation1 = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation reservation2 = new Reservation(2, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        when(reservationDao.create(reservation1)).thenReturn(1L);
        when(reservationDao.create(reservation2)).thenThrow(DaoException.class);

        long id = reservationDao.create(reservation1);
        assertThrows(DaoException.class, () -> reservationDao.create(reservation2));
        verify(reservationDao, times(1)).create(reservation1);
        verify(reservationDao, times(1)).create(reservation2);
    }

    @Test
    public void create_reservation_same_vehicle_more_than_seven_days() throws DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(8));
        when(reservationDao.create(reservation)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> reservationDao.create(reservation));
        verify(reservationDao, times(1)).create(reservation);
    }

    @Test
    public void create_reservation_same_vehicle_thirty_days() throws DaoException {
        Reservation reservation1 = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(6));
        Reservation reservation2 = new Reservation(2, 1, 1, LocalDate.now().plusDays(7), LocalDate.now().plusDays(6));
        Reservation reservation3 = new Reservation(3, 1, 1, LocalDate.now().plusDays(14), LocalDate.now().plusDays(6));
        Reservation reservation4 = new Reservation(4, 1, 1, LocalDate.now().plusDays(21), LocalDate.now().plusDays(6));
        Reservation reservation5 = new Reservation(5, 1, 1, LocalDate.now().plusDays(28), LocalDate.now().plusDays(3));
        when(reservationDao.create(reservation1)).thenReturn(1L);
        when(reservationDao.create(reservation2)).thenReturn(2L);
        when(reservationDao.create(reservation3)).thenReturn(3L);
        when(reservationDao.create(reservation4)).thenReturn(4L);
        when(reservationDao.create(reservation5)).thenThrow(DaoException.class);

        long id1 = reservationDao.create(reservation1);
        long id2 = reservationDao.create(reservation2);
        long id3 = reservationDao.create(reservation3);
        long id4 = reservationDao.create(reservation4);
        assertThrows(DaoException.class, () -> reservationDao.create(reservation5));
        verify(reservationDao, times(1)).create(reservation1);
        verify(reservationDao, times(1)).create(reservation2);
        verify(reservationDao, times(1)).create(reservation3);
        verify(reservationDao, times(1)).create(reservation4);
        verify(reservationDao, times(1)).create(reservation5);
    }
}