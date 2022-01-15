package ch.epfl.rigel.astronomy;


import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Représente un catalogue d'étoiles et d'aastérismes
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public final class StarCatalogue {

    private final HashMap<Asterism, Integer> asterismsMap = new HashMap<>();
    private final HashMap<Star, Integer> starsMap = new HashMap<>();

    private final List<Star> stars;

    /**
     * Construit un catalogue des étoiles stars et des astérismes asterisms
     * lève IllegalArgumentException si un astérisme contient une étoile
     * qui n'est pas dans la liste stars.
     *
     * @param stars     la liste d'étoiles
     * @param asterisms la liste d'astérismes
     */

    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        this.stars = List.copyOf(stars);

        for (int i = 0; i < asterisms.size(); i++) {
            Preconditions.checkArgument(stars.containsAll(asterisms.get(i).stars()));
            asterismsMap.put(asterisms.get(i),i);
        }

        for (int i = 0; i < stars.size(); i++) {
            starsMap.put(stars.get(i), i);
        }
    }

    /**
     * @return Retourne Une liste des étoiles du catalogue
     */
    public List<Star> stars() {
        return stars;
    }

    /**
     * @return Retorune l'ensemble des astérismes du catalogue
     */
    public Set<Asterism> asterisms() {
        return Collections.unmodifiableSet(asterismsMap.keySet());
    }

    /**
     * Nous permet d'obtenir les index des étoiles qui constitue l'astérisme donné
     *
     * @param asterism l'astérisme
     * @return Retounre la liste des index, dans le catalogue, des étoiles constituant l'astérisme asterism
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        Preconditions.checkArgument((asterisms().contains(asterism)));
        List<Integer> list = new ArrayList<>();
        for (Star star : asterism.stars()) {
            list.add(starsMap.get(star));
        }
        return list;
    }

    /**
     * Représente un chargeur de catalogue d'étoiles et d'astérismes
     */
    public interface Loader {
        void load(InputStream inputStream, Builder builder) throws IOException;
    }

    /**
     * Bâtisseur de StarCatalogue
     */
    public static class Builder {

        private final List<Star> stars;
        private final List<Asterism> asterisms;

        /**
         * Constructeur qui s'assure que le catalogue est vide au départ
         */
        public Builder() {
            stars = new ArrayList<>();
            asterisms = new ArrayList<>();
        }

        /**
         * Ajoute l'étoile star au catalogue en construction
         *
         * @param star l'étoile à ajouter
         * @return le bâtisseur
         */
        public Builder addStar(Star star) {
            stars.add(star);
            return this;
        }

        /**
         * @return Retourne une vue non modifiable sur les étoiles du catalogue en construction
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(stars);
        }

        /**
         * Ajoute l'astérisme donné au catalogue en cours de construction
         *
         * @param asterism l'astérisme à ajouter
         * @return le constructeur
         */
        public Builder addAsterism(Asterism asterism) {
            asterisms.add(asterism);
            return this;
        }

        /**
         * @return Retourne une vue non modifiable sur les astérismes du catalogue
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterisms);
        }

        /**
         * Ajoute au catalogue, via loader, les étoiles et astérismes qu'il obtient depuis le flot d'entrée
         * et retorune le bâtisseur. Lève IOException en cas d'erreur d'entrée/sortie
         *
         * @param inputStream le flot d'entrée
         * @param loader      le Loader
         * @return le bâtisseur
         * @throws IOException Lève IOException en cas d'erreur d'entrée/sortie
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * @return Retourne le catalogue contenant les étoiles et astérismes ajoutés lors de la construction du catalogue
         */
        public StarCatalogue build() {
            return new StarCatalogue(stars, asterisms);
        }

    }

}



