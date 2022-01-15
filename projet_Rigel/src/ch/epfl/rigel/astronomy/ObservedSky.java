package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;

/**
 * Classe représentant une photgraphie du ciel, à un moment et à un endroit donnés.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public class ObservedSky {

    private double[][] planetPositions, starsPositions;

    private Moon moon;
    private CartesianCoordinates moonProjectedPosition;

    private Sun sun;
    private CartesianCoordinates sunProjectedPosition;

    private List<Planet> planets;

    private StarCatalogue starCatalogue;


    /**
     * Construit le ciel observé, calcul la position projetée dans le plan des objets célestes.
     *
     * @param zonedDateTime           l'instant d'observation
     * @param geographicCoordinates   la position d'observation
     * @param stereographicProjection la projection à utiliser
     * @param starCatalogue           le catalogue contenant les étoiles et les astérismes
     */


    public ObservedSky(ZonedDateTime zonedDateTime, GeographicCoordinates geographicCoordinates,
                       StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {

        this.starCatalogue = starCatalogue;

        double days = J2010.daysUntil(zonedDateTime);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(zonedDateTime);
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(zonedDateTime, geographicCoordinates);

        moon = MoonModel.MOON.at(days, eclipticToEquatorialConversion);
        HorizontalCoordinates moonHorizontalCoord = equatorialToHorizontalConversion.apply(moon.equatorialPos());
        moonProjectedPosition = stereographicProjection.apply(moonHorizontalCoord);

        sun = SunModel.SUN.at(days, eclipticToEquatorialConversion);
        HorizontalCoordinates sunHorizontalCoord = equatorialToHorizontalConversion.apply(sun.equatorialPos());
        sunProjectedPosition = stereographicProjection.apply(sunHorizontalCoord);


        planets = new ArrayList<>();
        planets.add(new Planet("Mercure",
                PlanetModel.MERCURY.at(days, eclipticToEquatorialConversion).equatorialPos(),
                (float) PlanetModel.MERCURY.at(days, eclipticToEquatorialConversion).angularSize(),
                (float) PlanetModel.MERCURY.at(days, eclipticToEquatorialConversion).magnitude()));
        planets.add(new Planet("Mars",
                PlanetModel.MARS.at(days, eclipticToEquatorialConversion).equatorialPos(),
                (float) PlanetModel.MARS.at(days, eclipticToEquatorialConversion).angularSize(),
                (float) PlanetModel.MARS.at(days, eclipticToEquatorialConversion).magnitude()));
        planets.add(new Planet("Jupiter",
                PlanetModel.JUPITER.at(days, eclipticToEquatorialConversion).equatorialPos(),
                (float) PlanetModel.JUPITER.at(days, eclipticToEquatorialConversion).angularSize(),
                (float) PlanetModel.JUPITER.at(days, eclipticToEquatorialConversion).magnitude()));
        planets.add(new Planet("Saturne",
                PlanetModel.SATURN.at(days, eclipticToEquatorialConversion).equatorialPos(),
                (float) PlanetModel.SATURN.at(days, eclipticToEquatorialConversion).angularSize(),
                (float) PlanetModel.SATURN.at(days, eclipticToEquatorialConversion).magnitude()));
        planets.add(new Planet("Vénus",
                PlanetModel.VENUS.at(days, eclipticToEquatorialConversion).equatorialPos(),
                (float) PlanetModel.VENUS.at(days, eclipticToEquatorialConversion).angularSize(),
                (float) PlanetModel.VENUS.at(days, eclipticToEquatorialConversion).magnitude()));
        planets.add(new Planet("Uranus",
                PlanetModel.URANUS.at(days, eclipticToEquatorialConversion).equatorialPos(),
                (float) PlanetModel.URANUS.at(days, eclipticToEquatorialConversion).angularSize(),
                (float) PlanetModel.URANUS.at(days, eclipticToEquatorialConversion).magnitude()));
        planets.add(new Planet("Neptune",
                PlanetModel.NEPTUNE.at(days, eclipticToEquatorialConversion).equatorialPos(),
                (float) PlanetModel.NEPTUNE.at(days, eclipticToEquatorialConversion).angularSize(),
                (float) PlanetModel.NEPTUNE.at(days, eclipticToEquatorialConversion).magnitude()));

        planetPositions = new double[7][2];
        for (int i = 0; i < planets.size(); i++) {
            HorizontalCoordinates planetHorizontalCoord = equatorialToHorizontalConversion.apply(planets.get(i).equatorialPos());
            CartesianCoordinates planetProjectedPosition = stereographicProjection.apply(planetHorizontalCoord);
            planetPositions[i][0] = planetProjectedPosition.x();
            planetPositions[i][1] = planetProjectedPosition.y();
        }

        int catalogueSize = starCatalogue.stars().size();
        starsPositions = new double[catalogueSize][2];
        for (int i = 0; i < catalogueSize; i++) {
            HorizontalCoordinates starHorizontalCoord = equatorialToHorizontalConversion.apply(starCatalogue.stars().get(i).equatorialPos());
            CartesianCoordinates starProjectedPosition = stereographicProjection.apply(starHorizontalCoord);
            starsPositions[i][0] = starProjectedPosition.x();
            starsPositions[i][1] = starProjectedPosition.y();
        }

    }

    /**
     * une méthode d'accès permettant d'obtenir le soleil
     *
     * @return retourne l'instance de Sun
     */

    public Sun getSun() {
        return sun;
    }

    /**
     * une méthode donnant la position du Sun dans le plan
     *
     * @return retourne la position du Sun dans le plan
     */

    public  CartesianCoordinates getSunProjectedPosition() {
        return sunProjectedPosition;
    }

    /**
     * une méthode d'accès permettant d'obtenir la lune
     *
     * @return l'instance de Moon
     */

    public Moon getMoon() {
        return moon;
    }

    /**
     * une méthode donnant la position du Moon dans le plan
     *
     * @return la position du Moon dans le plan
     */

    public CartesianCoordinates getMoonProjectedPosition() {
        return moonProjectedPosition;
    }

    /**
     * une méthode d'accès permettant d'obtenir une liste de planètes
     *
     * @return la liste d'instances de Planet
     */

    public List<Planet> getPlanets() {
        return List.copyOf(planets);
    }

    /**
     * méthode d'accès aux posiitons des planètes dans le plan
     *
     * @return un tableau contenant les positions des planètes dans le plan
     */

    public double[][] getPlanetsProjectedPositions() {
        return planetPositions;
    }

    /**
     * méthode d'accès aux étoiles
     *
     * @return une liste d'étoiles
     */

    public List<Star> getStarList() {
        return starCatalogue.stars();
    }

    /**
     * méthodes d'accès aux positions des étoiles dans le plan
     *
     * @return un tableau contenant les positions des étoiles, dans le plan
     */

    public double[][] getStarsProjectedPositions() {
        return starsPositions;
    }

    /**
     * méthode d'accès aux astérismes
     *
     * @return un ensemble contenant les astérismes
     */

    public Set<Asterism> getAsterism() {
        return starCatalogue.asterisms();
    }

    /**
     * Méthode permettant d'obtenir une liste contenant les index des étoiles d'un astérisme
     *
     * @param asterism l'astérisme en question
     * @return une liste contenant les index de cet astérisme
     */

    public List<Integer> getStarsIndex(Asterism asterism) {
        return starCatalogue.asterismIndices(asterism);
    }

    /**
     * Nous permet d'obtenir l'objet céleste le plus proche de coordonnées données, en respectant une distance maximum
     *
     * @param cartesianCoordinates les coordonnées du plan, données
     * @param max                  la distance maximale à respecter
     * @return l'objet le plus proche des coordonnées du plan, se trouvant à une distance inférieur à max
     */

    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates cartesianCoordinates, double max) {

        Preconditions.checkArgument(max > 0);

        Map<Double, CelestialObject> allDistances = new HashMap<>();

        double moonDist = computeDistance(cartesianCoordinates, getMoonProjectedPosition());
        allDistances.put(moonDist, getMoon());

        double sunDist = computeDistance(cartesianCoordinates, getSunProjectedPosition());
        allDistances.put(sunDist, getSun());

        for (int i = 0; i < getPlanets().size(); i++) {
            double planetDist = computeDistance(cartesianCoordinates,
                    CartesianCoordinates.of(getPlanetsProjectedPositions()[i][0], getPlanetsProjectedPositions()[i][1]));
            allDistances.put(planetDist, getPlanets().get(i));
        }

        for (int i = 0; i < getStarList().size(); i++) {
            double starDist = computeDistance(cartesianCoordinates,
                    CartesianCoordinates.of(getStarsProjectedPositions()[i][0], getStarsProjectedPositions()[i][1]));
            allDistances.put(starDist, getStarList().get(i));
        }

        double minDistance = Math.min(moonDist, sunDist);

        for (Double aDouble : allDistances.keySet()) {
            minDistance = (aDouble < minDistance) ? aDouble : minDistance;
        }
        if (minDistance < max) {
            return (Optional.of(allDistances.get(minDistance)));
        }
        return Optional.empty();

    }

    private double computeDistance(CartesianCoordinates c1, CartesianCoordinates c2) {
        return Math.sqrt(Math.pow(c1.x() - c2.x(), 2) + Math.pow(c1.y() - c2.y(), 2));
    }
}
