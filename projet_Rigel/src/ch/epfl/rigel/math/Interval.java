package ch.epfl.rigel.math;

/**
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public abstract class Interval {

    private final double min, max;

    protected Interval(double min, double max) {

        this.min = min;
        this.max = max;
    }

    /**
     * Pour obtenir la borne inférieure
     *
     * @return la borne inférieure
     */

    public double low() {
        return min;
    }

    /**
     * Pour obtenir la borne supérieure
     *
     * @return la borne supérieure
     */
    public double high() {
        return max;
    }

    /**
     * Pour obtenir la taille de l'intervalle
     *
     * @return la taille de l'intervalle
     */
    public double size() {
        return (max - min);
    }

    /**
     * Vérifie si une valeur est contenu dans l'intervalle
     *
     * @param v la valeur en question
     * @return vrai si v est contenu dans l'intervalle,
     * faux autrement
     */
    public abstract boolean contains(double v);

    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}
