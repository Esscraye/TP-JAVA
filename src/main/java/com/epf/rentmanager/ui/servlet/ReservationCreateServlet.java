package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ClientService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
    private VehicleService vehicleService = context.getBean(VehicleService.class);
    private ReservationService reservationService = context.getBean(ReservationService.class);
    private ClientService clientService = context.getBean(ClientService.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        long clientId = Long.parseLong(request.getParameter("client"));
        long vehicleId = Long.parseLong(request.getParameter("vehicle"));
        LocalDate debut = LocalDate.parse(request.getParameter("begin"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate fin = LocalDate.parse(request.getParameter("end"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Reservation reservation = new Reservation(0, clientId, vehicleId, debut, fin);
        try {
            reservationService.create(reservation);
            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
