package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Locale;
import java.util.Objects;

/**
 * Classe mère pour les classes d'objets célestes.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize, magnitude;


    /**
     * Construit un objet céleste
     *
     * @param name          nom de l'objet céleste
     * @param equatorialPos position équatoriale de l'objet céleste
     * @param angularSize   taille angulaire de l'objet céleste
     * @param magnitude     magnitude de l'object céleste
     * @throws IllegalArgumentException si la taille angulaire est négative
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;

        Preconditions.checkArgument(!(angularSize < 0));
    }

    /**
     * @return retourne le nom de l'objet céleste
     */
    public String name() {
        return name;
    }

    /***
     * @return retourne la taille angulaire de l'objet céleste
     */
    public double angularSize() {
        return angularSize;
    }

    /***
     * @return retourne la magnitude de l'objet céleste
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     * @return retourne la position équatoriale de l'objet céleste
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     * @return retourne un (court) texte informatif au sujet de l'objet, destiné à être montré à l'utilisateur
     */
    public String info() {
        return name;
    }

    /**
     *
     * @return la distance entre l'objet céleste et la terre
     */

    public double distance() {
        return 0;
    }

    /**
     *
     * @return la vitesse angulaire de l'objet
     */
    public double rv() {
        return 0;
    }

    @Override
    public String toString() {
        return info();
    }
}

