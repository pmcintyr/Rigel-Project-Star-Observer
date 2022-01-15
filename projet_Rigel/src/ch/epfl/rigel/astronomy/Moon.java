package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;


/**
 * Classe pour construire la lune.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public final class Moon extends CelestialObject {

    private final float phase;

    private final static ClosedInterval CLOSED_INTERVAL = ClosedInterval.of(0,1);

    /**
     * Construit la lune
     *
     * @param equatorialPos position équatoriale de la lune
     * @param angularSize   taille angulaire de la lune
     * @param magnitude     magnitude de la lune
     * @param phase         phase de la lune
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = phase;
        Preconditions.checkInInterval(CLOSED_INTERVAL, phase);
    }

    @Override
    public String info() {
        return String.format(Locale.ROOT,  name() +"(%.1f", phase * 100) + "%" + ")";
    }
}

