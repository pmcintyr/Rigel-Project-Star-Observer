package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Conversion des coordonées équatoriales en Horizontales
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double lat, cosLat, sinLat, siderealTime;

    /**
     * Construit la conversion des coordonées pour le couplpe when (le moment) et where (observateur)
     *
     * @param when  le moment date/heure
     * @param where le  lieux d'observation
     */

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        lat = where.lat();
        cosLat = Math.cos(lat);
        sinLat = Math.sin(lat);
        siderealTime = SiderealTime.local(when, where);
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double delta = equ.dec();
        double alpha = equ.ra();
        double H = siderealTime - alpha;
        double h = Math.asin(Math.sin(delta) * sinLat + Math.cos(delta) * cosLat * Math.cos(H));
        double A = Math.atan2(-Math.cos(delta) * cosLat * Math.sin(H),
                Math.sin(delta) - sinLat * Math.sin(h));
        return HorizontalCoordinates.of(Angle.normalizePositive(A), h);
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
