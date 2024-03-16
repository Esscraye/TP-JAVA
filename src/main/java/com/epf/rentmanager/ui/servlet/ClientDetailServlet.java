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

@WebServlet("/users/details")
public class ClientDetailServlet extends HttpServlet {
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
