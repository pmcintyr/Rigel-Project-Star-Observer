package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

/**
 * Classe représentant un animateur de temps
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class TimeAnimator extends AnimationTimer {

    private DateTimeBean dateTimeBean;

    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private final BooleanProperty running = new SimpleBooleanProperty();

    private  long time, deltaTime;

    private static boolean hasBeenHandled = false;

    /**
     * Construit l'animateur de temps, qui modifie l'instant d'observation au travers d'un accélérateur de temps
     * contenu dans une instance de DateTimeBean
     *
     * @param dateTimeBean l'instance de DateTimeBean
     */

    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
    }

    @Override
    public void handle(long now) {
        if(hasBeenHandled){
            deltaTime = now - time;
            dateTimeBean.setZonedDateTime(getAccelerator().adjust(dateTimeBean.getZonedDateTime(),deltaTime));
        } else hasBeenHandled = true;

            time = now;
    }

    /**
     *
     * @return l'accélérateur de temps
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     *
     * @return la propriété de l'accélérateur de temps
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * Modifie l'accélérateur de temps en fonction de celui fourni
     *
     * @param accelerator l'accélérateur fourni
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }

    /**
     *
     * @return la propriété contenant l'état de l'animateur : vrai si l'animation est en cours, faux autrement
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return running;
    }


    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    @Override
    public void start() {
        super.start();
        running.set(true);
    }
}
