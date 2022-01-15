package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * Enumération qui représente les époques astronomiques J2000 et J2010
 */

public enum Epoch {

      J2000 (ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY,1),
            LocalTime.of(12,0),
            ZoneOffset.UTC)),
     J2010 (ZonedDateTime.of(LocalDate.of(2010,Month.JANUARY,1).minusDays(1),
            LocalTime.of(0,0),
            ZoneOffset.UTC));

    private final ZonedDateTime dateTime;
    private final static double MILLISECONDS_IN_A_DAY = 86400000 ;
    private final static int JULIAN_CENTURY = 36525;

    private Epoch(ZonedDateTime dateTime){
        this.dateTime = dateTime;
    }

    /**
     * Retourne le nombre de jours entre l'époque this et le moment when. (Peut retourner un double ! )
     * @param when
     *          le moment when, on veut calculer le nombre de jours entre celui ci et l'instant this
     * @return
     *          le nombre de jours entre when et this
     */

    public double daysUntil(ZonedDateTime when){
        return this.dateTime.until(when, ChronoUnit.MILLIS) / MILLISECONDS_IN_A_DAY;
    }

    /**
     * @param when
     *          le moment en question
     * @return
     *     le nombre de siècles juliens entre l'époque à laquelle on l'applique et le moment when
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        return daysUntil(when)/JULIAN_CENTURY;
    }
}
