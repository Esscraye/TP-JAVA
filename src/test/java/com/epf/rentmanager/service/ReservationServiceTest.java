package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
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
public class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void create_reservation_with_valid_data() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        when(reservationDao.create(any(Reservation.class))).thenReturn(1L);

        long result = reservationService.create(reservation);

        assertEquals(1L, result);
        verify(reservationDao, times(1)).create(reservation);
    }

    @Test
    public void create_reservation_with_invalid_dates() {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now().plusDays(1), LocalDate.now());

        assertThrows(ServiceException.class, () -> reservationService.create(reservation));
    }

    @Test
    public void delete_existing_reservation() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        when(reservationDao.delete(reservation)).thenReturn(1L);

        long result = reservationService.delete(reservation);

        assertEquals(1L, result);
        verify(reservationDao, times(1)).delete(reservation);
    }

    @Test
    public void update_existing_reservation() throws ServiceException, DaoException {
        doNothing().when(reservationDao).update(any(Reservation.class));

        reservationService.update(new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1)));

        verify(reservationDao, times(1)).update(any(Reservation.class));
    }

    @Test
    public void find_reservation_by_id() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
        when(reservationDao.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.findById(1L);

        assertEquals(reservation, result);
        verify(reservationDao, times(1)).findById(1L);
    }

    @Test
    public void find_all_reservations() throws ServiceException, DaoException {
        List<Reservation> reservations = Arrays.asList(
                new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1)),
                new Reservation(2, 2, 2, LocalDate.now(), LocalDate.now().plusDays(1))
        );
        when(reservationDao.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertEquals(reservations, result);
        verify(reservationDao, times(1)).findAll();
    }

    @Test
    public void count_all_reservations() throws ServiceException, DaoException {
        when(reservationDao.countAllReservations()).thenReturn(2);

        int result = reservationService.countAllReservations();

        assertEquals(2, result);
        verify(reservationDao, times(1)).countAllReservations();
    }
}