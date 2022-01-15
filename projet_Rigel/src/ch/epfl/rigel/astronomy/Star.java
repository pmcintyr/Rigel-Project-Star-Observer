package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Classe représentant une étoile
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class Star extends CelestialObject {

    private final int hipparcosId;
    private final float colorIndex, distance, rv;
    private final static ClosedInterval CLOSED_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    /**
     * Construit une étoile selon les données qui lui sont fournies
     *
     * @param hipparcosId   le numéro Hipparcos de l'étoile
     * @param name          le nom de l'étoile
     * @param equatorialPos la position de l'étoile en coordonées équatoriales
     * @param magnitude     la magnitude de l'étoile
     * @param colorIndex    l'indice de couleur de l'étoile
     */

    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex, float distance, float rv) {

        super(name, equatorialPos, 0, magnitude);
        this.colorIndex = colorIndex;
        this.hipparcosId = hipparcosId;
        this.distance = distance;
        this.rv = rv;

        Preconditions.checkInInterval(CLOSED_INTERVAL, colorIndex);
        Preconditions.checkArgument(hipparcosId >= 0);
    }

    /**
     * @return retourne le numéro hipparcos de l'étoile
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * @return retourne la température de couleur de l'étoile
     */
    public int colorTemperature() {
        double T = 4600 * (1 / (0.92 * colorIndex + 1.7) + (1 / (0.92 * colorIndex + 0.62)));
        return (int) Math.floor(T);
    }
    @Override
    public double distance() {
        return distance;
    }
    @Override
    public double rv(){
        return rv;
    }
}
