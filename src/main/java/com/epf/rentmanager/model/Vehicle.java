package com.epf.rentmanager.model;

public record Vehicle(long id, String constructeur, String modele, int nbPlaces) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Vehicle other = (Vehicle) obj;
        return id == other.id && nbPlaces == other.nbPlaces && constructeur.equals(other.constructeur)
                && modele.equals(other.modele);
    }
}
