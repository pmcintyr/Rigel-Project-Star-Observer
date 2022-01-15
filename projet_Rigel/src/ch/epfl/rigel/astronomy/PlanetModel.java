package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * Représente le modèle de planètes
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public enum PlanetModel implements CelestialObjectModel<Planet> {

    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    /**
     * Liste contenant la totalité des planètes ci-dessus
     */

    public final static List<PlanetModel> ALL = List.of(MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE);

    private final String frenchName;
    private final double Tp, epsilon, w, e, a, i, omega, theta0, V0;

    PlanetModel(String frenchName, double Tp, double epsilon, double w, double e
            , double a, double i, double omega, double theta0, double V0) {

        this.frenchName = frenchName;
        this.theta0 = theta0;
        this.Tp = Tp;
        this.epsilon = epsilon;
        this.w = w;
        this.e = e;
        this.i = i;
        this.a = a;
        this.omega = omega;
        this.V0 = V0;
    }

    private final static double ANGULAR_SPEED = Angle.TAU / 365.242191;
    private final static double EARTH_W_RAD = Angle.ofDeg(EARTH.w);
    private final static double EARTH_EPSILON_RAD = Angle.ofDeg(EARTH.epsilon);

    private double getEarthTrueAnomaly(double daysSinceJ2010) {
        double meanAnomaly = (ANGULAR_SPEED) * daysSinceJ2010 / EARTH.Tp + EARTH_EPSILON_RAD
                - EARTH_W_RAD;
        double trueAnomaly = meanAnomaly + 2 * EARTH.e * Math.sin(meanAnomaly);
        return trueAnomaly;
    }

    private double getEarthLon(double daysSinceJ2010) {
        double Lon = getEarthTrueAnomaly(daysSinceJ2010) + EARTH_W_RAD;
        return Lon;
    }

    private double getEarthRadius(double daysSinceJ2010) {
        double R = EARTH.a * (1 - Math.pow(EARTH.e, 2)) / (1 + EARTH.e * Math.cos(getEarthTrueAnomaly(daysSinceJ2010)));
        return R;
    }

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double meanAnomaly = (ANGULAR_SPEED) * (daysSinceJ2010 / Tp) + Angle.ofDeg(epsilon) - Angle.ofDeg(w);
        double trueAnomaly = meanAnomaly + 2 * e * Math.sin(meanAnomaly);
        double r = a * (1 - Math.pow(e, 2)) / (1 + e * Math.cos(trueAnomaly));
        double l = trueAnomaly + Angle.ofDeg(w);
        double psy = Math.asin(Math.sin(l - Angle.ofDeg(omega)) * Math.sin(Angle.ofDeg(i)));

        double radiusProjection = r * Math.cos(psy);
        double heliocentricLonEcl = Angle.normalizePositive(Math.atan2(Math.sin(l - Angle.ofDeg(omega)) * Math.cos(Angle.ofDeg(i)),
                Math.cos(l - Angle.ofDeg(omega))) + Angle.ofDeg(omega));

        double L = getEarthLon(daysSinceJ2010);
        double R = getEarthRadius(daysSinceJ2010);
        double lambda = 0;

        double constant = R * Math.sin(heliocentricLonEcl - L);
        if (r < 1) {
            lambda = Angle.normalizePositive(Math.PI + L + Math.atan2(radiusProjection * Math.sin(L - heliocentricLonEcl)
                    , R - radiusProjection * Math.cos(L - heliocentricLonEcl)));
        } else if (r > 1) {
            lambda = Angle.normalizePositive(heliocentricLonEcl + Math.atan2(constant
                    , radiusProjection - R * Math.cos(heliocentricLonEcl - L)));
        }
        double beta = Math.atan(radiusProjection * Math.tan(psy) * Math.sin(lambda - heliocentricLonEcl) / (constant));

        double ro = Math.sqrt(Math.pow(R, 2) + Math.pow(r, 2) - 2 * R * r * Math.cos(l - L) * Math.cos(psy));
        double theta = Angle.ofArcsec(theta0) / ro;

        double F = (1 + Math.cos(lambda - l)) / 2;
        double m = V0 + 5 * Math.log(r * ro / Math.sqrt(F)) / Math.log(10);

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lambda, beta);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        Planet planet = new Planet(frenchName, equatorialCoordinates, (float) theta, (float) m);

        return planet;
    }
}
