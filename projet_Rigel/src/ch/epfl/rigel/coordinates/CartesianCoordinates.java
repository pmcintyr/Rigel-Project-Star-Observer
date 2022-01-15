package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Des coordonnées cartésiennes.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class CartesianCoordinates {

    private final double x, y;


    /**
     * Construit des coordonnées cartésiennes avec l'abscisse et l'ordonnée
     *
     * @param x l'abscisse des coordonnées cartésiennes
     * @param y l'ordonnée des coordonnées cartésiennes
     */
    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Méthode de construction des coordonnées cartésiennes
     *
     * @param x l'abscisse des coordonnées cartésiennes
     * @param y l'ordonnée des coordonnées cartésiennes
     * @return retourne les coordonnées cartésiennes construites
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * @return retourne x, l'abscisse des coordonnées cartésiennes
     */
    public double x() {
        return x;
    }

    /**
     * @return retourne y, l'ordonnée des coordonnées cartésiennes
     */
    public double y() {
        return y;
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(%s ; %s)", x, y);
    }

}

