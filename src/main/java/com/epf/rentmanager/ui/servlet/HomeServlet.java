package com.epf.rentmanager.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.service.ClientService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@WebServlet("/home")
public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

	private VehicleService vehicleService = context.getBean(VehicleService.class);
	private ClientService clientService = context.getBean(ClientService.class);
	private ReservationService reservationService = context.getBean(ReservationService.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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
