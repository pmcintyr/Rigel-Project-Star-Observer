package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Classe utilitaire, permet de valider les arguments
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Lance une IllegalArgumentException si son argument est faux,
     * ne fait rien autrement.
     *
     * @param isTrue l'argument
     * @throws IllegalArgumentException si l'argument est faux.
     */

    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Vérifie que value appartient à l'intervalle, la retourne.
     *
     * @param interval l'intervalle en question
     * @param value    la valeur
     * @return value si elle appartient à l'intervalle.
     * @throws IllegalArgumentException si value n'appartient pas
     *                                  à l'intervalle.
     */

    public static double checkInInterval(Interval interval, double value) {
        if (!interval.contains(value)) {
            throw new IllegalArgumentException(value + " pas dans intervalle");
        }
        return value;
    }

}
