package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe représentant un bean contenant les paramètres déterminant la portion du ciel visible sur l'image.
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public class ViewingParametersBean {

    private final ObjectProperty<Double> fieldOfViewDeg = new SimpleObjectProperty<>();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>();

    /**
     * Constructeur du bean
     */

    public ViewingParametersBean() {
    }

    /**
     * @return la valeur du champ de vue, exprimée en degrés
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * @return la propriété du champ de vue
     */
    public ObjectProperty<Double> fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * Modifie le champ de vue en focntion de celui fourni
     *
     * @param fieldOfViewDeg le champ de vue fourni
     */
    public void setFieldOfViewDeg(double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * @return les coordonnées du centre de projection
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * @return la propriété du centre de projection
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * Modifie le centre de projection en fonction des coordonnées fournies
     *
     * @param center les coordonnées fournies
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}
