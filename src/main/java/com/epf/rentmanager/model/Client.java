package com.epf.rentmanager.model;

import java.time.LocalDate;

public record Client(long id, String nom, String prenom, String email, LocalDate naissance) {
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", naissance=" + naissance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != client.id) return false;
        if (!nom.equals(client.nom)) return false;
        if (!prenom.equals(client.prenom)) return false;
        if (!email.equals(client.email)) return false;
        return naissance.equals(client.naissance);
    }
}