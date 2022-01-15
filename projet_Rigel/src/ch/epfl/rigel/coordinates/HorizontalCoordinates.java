package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Coordonnées horizontales
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class HorizontalCoordinates extends SphericalCoordinates {

    private final static ClosedInterval CLOSED_INTERVAL_HAUTEUR_DEG = ClosedInterval.symmetric(180);
    private final static ClosedInterval CLOSED_INTERVAL_HAUTEUR_RAD = ClosedInterval.symmetric(Math.PI);
    private final static RightOpenInterval RIGHT_OPEN_INTERVAL_AZIMUT_DEG = RightOpenInterval.of(0, 360);
    private final static RightOpenInterval RIGHT_OPEN_INTERVAL_AZIMUT_RAD = RightOpenInterval.of(0, Math.PI * 2);


    private HorizontalCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Construit des coordonées horizontales à partir de paramètres en radians
     *
     * @param az  l'azimut en radians
     * @param alt la hauteur en radians
     * @return des coordonéées horizontales
     * @throws IllegalArgumentException si l'azimut ou la hauteur n'appariennent pas à l'intervalle
     *                                  qui leur est attribué.
     */
    public static HorizontalCoordinates of(double az, double alt) {

        return new HorizontalCoordinates(Preconditions.checkInInterval(RIGHT_OPEN_INTERVAL_AZIMUT_RAD, az)
                , Preconditions.checkInInterval(CLOSED_INTERVAL_HAUTEUR_RAD, alt));
    }

    /**
     * Construit des coordonées horizontales à partir de paramètres en degrés
     *
     * @param azDeg  l'azimut en degrés
     * @param altDeg la hauteur en degrés
     * @return des coordonéées horizontales
     * @throws IllegalArgumentException si l'azimut ou la hauteur n'appariennent pas à l'intervalle
     *                                  qui leur est attribué.
     */

    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {

        return new HorizontalCoordinates(Angle.ofDeg(Preconditions.checkInInterval(RIGHT_OPEN_INTERVAL_AZIMUT_DEG, azDeg))
                , Angle.ofDeg(Preconditions.checkInInterval(CLOSED_INTERVAL_HAUTEUR_DEG, altDeg)));
    }

    /**
     * @return l'azimut en radians
     */

    public double az() {
        return super.lon();
    }

    /**
     * @return l'azimut en degrés
     */

    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * @return la hauteur en radians
     */

    public double alt() {
        return super.lat();
    }

    /**
     * @return la hauteur en degrés
     */

    public double altDeg() {
        return super.latDeg();
    }

    /**
     * Affiche un message nous donnant l'information de l'octant dans lequel se trouve notre point
     *
     * @param n le nord
     * @param e l'est
     * @param s le sud
     * @param w l'ouest
     * @return un message contenat une des huit informations suivantes:
     * N,NE,E,SE,S,SW,W,NW
     */

    public String azOctantName(String n, String e, String s, String w) {
        RightOpenInterval[] octants = new RightOpenInterval[7];
        for (int c = 0; c < 7; ++c) {
            octants[c] = RightOpenInterval.of(22.5 + (45) * c, 22.5 + (45) * (c + 1));
        }
        if (octants[0].contains(azDeg())) {
            return n + e;
        }
        if (octants[1].contains(azDeg())) {
            return e;
        }
        if (octants[2].contains(azDeg())) {
            return s + e;
        }
        if (octants[3].contains(azDeg())) {
            return s;
        }
        if (octants[4].contains(azDeg())) {
            return s + w;
        }
        if (octants[5].contains(azDeg())) {
            return w;
        }
        if (octants[6].contains(azDeg())) {
            return n + w;
        } else {
            return n;
        }
    }


    /**
     * Calcul la distance angulaire entre deux points de l'espace
     *
     * @param that le point donné en argument
     * @return la distance angulaire en that et le récepteur this.
     */
    public double angularDistanceTo(HorizontalCoordinates that) {

        return (Math.acos(Math.sin(this.alt()) * Math.sin(that.alt()) + Math.cos(this.alt())
                * Math.cos(that.alt()) * Math.cos((this.lon()) - that.lon())));
    }

    @Override
    public String toString() {
        return String.format("(az=%.4f° , alt=%.4f°)", azDeg(), altDeg());
    }
}

