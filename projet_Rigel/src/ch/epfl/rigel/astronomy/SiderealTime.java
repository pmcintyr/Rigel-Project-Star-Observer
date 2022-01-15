package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 * Calcul du temps sidéral Greenwich et Local
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class SiderealTime {

    private final static double MILLISECONDS_IN_AN_HOUR = 3600000;

    private SiderealTime() {
    }

    /**
     * @param when couple date/heure
     * @return le temps sidéral de Greenwich pour when, en radians, compris dans
     * l'intervalle [0, TAU [
     */

    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime whenUTC = when.withZoneSameInstant(ZoneId.of("UTC"));

        double T = J2000.julianCenturiesUntil(whenUTC.truncatedTo(ChronoUnit.DAYS));
        double t = whenUTC.truncatedTo(ChronoUnit.DAYS).until(whenUTC, ChronoUnit.MILLIS) / MILLISECONDS_IN_AN_HOUR;

        double s0 = Polynomial.of(0.000_025_862, 2400.051336, 6.697374558).at(T);
        double s1 = 1.002737909 * t;
        double sg = s0 + s1;
        double hr = RightOpenInterval.of(0, 24).reduce(sg);
        double rad = Angle.ofHr(hr);
        return rad;
    }

    /**
     * @param when  couple date/heure
     * @param where position en coordonées géographiques
     * @return le temps sidéral local, en radians, compris dans l'intervalle [0, TAU [
     */

    public final static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }
}
