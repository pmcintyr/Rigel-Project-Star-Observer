package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Représente un accélérateur de temps nommé, donc un paire (nom, accélérateur)
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public enum NamedTimeAccelerator {
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDERAL_DAY("jour sidéral", TimeAccelerator.discrete(60,
            Duration.ofHours(23).plus(Duration.ofMinutes(56)).plus(Duration.ofSeconds(4))));

    private final String visibleName;
    private final TimeAccelerator timeAccelerator;

    NamedTimeAccelerator(String visibleName, TimeAccelerator timeAccelerator) {
        this.timeAccelerator = timeAccelerator;
        this.visibleName = visibleName;
    }

    /**
     *  @return le nom de l'accélérateur
     */

    public String getName() {
        return visibleName;
    }

    /**
     * @return l'accélérateur
     */

    public TimeAccelerator getAccelerator() {
        return timeAccelerator;
    }

    @Override
    public String toString() {
        return getName();
    }
}
