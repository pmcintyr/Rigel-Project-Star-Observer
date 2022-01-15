package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 *Représente le modèle d'un soleil
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public enum SunModel implements CelestialObjectModel<Sun> {

    SUN ;

    private final static double epsilonGDeg = 279.557208, epsilonGRad = Angle.ofDeg(epsilonGDeg);
    private final static double wGDeg = 283.112438, wGRad = Angle.ofDeg(wGDeg);
    private final static double e = 0.016705;
    private final static double angularSpeed = Angle.TAU / 365.242191;


    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAnomaly = (angularSpeed * daysSinceJ2010 + epsilonGRad - wGRad);
        double trueAnomaly =  (meanAnomaly + 2 * e * Math.sin(meanAnomaly));
        double sunLon = Angle.normalizePositive(trueAnomaly + wGRad);
        double sunLat = 0;
        double theta0Deg = 0.533128;
        double theta =(Angle.ofDeg(theta0Deg) * ( (1 + e * Math.cos(trueAnomaly)) / (1-Math.pow(e,2))));

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(sunLon,sunLat);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Sun(eclipticCoordinates, equatorialCoordinates,(float)theta,(float) meanAnomaly);
    }
}
