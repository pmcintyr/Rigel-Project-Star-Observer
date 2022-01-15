package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Représente un accélérateur de temps
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

@FunctionalInterface
public interface TimeAccelerator {

    /**
     * Calcul le temps de simulation
     *
     * @param initialSimulatedTime le temps simulé initial
     * @param elapsedTime          le temps écoulé depuis le début de l'animation
     * @return le temps simulé, sous formce d'une instance de ZonedDateTime
     */

    public abstract ZonedDateTime adjust(ZonedDateTime initialSimulatedTime, long elapsedTime);

    /**
     * Permet d'obtenir un accélérateur continu, selon un facteur d'accélération
     *
     * @param accelerationFactor le facteur d'accélération
     * @return un accélérateur continu
     */

    public static TimeAccelerator continuous(int accelerationFactor) {
        return ((initialSimulatedTime, elapsedTime) ->
                initialSimulatedTime.plus(accelerationFactor * (elapsedTime),
                        ChronoUnit.NANOS));
    }

    /**
     * Permet d'obtenir un accélérateur discret, en fonction de la fréqunce d'avancement et du pas d'accélérateur
     *
     * @param frequence la fréquence d'avancement
     * @param duration  le pas d'accélérateur discret
     * @return un accélérateur discret
     */

    public static TimeAccelerator discrete(long frequence, Duration duration) {
        return ((initialSimulatedTime, elapsedTime) -> {
            Duration multiplicatedDuration = duration.multipliedBy(
                    (long) Math.floor(frequence * elapsedTime / 1_000_000_000));
            return initialSimulatedTime.plus(multiplicatedDuration);
        });
    }
}
