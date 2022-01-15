package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonées géoraphiques
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class GeographicCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval RIGHT_OPEN_INTERVAL_FULL = RightOpenInterval.symmetric(360);
    private final static ClosedInterval CLOSED_INTERVAL = ClosedInterval.symmetric(180);


    private GeographicCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Construit des coordonées géographiques avec la longitude et la latitude
     * données en degrés.
     *
     * @param lonDeg la longitude en degrés
     * @param latDeg la latitude en degrés
     * @return des coordonées géographiques
     * @throws IllegalArgumentException si la longitude ou la latitude ne sont pas dans l'intervalle
     *                                  qui leur est attribué.
     */

    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Preconditions.checkArgument(isValidLonDeg(lonDeg) && isValidLatDeg(latDeg));
            return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * @param lonDeg la longitude en degrés
     * @return Retourne vrai ssi la longitude (en degrés) est bien dans l'intervalle.
     */

    public static boolean isValidLonDeg(double lonDeg) {
        return RIGHT_OPEN_INTERVAL_FULL.contains(lonDeg);
    }

    /**
     * @param latDeg la latitude en degrés
     * @return Retourne vrai ssi la latitude (en degrés) est bien dans l'intervalle.
     */
    public static boolean isValidLatDeg(double latDeg) {
        return CLOSED_INTERVAL.contains(latDeg);
    }

    /**
     * @return la longitude en radians
     */
    public double lon() {
        return super.lon();
    }

    /**
     * @return la longitude en degrés
     */
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @return la latitude en radians
     */
    public double lat() {
        return super.lat();
    }

    /**
     * @return la latitude en degrés
     */
    public double latDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format("(lon=% .4f° , lat=%.4f°)", lonDeg(), latDeg());
    }
}
