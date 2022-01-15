package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Coordonnées sphériques
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

abstract class SphericalCoordinates {

    private final double lon, lat;

    SphericalCoordinates(double lon, double lat) {
        this.lat = lat;
        this.lon = lon;
    }

    double lon() {
        return lon;
    }

    double lonDeg() {
        return Angle.toDeg(lon);
    }

    double lat() {
        return lat;
    }

    double latDeg() {
        return Angle.toDeg(lat);
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
