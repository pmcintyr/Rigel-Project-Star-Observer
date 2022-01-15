package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;

/**
 * Classe représentant un bean contenant la position de l'observateur, exprimée en degrés
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public class ObserverLocationBean {

    private final DoubleProperty lonDegPosition = new SimpleDoubleProperty();
    private final DoubleProperty latDegPosition = new SimpleDoubleProperty();

    private final ObservableObjectValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(
            () -> GeographicCoordinates.ofDeg(getLonDegPosition(), getLatDegPosition()),
            lonDegPosition, latDegPosition
    );

    /**
     * Construit le bean en question
     */
    public ObserverLocationBean() {

    }

    /**
     * @return les coordonnées géographiques de la position de l'observateur
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }

    /**
     * @return la propriété des coordonnées de l'observateur
     */
    public ObservableObjectValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Modifie les coordonnées de l'observateur en fonction de celles fournies
     *
     * @param coordinates les coordonnées fournies
     */
    public void setCoordinates(GeographicCoordinates coordinates) {
        setLonDegPosition(coordinates.lonDeg());
        setLatDegPosition(coordinates.latDeg());
    }

    /**
     * @return la longitude de l'observateur
     */
    public double getLonDegPosition() {
        return lonDegPosition.get();
    }

    /**
     * @return la propriété de la longitude de l'observateur
     */

    public DoubleProperty lonDegPositionProperty() {
        return lonDegPosition;
    }

    /**
     * Moodifie la longitude de l'observateur en fonction de celle fournie
     *
     * @param lonDegPosition la longitude fournie
     */

    public void setLonDegPosition(Double lonDegPosition) {
        this.lonDegPosition.set(lonDegPosition);
    }

    /**
     * @return la latitude de l'observateur
     */

    public double getLatDegPosition() {
        return latDegPosition.get();
    }

    /**
     * @return la propriété de la latitude de l'observateur
     */

    public DoubleProperty latDegPositionProperty() {
        return latDegPosition;
    }

    /**
     * Modifie la latitude de l'observateur en fonction de celle fournie
     *
     * @param latDegPosition la latitude fournie
     */

    public void setLatDegPosition(Double latDegPosition) {
        this.latDegPosition.set(latDegPosition);
    }
}
