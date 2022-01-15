package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Projection stéréographique.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double phi1, cx, cosPhi1, sinPhi1, lambda0;

    /**
     * Construit une projection stéréographique avec le centre donné
     *
     * @param center le centre de la projection stéréographique
     */
    public StereographicProjection(HorizontalCoordinates center) {
        lambda0 = center.lon();
        phi1 = center.lat();
        cx = 0;
        cosPhi1 = Math.cos(phi1);
        sinPhi1 = Math.sin(phi1);
    }

    /**
     * Construit les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes xy
     *
     * @param xy coordonnées cartésiennes de la projection du point de coordonnées horizontales
     * @return retourne les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes xy
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double rho = Math.sqrt(Math.pow(xy.x(), 2) + Math.pow(xy.y(), 2));
        double rhoSquared = Math.pow(rho,2);
        double sinC = 2 * rho / (rhoSquared + 1);
        double cosC = (1 - rhoSquared) / (rhoSquared + 1);

        double lambda = lambda0;
        double phi = phi1;
        if (xy.x() != 0 || xy.y() != 0) {
            lambda = Angle.normalizePositive(Math.atan2(xy.x() * sinC, rho * cosPhi1 * cosC - xy.y() * sinPhi1 * sinC) + lambda0);
            phi = Math.asin(cosC * sinPhi1 + xy.y() * sinC * cosPhi1 / rho);
        }

        return HorizontalCoordinates.of(lambda, phi);

    }

    /**
     * Construit le centre du cercle correspondant à la projection du parallèle passant par le point hor
     *
     * @param hor point par lequel on effectue la projection du parallèle
     * @return retourne les coordonnées du centre du cercle correspondant à la projection du parallèle passant par le point hor
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        double phi = hor.lat();
        double cy = cosPhi1 / (Math.sin(phi) + sinPhi1);
        return CartesianCoordinates.of(cx, cy);

    }

    /**
     * Construit le rayon du cercle correspondant à la projection du parallèle passant par le point de coordonnées hor
     *
     * @param parallel ligne de latitude constante
     * @return retourne le rayon du cercle correspondant à la projection du parallèle passant par le point de coordonnées hor
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        double phi = parallel.lat();
        return Math.cos(phi) / (Math.sin(phi) + sinPhi1);

    }

    /**
     * Construit le diamètre projeté d'une sphère de taille angulaire rad centrée au centre de projection
     *
     * @param rad taille angulaire
     * @return retourne le diamètre projeté d'une sphère de taille angulaire rad centrée au centre de projection
     */
    public double applyToAngle(double rad) {
        return  2 * Math.tan(rad / 4);

    }

    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double phi = azAlt.lat();
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double landa = azAlt.lon();
        double LandaDiff = landa - lambda0;
        double d = 1 / (1 + sinPhi* sinPhi1 + cosPhi * cosPhi1 * Math.cos(LandaDiff));
        double x = d * cosPhi * Math.sin(LandaDiff);
        double y = d * (sinPhi* cosPhi1 - cosPhi * sinPhi1 * Math.cos(LandaDiff));
        return CartesianCoordinates.of(x, y);

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
        return String.format(Locale.ROOT,
                "StereographicProjection: centre de projection en coordonnées horizontales ( %s; %s)", lambda0, phi1);
    }
}

