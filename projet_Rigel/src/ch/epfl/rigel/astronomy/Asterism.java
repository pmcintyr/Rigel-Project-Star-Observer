package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

/**
 * Représente une liste d'étoile
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class Asterism {

    private final List<Star> stars;

    /**
     *Construit une liste un astérisme avec la liste d'étoiles qui lui est fournie
     *
     * @param stars
     *          la liste d'étoile fournie
     */

    public Asterism(List<Star> stars){
        this.stars = List.copyOf(stars);
        Preconditions.checkArgument(!(stars.isEmpty()));
    }

    /**
     *
     * @return
     *      retorune une liste non modifiable contenant les étoiles
     */
    public List<Star> stars(){
        return List.copyOf(stars);
    }
}
