package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class RightOpenInterval extends Interval {
    private RightOpenInterval(double min, double max) {
        super(min, max);
    }

    /**
     * Construit un intervalle semi-ouvert à droite.
     *
     * @param low  la borne inférieure de l'intervalle
     * @param high la borne supérieure de l'intervalle
     */
    public static RightOpenInterval of(double low, double high) {
            Preconditions.checkArgument( low < high);
            RightOpenInterval rightOpenInterval = new RightOpenInterval(low, high);
            return rightOpenInterval;
    }

    /**
     * Retourne un intervalle semi-ouvert à droite,
     * symmétrique et de taille size
     *
     * @param size la taille de l'intervalle
     * @return Un intervalle symmétrique, semi-ouvert à droite
     */
    public static RightOpenInterval symmetric(double size) {
            Preconditions.checkArgument(size > 0);
            RightOpenInterval rightOpenInterval = new RightOpenInterval(-size / 2, size / 2);
            return rightOpenInterval;
    }

    /**
     * Réduit l'argument à l'intervalle.
     *
     * @param v l'argument
     * @return la valuer réduite de v, qui se trouve dans l'intervalle
     */
    public double reduce(double v) {
        return low() + floorMod(v - low(), size());
    }

    private static double floorMod(double x, double y) {
        return x - y * Math.floor(x / y);
    }

    @Override
    public boolean contains(double v) {
        return  (v >= low() && v < high());
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "[%2.f, %2.f[", low(), high());
    }
}
