package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Classe pour construire le soleil.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public final class Sun extends CelestialObject {

    private final EclipticCoordinates eclipticPos;

    private final float meanAnomaly;

    /**
     * Construit le soleil
     *
     * @param eclipticPos   position écliptique du soleil
     * @param equatorialPos position équatoriale du soleil
     * @param angularSize   taille angulaire du soleil
     * @param meanAnomaly   anomalie moyenne du soleil
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);
        this.eclipticPos =  Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * Retourne les coordonnées écliptiques
     *
     * @return retourne les coordonnées écliptiques
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * Retourne l'anomalie moyenne
     *
     * @return retourne l'anomalie moyenne
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }
}

