package com.epf.rentmanager.ui.servlet;

import java.io.IOException;
import java.time.LocalDate;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/users/create")
public class ClientCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ClientService clientService = ClientService.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String nom = request.getParameter("last_name");
        String prenom = request.getParameter("first_name");
        String email = request.getParameter("email");
        LocalDate date_naissance = LocalDate.parse(request.getParameter("date_naissance"));

        Client newClient = new Client(0, nom, prenom, email, date_naissance);

        try {
            clientService.create(newClient);
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (com.epf.rentmanager.exception.ServiceException e) {
            e.printStackTrace();
        }
    }
}
