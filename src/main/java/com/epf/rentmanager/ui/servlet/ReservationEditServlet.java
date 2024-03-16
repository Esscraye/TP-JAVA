package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.model.Reservation;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet("/rents/edit")
public class ReservationEditServlet extends HttpServlet {
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
            long id = Long.parseLong(request.getParameter("id"));
            Reservation reservation = reservationService.findById(id);
            LocalDate localDateDebut = reservation.debut();
            Instant instantDebut = localDateDebut.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date dateDebut = Date.from(instantDebut);
            request.setAttribute("reservationDebut", dateDebut);

            LocalDate localDateFin = reservation.fin();
            Instant instantFin = localDateFin.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date dateFin = Date.from(instantFin);
            request.setAttribute("reservationFin", dateFin);

            request.setAttribute("id", id);
            request.setAttribute("reservation", reservation);
            request.setAttribute("clients", clientService.findAll());
            request.setAttribute("vehicles", vehicleService.findAll());
        } catch (Exception e) {
            throw new ServletException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));

            long clientId = Long.parseLong(request.getParameter("client"));
            long vehicleId = Long.parseLong(request.getParameter("vehicle"));
            LocalDate debut = LocalDate.parse(request.getParameter("begin"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate fin = LocalDate.parse(request.getParameter("end"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            Reservation reservation = new Reservation(id, clientId, vehicleId, debut, fin);
            reservationService.update(reservation);
            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (com.epf.rentmanager.exception.ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
