package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class Angle {

    public final static double TAU = 2 * Math.PI;

    private final static RightOpenInterval RIGHT_OPEN_INTERVAL = RightOpenInterval.of(0, TAU);
    private final static RightOpenInterval CHECKED_INTERVAL = RightOpenInterval.of(0,60);

    private Angle() {
    }

    /**
     * Normalise l'angle en le réduisant à l'intervalle [0,TAU[
     *
     * @param rad l'angle à normaliser
     * @return l'angle normalisé
     */
    public static double normalizePositive(double rad) {
        return RIGHT_OPEN_INTERVAL.reduce(rad);
    }

    /**
     * Retourne l'angle correspondant aux secondes d'arc données
     *
     * @param sec secondes d'arc
     * @return un angle en radians
     */
    public static double ofArcsec(double sec) {
        return Angle.ofDeg((sec / 3600) % 360);
    }

    /**
     * Retourne l'angle en radians correspondant aux degrés, minutes et secondes d'arc donnés
     *
     * @param deg les degrés
     * @param min les minutes d'arc
     * @param sec les secondes d'arc
     * @return l'angle correspondant, en radians
     */
    public static double ofDMS(int deg, int min, double sec) {
        Preconditions.checkArgument(deg >= 0);
        Preconditions.checkInInterval(CHECKED_INTERVAL,min);
        Preconditions.checkInInterval(CHECKED_INTERVAL,sec);
        double degToRad = Math.toRadians(deg);
        double minToRad = Math.toRadians(min / 60.0);
        double secToRad = Math.toRadians(sec / 3600.0);
        double finalRad = degToRad + minToRad + secToRad;
        return finalRad;
    }


    /**
     * Retourne l'angle en radians correspondant à langle donné en degrés
     *
     * @param deg l'angle en degrés
     * @return un angle en radians
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Converti un angle exprimé en radians en degrés
     *
     * @param rad l'angle en radians
     * @return l'angle en degrés
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Converti un angle exprimé en heures en radians
     *
     * @param hr l'angle en heures
     * @return l'angle en radians
     */
    public static double ofHr(double hr) {
        return (hr * TAU) / 24;
    }

    /**
     * Converti un angle exprimé en radians en heures
     *
     * @param rad l'angle en radians
     * @return l'angle en heures
     */
    public static double toHr(double rad) {
        return (rad * 24) / TAU;
    }
}
