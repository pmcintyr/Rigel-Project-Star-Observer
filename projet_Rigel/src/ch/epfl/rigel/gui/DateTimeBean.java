package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Classe représentant un bean javaFX contenant l'instant d'observation, c'est-à-dire le triplet (date,heure,fuseau horaire)
 */

public final class DateTimeBean {

    private final ObjectProperty<LocalTime> timeObjectProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateObjectProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ZoneId> idObjectProperty = new SimpleObjectProperty<>();

    /**
     * @return la valeur contenue dans la propriété de la date
     */
    public LocalDate getDateObjectProperty() {
        return dateObjectProperty.get();
    }

    /**
     * @return la propriété contenant la date
     */
    public ObjectProperty<LocalDate> dateObjectProperty() {
        return dateObjectProperty;
    }

    /**
     * Modifie la date en focntion de la date fournie
     *
     * @param dateObject la date fournie
     */

    public void setDateObject(LocalDate dateObject) {
        this.dateObjectProperty.set(dateObject);
    }

    /**
     * @return le temps contenu dans la propriété du temps
     */
    public LocalTime getTimeObjectProperty() {
        return timeObjectProperty.get();
    }

    /**
     * @return la propriété contenant le temps
     */
    public ObjectProperty<LocalTime> timeObjectProperty() {
        return timeObjectProperty;
    }

    /**
     * Modifie le temps en fonction de celui qui nous est fourni
     *
     * @param timeObject le temps fourni
     */
    public void setTimeObject(LocalTime timeObject) {
        this.timeObjectProperty.set(timeObject);
    }

    /**
     * @return la valeur contenue dans la propriété du fuseau horaire
     */
    public ZoneId getIdObjectProperty() {
        return idObjectProperty.get();
    }

    /**
     * @return la propriété du fuseau horaire
     */
    public ObjectProperty<ZoneId> idObjectProperty() {
        return idObjectProperty;
    }

    /**
     * Modifie le fuseau horaire en fonction de celui fourni
     *
     * @param idObject le fuseau horaire fourni
     */
    public void setIdObject(ZoneId idObject) {
        this.idObjectProperty.set(idObject);
    }

    /**
     * @return l'instant d'observation sour la forme d'une instance de ZonedDateTime
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDateObjectProperty(), getTimeObjectProperty(), getIdObjectProperty());
    }

    /**
     * Modifie l'instant d'observation en fonction de celui fourni
     *
     * @param zonedDateTime l'instant d'observation fourni
     */
    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        setDateObject(zonedDateTime.toLocalDate());
        setTimeObject(zonedDateTime.toLocalTime());
        setIdObject(zonedDateTime.getZone());
    }
}
