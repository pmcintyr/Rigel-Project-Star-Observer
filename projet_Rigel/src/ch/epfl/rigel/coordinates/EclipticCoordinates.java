package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonées écliptiques
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class EclipticCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval RIGHT_OPEN_INTERVAL_LON_RAD = RightOpenInterval.of(0, Math.PI * 2);
    private final static ClosedInterval CLOSED_INTERVAL_LAT_RAD = ClosedInterval.symmetric(Math.PI);

    private EclipticCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Construit des coordonées écliptiques avec la longitude et la latitude fournies
     *
     * @param lon la longitude en radians
     * @param lat la latitude en radians
     * @return des coordonnées écliptiques
     * @throws IllegalArgumentException si la longitude ou la latitude n'appartiennent pas à leur intervalle repectif.
     */
    public static EclipticCoordinates of(double lon, double lat) {

        return new EclipticCoordinates(Preconditions.checkInInterval(RIGHT_OPEN_INTERVAL_LON_RAD, lon),
                Preconditions.checkInInterval(CLOSED_INTERVAL_LAT_RAD, lat));

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
        return String.format("(\u03BB=%.4f° , \u03B2=%.4f°)", lonDeg(), latDeg());
    }
}
