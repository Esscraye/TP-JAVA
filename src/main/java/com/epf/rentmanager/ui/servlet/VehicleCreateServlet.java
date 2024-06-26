package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.model.Vehicle;
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

@WebServlet("/cars/create")
public class VehicleCreateServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Autowired
    VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String constructeur = request.getParameter("constructeur");
    String modele = request.getParameter("modele");
    int nbPlaces = Integer.parseInt(request.getParameter("nbPlaces"));

    if (constructeur == null || modele == null || nbPlaces < 2 || nbPlaces > 9) {
        request.setAttribute("error", "Invalid vehicle data");
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
        return;
    }

    Vehicle newVehicle = new Vehicle(0, constructeur, modele, nbPlaces);
    try {
        vehicleService.create(newVehicle);
        response.sendRedirect(request.getContextPath() + "/cars");
    } catch (com.epf.rentmanager.exception.ServiceException e) {
        e.printStackTrace();
    }
}
}