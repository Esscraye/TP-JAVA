package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/details")
public class ClientDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ClientService clientService = ClientService.getInstance();
    private VehicleService vehicleService = VehicleService.getInstance();
    private ReservationService reservationService = ReservationService.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int nbResa = reservationService.countAllReservationsByClient(Long.parseLong(request.getParameter("id")));
            int nbVehicules = reservationService.countDistinctVehiclesByClientId(Long.parseLong(request.getParameter("id")));
            request.setAttribute("client", clientService.findById(Long.parseLong(request.getParameter("id"))));
            request.setAttribute("nbReservations", nbResa);
            request.setAttribute("nbVehicules", nbVehicules);
            request.setAttribute("reservations", reservationService.findResaByClientWithVehicle(Long.parseLong(request.getParameter("id"))));
            request.setAttribute("vehicles", vehicleService.findVehiclesByClient(Long.parseLong(request.getParameter("id"))));
        } catch (Exception e) {
            throw new ServletException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);
    }
}
