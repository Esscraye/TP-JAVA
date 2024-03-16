package com.epf.rentmanager.model;

public record Vehicle(long id, String constructeur, String modele, int nbPlaces) {
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", constructeur='" + constructeur + '\'' +
                ", modele='" + modele + '\'' +
                ", nbPlaces=" + nbPlaces +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        if (id != vehicle.id) return false;
        if (nbPlaces != vehicle.nbPlaces) return false;
        if (!constructeur.equals(vehicle.constructeur)) return false;
        return modele.equals(vehicle.modele);
    }
}
