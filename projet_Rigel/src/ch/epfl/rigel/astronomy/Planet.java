package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Classe pour construire des planètes.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public final class Planet extends CelestialObject {

    /**
     * Construit une planète
     *
     * @param name          nom de la planète
     * @param equatorialPos position équatoriale de la planète
     * @param angularSize   taille angulaire de la planète
     * @param magnitude     magnitude de la planète
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }

}

