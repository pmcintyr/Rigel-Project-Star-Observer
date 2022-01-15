package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class Polynomial {

    private final double[] finalCoeff;

    private Polynomial(double[] coeff) {
        finalCoeff = coeff;
    }

    /**
     * Construit un polynôme avec les coefficients fournis en paramètres
     *
     * @param coefficientN le coefficient de la plus grande puissance
     * @param coefficients les coefficients des puissances restantes,
     *                     apparaissent dans l'ordre décroissant par rapport
     *                     aux puissances auxquelles ils sont associés
     *                     (le coeff de x^4 vient avant celui de x^3, p.ex)
     * @return un polynôme
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(!(coefficientN == 0));
        double[] poly = new double[coefficients.length + 1];
        poly[0] = coefficientN;
        for (int i = 1; i <= coefficients.length; ++i) {
            poly[i] = coefficients[i - 1];
        }
        Polynomial polynomial = new Polynomial(poly);
        return polynomial;
    }

    /**
     * Retourne la valeur de la fonction polynomial pour l'argument donné
     *
     * @param x l'argument
     * @return la valeur du polynôme pour x
     */
    public double at(double x) {
        double somme = finalCoeff[0];
        for (int j = 0; j < finalCoeff.length - 1; ++j) {
            somme = somme * x + finalCoeff[j + 1];
        }
        return somme;
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < finalCoeff.length; ++i) {

            // cas pour la puissance la plus grande

            if (i == 0 && finalCoeff[i] == -1) {
                if (!(finalCoeff[i] == 0)) {
                    stringBuilder.append("-x^" + (finalCoeff.length - 1));
                }
                // ajoute + ssi le coeff d'après et positif

                if (finalCoeff[i + 1] > 0) {
                    stringBuilder.append("+");
                }
            }

            if (i < finalCoeff.length - 2 && !(finalCoeff[0] == -1)) {

                //affiche le coeff que si il est different de 0

                if (!(finalCoeff[i] == 1) && !(finalCoeff[i] == -1) && !(finalCoeff[i] == 0)) {
                    stringBuilder.append((finalCoeff[i]));
                }
                if (!(finalCoeff[i] == 0)) {
                    stringBuilder.append("x^" +
                            (finalCoeff.length - 1 - i));
                }
                if (finalCoeff[i + 1] > 0) {
                    stringBuilder.append("+");
                }
            }

            // Cas x^1 car on ne veut pas afficher ^1 comme avant

            if (i == finalCoeff.length - 2) {
                if (!(finalCoeff[i] == 1) && !(finalCoeff[i] == -1) && !(finalCoeff[i] == 0)) {
                    stringBuilder.append((finalCoeff[i]));
                }
                if (!(finalCoeff[i] == 0)) {
                    stringBuilder.append("x");
                }
                if (finalCoeff[i + 1] > 0) {
                    stringBuilder.append("+");
                }
                ;
            }

            // Cas x^0 car on ne veut pas afficher x^0 et on ne met pas de signe d'opération +/-

            if (i == finalCoeff.length - 1 && !(finalCoeff[i] == 0)) {
                stringBuilder.append((finalCoeff[i]));
            }
        }

        return stringBuilder.toString();
    }
}
