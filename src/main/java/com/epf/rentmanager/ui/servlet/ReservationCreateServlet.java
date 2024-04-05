package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Autowired
    ClientService clientService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Client> clients = clientService.findAll();
            List<Vehicle> vehicles = vehicleService.findAll();
            request.setAttribute("clients", clients);
            request.setAttribute("vehicles", vehicles);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long clientId = Long.parseLong(request.getParameter("client"));
        long vehicleId = Long.parseLong(request.getParameter("vehicle"));
        LocalDate debut = LocalDate.parse(request.getParameter("begin"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate fin = LocalDate.parse(request.getParameter("end"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Reservation reservation = new Reservation(0, clientId, vehicleId, debut, fin);

        try {
            if (reservationService.checkVehicleReservedSameDay(reservation)) {
                throw new ServletException("Vehicle already reserved for the same day");
            }
            if (reservationService.checkVehicleReservedConsecutiveDaysBySameClient(reservation)) {
                throw new ServletException("Vehicle cannot be reserved for more than 7 consecutive days by the same client");
            }
            if (reservationService.checkVehicleReservedConsecutiveDaysWithoutBreak(reservation)) {
                throw new ServletException("Vehicle cannot be reserved for 30 consecutive days without a break");
            }
        } catch (ServletException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
            return;
        } catch (Exception e) {
            throw new ServletException(e);
        }

        try {
            reservationService.create(reservation);
            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
