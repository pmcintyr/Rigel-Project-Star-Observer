package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Conversion de coordonles écliptiques en horizontales
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final double epsilon, cosEpsilon, sinEpsilon, julianCentury;

    /**
     * Construit le changement de coordonées pour when
     *
     * @param when couple date/heure
     */

    public EclipticToEquatorialConversion(ZonedDateTime when) {
        julianCentury = Epoch.J2000.julianCenturiesUntil(when);
        epsilon = Polynomial.of(Angle.ofArcsec(0.00181),
                Angle.ofArcsec(-0.0006),
                Angle.ofArcsec(-46.815),
                Angle.ofDMS(23, 26, 21.45))
                .at(julianCentury);
        cosEpsilon = Math.cos(epsilon);
        sinEpsilon = Math.sin(epsilon);
    }

    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        double alpha = Math.atan2((Math.sin(ecl.lon()) * cosEpsilon
                        - Math.tan(ecl.lat()) * sinEpsilon)
                , Math.cos(ecl.lon()));
        double delta = Math.asin(Math.sin(ecl.lat()) * cosEpsilon
                + Math.cos(ecl.lat()) * sinEpsilon * Math.sin(ecl.lon()));
        return EquatorialCoordinates.of(Angle.normalizePositive(alpha), delta);

    }

    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}
