package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;


/**
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public final class ClosedInterval extends Interval {

    private ClosedInterval(double min, double max) {
        super(min, max);
    }

    /**
     * Créer un intervalle fermé allant de low à high.
     *
     * @param low  la borne inférieure
     * @param high la borne supérieure
     * @return un intervalle fermé
     */
    public static ClosedInterval of(double low, double high) {
            Preconditions.checkArgument(low < high);
            ClosedInterval closedInterval = new ClosedInterval(low, high);
            return closedInterval;
    }

    /**
     * Retourne un intervalle fermé de taille size, centré en 0.
     *
     * @param size la taille de l'intervalle
     * @return un intervalle fermé
     */
    public static ClosedInterval symmetric(double size) {
            Preconditions.checkArgument(size > 0);
            ClosedInterval closedInterval = new ClosedInterval(-size / 2, size / 2);
            return closedInterval;
    }

    /**
     * Ecrête son argument à l'intervalle
     *
     * @param v la valeur à écrêter
     * @return la nouvelle valeur écrêtée de v
     */
    public double clip(double v) {

        if (v <= low()) {
            v = low();
        } else if (v > high()) {
            v = high();
        }
        return v;
    }

    @Override
    public boolean contains(double v) {
        return  (v >= low() && v <= high());
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "[%2.f, %2.f]", low(), high());
    }
}
