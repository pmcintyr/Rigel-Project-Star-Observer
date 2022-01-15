package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonées équatoriales
 *
 * @author Cyril Golaz (301379)
 * @author PaulMcIntyre (302264)
 */

public final class EquatorialCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval RIGHT_OPEN_INTERVAL = RightOpenInterval.of(0, Math.PI * 2);
    private final static ClosedInterval CLOSED_INTERVAL = ClosedInterval.symmetric(Math.PI);

    private EquatorialCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Construit des coordonées équatoriales avec l'ascension droite et la déclinaison donées.
     *
     * @param ra  l'ascension droite
     * @param dec la déclinaison
     * @return des coordonées éqautoriales
     * @throws IllegalArgumentException si la déclinaison (latitude) ou l'ascension droite (longitude) n'appartiennent pas
     *                                  à leur intervalle respectif.
     */
    public static EquatorialCoordinates of(double ra, double dec) {

        return new EquatorialCoordinates(Preconditions.checkInInterval(RIGHT_OPEN_INTERVAL, ra)
                , Preconditions.checkInInterval(CLOSED_INTERVAL, dec));
    }

    /**
     * @return l'ascension droite (associée à la longitude) en radians
     */
    public double ra() {
        return super.lon();
    }

    /**
     * @return l'ascension droite en en degrés
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * @return l'ascension droite en heures
     */
    public double raHr() {
        return Angle.toHr(ra());
    }

    /**
     * @return la déclinaison (associée à la latitude) en radians
     */
    public double dec() {
        return super.lat();
    }

    /**
     * @return la déclainson en degrés
     */
    public double decDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format("(ra=%.4fh , dec=%.4f°)", raHr(), decDeg());
    }
}
