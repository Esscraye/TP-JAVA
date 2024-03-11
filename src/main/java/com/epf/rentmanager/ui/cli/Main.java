package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.utils.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
    
    private ClientService clientService = context.getBean(ClientService.class);
    private VehicleService vehicleService = context.getBean(VehicleService.class);
    private ReservationService reservationService = context.getBean(ReservationService.class);
    
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
    
    public void run() {
        System.out.println("Bienvenue dans RentManager !");
        System.out.println("Que souhaitez-vous faire ?");
        System.out.println("1. Créer un client");
        System.out.println("2. Lister tous les clients");
        System.out.println("3. Créer un véhicule");
        System.out.println("4. Lister tous les véhicules");
        System.out.println("5. Supprimer un client");
        System.out.println("6. Supprimer un véhicule");
        System.out.println("7. Créer une réservation");
        System.out.println("8. Lister toutes les réservations");
        System.out.println("9. Supprimer une réservation");
        System.out.println("10. Lister les réservations par véhicule");
        System.out.println("11. Lister les réservations par client");
        System.out.println("12. Quitter");
        int choix = IOUtils.readInt("Votre choix : ");
        switch(choix) {
        case 1:
            createClient();
            break;
        case 2:
            listClients();
            break;
        case 3:
            createVehicle();
            break;
        case 4:
            listVehicles();
            break;
        case 5:
            deleteClient();
            break;
        case 6:
            deleteVehicle();
            break;
        case 7:
            createReservation();
            break;
        case 8:
            listReservations();
            break;
        case 9:
            deleteReservation();
            break;
        case 10:
            listResaByVehicleId();
            break;
        case 11:
            listResaByClientId();
            break;
        case 12:
            System.out.println("Au revoir !");
            break;
        default:
            System.out.println("Choix invalide");
        }
    }

    private void createClient() {
        Client client = new Client(
            0,
            IOUtils.readString("Nom : ", true),
            IOUtils.readString("Prénom : ", true),
            IOUtils.readString("Email : ", true),
            IOUtils.readDate("Date de naissance (jj/mm/aaaa) : ", true)
        );
        try {
            clientService.create(client);
            System.out.println("Client créé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la création du client : " + e.getMessage());
        }
    }

    private  void listClients() {
        try {
            for(Client client : clientService.findAll()) {
                System.out.println(client.id() + " - " + client.nom() + " " + client.prenom() + " (" + client.email() + ")");
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des clients : " + e.getMessage());
        }
    }

    private  void createVehicle() {
        Vehicle vehicle = new Vehicle(
            0,
            IOUtils.readString("Constructeur : ", true),
            IOUtils.readString("Modèle : ", true),
            IOUtils.readInt("Nombre de places : ")
        );
        try {
            vehicleService.create(vehicle);
            System.out.println("Véhicule créé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la création du véhicule : " + e.getMessage());
        }
    }

    private  void listVehicles() {
        try {
            for(Vehicle vehicle : vehicleService.findAll()) {
                System.out.println(vehicle.id() + " - " + vehicle.constructeur() + " - " + vehicle.modele() + " - " + " (" + vehicle.nbPlaces() + " places)");
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des véhicules : " + e.getMessage());
        }
    }

    private  void deleteClient() {
        long id = IOUtils.readInt("ID du client à supprimer : ");
        try {
            Client client = clientService.findById(id);
            clientService.delete(client);
            System.out.println("Client supprimé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression du client : " + e.getMessage());
        }
    }

    private  void deleteVehicle() {
        long id = IOUtils.readInt("ID du véhicule à supprimer : ");
        try {
            Vehicle vehicle = vehicleService.findById(id);
            vehicleService.delete(vehicle);
            System.out.println("Véhicule supprimé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression du véhicule : " + e.getMessage());
        }
    }

    // reservation
    private  void createReservation() {
        Reservation reservation = new Reservation(
            0,
            IOUtils.readInt("ID du client : "),
            IOUtils.readInt("ID du véhicule : "),
            IOUtils.readDate("Date de début (jj/mm/aaaa) : ", true),
            IOUtils.readDate("Date de fin (jj/mm/aaaa) : ", true)
        );
        try {
            reservationService.create(reservation);
            System.out.println("Réservation créée avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la création de la réservation : " + e.getMessage());
        }
    }

    private  void listReservations() {
        try {
            for(Reservation reservation : reservationService.findAll()) {
                System.out.println(reservation.id() + " - " + reservation.debut() + " - " + reservation.fin());
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }

    private  void deleteReservation() {
        long id = IOUtils.readInt("ID de la réservation à supprimer : ");
        try {
            Reservation reservation = reservationService.findById(id);
            reservationService.delete(reservation);
            System.out.println("Réservation supprimée avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    private  void listResaByVehicleId() {
        long id = IOUtils.readInt("ID du véhicule : ");
        try {
            for(Reservation reservation : reservationService.findResaByVehicleId(id)) {
                System.out.println(reservation.id() + " - " + reservation.debut() + " - " + reservation.fin());
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }

    private  void listResaByClientId() {
        long id = IOUtils.readInt("ID du client : ");
        try {
            for(Reservation reservation : reservationService.findResaByClientId(id)) {
                System.out.println(reservation.id() + " - " + reservation.debut() + " - " + reservation.fin());
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }
}
