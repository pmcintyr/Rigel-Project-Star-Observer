package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Représente le modèle de la lune
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;

    private final float magnitude = 0;

    private final static double l0Deg = 91.929336, l0Rad = Angle.ofDeg(l0Deg);
    private final static double P0Deg = 130.143076, P0Rad = Angle.ofDeg(P0Deg);
    private final static double N0Deg = 291.682547, N0Rad = Angle.ofDeg(N0Deg);
    private final static double iDeg = 5.145396, iRad = Angle.ofDeg(iDeg);
    private final static double e = 0.0549;


    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun sun = SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
        double SunMeanAnomaly = sun.meanAnomaly();
        double SunEclLon = sun.eclipticPos().lon();

        double l = Angle.ofDeg(13.1763_966) * daysSinceJ2010 + l0Rad;
        double MoonMeanAnomaly = l - Angle.ofDeg(0.1114_041) * daysSinceJ2010 - P0Rad;
        double Ev = Angle.ofDeg(1.2739) * Math.sin(2 * (l - SunEclLon) - MoonMeanAnomaly);
        double Ae  = Angle.ofDeg(0.1858) * Math.sin(SunMeanAnomaly);
        double A3 = Angle.ofDeg(0.37) * Math.sin(SunMeanAnomaly);
        double correctedAnomaly = MoonMeanAnomaly + Ev - Ae - A3;
        double Ec = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double A4 = Angle.ofDeg(0.214) * Math.sin(2 * correctedAnomaly);
        double correctedLon = l + Ev + Ec - Ae + A4;
        double V = Angle.ofDeg(0.6583) * Math.sin(2 * (correctedLon - SunEclLon));
        double trueOrbitalLon = correctedLon + V ;

        double N = N0Rad - Angle.ofDeg(0.0529_539) * daysSinceJ2010;
        double correctedN = N - Angle.ofDeg(0.16) * Math.sin(SunMeanAnomaly);
        double constant = trueOrbitalLon - correctedN;
        double eclLon = Angle.normalizePositive(Math.atan2(Math.sin(constant) * Math.cos(iRad)
                ,Math.cos(constant))
                + correctedN) ;
        double eclLat = Math.asin(Math.sin(constant) * Math.sin(iRad));
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(eclLon, eclLat);


        float F =(float) (1 - Math.cos(trueOrbitalLon - SunEclLon)) / 2;
        double ro = (1 - Math.pow(e , 2)) / (1 + e * Math.cos(correctedAnomaly + Ec));
        float angularSize =(float) (Angle.ofDeg(0.5181) / ro );
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Moon(equatorialCoordinates , angularSize , magnitude , F);
    }
}
