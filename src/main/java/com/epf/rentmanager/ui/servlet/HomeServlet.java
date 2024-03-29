package com.epf.rentmanager.ui.servlet;

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


@WebServlet("/home")
public class HomeServlet extends HttpServlet {

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
            int nbVehicles = vehicleService.countAllVehicles();
            int nbClients = clientService.countAllClients();
            int nbReservations = reservationService.countAllReservations();

            request.setAttribute("nbVehicles", nbVehicles);
            request.setAttribute("nbClients", nbClients);
            request.setAttribute("nbReservations", nbReservations);
        } catch (com.epf.rentmanager.exception.ServiceException e) {
            e.printStackTrace();
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

}
