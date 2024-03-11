package com.epf.rentmanager.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@WebServlet("/cars/create")
public class VehicleCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
    private VehicleService vehicleService = context.getBean(VehicleService.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String constructeur = request.getParameter("constructeur");
    String modele = request.getParameter("modele");
    int nbPlaces = Integer.parseInt(request.getParameter("nbPlaces"));

    Vehicle newVehicle = new Vehicle(0, constructeur, modele, nbPlaces);
    try {
        vehicleService.create(newVehicle);
        response.sendRedirect(request.getContextPath() + "/vehicles");
    } catch (com.epf.rentmanager.exception.ServiceException e) {
        e.printStackTrace();
    }
}
}