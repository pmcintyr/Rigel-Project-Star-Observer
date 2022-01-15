package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Représente un modèle d'un objet céleste
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 *
 * @param <O>
 *         l'objet céleste en question
 */

public interface CelestialObjectModel <O> {

    /**
     *
     * @param daysSinceJ2010
     *              le nombre de jours depuis l'époque J2010 (peut être négatif)
     * @param eclipticToEquatorialConversion
     *          conversion fournie afin d'obtenir les coordonnées équatoriales
     *          de l'objet à partir de ses coordonnées écliptiques
     * @return
     *         Retourne l'objet modélisé pour le nombre de jours après l'époque J2010
     *
     */

    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
