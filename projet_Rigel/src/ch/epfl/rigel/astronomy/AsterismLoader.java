package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un chargeur d'astérismes
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */
public enum AsterismLoader implements StarCatalogue.Loader {

    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try (

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        ) {

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                List<Star> stars = new ArrayList<>();
                String[] strings = s.split(",");

                for (int i = 0; i < strings.length; i++) {
                    for (Star star : builder.stars()) {
                        if (star.hipparcosId() == Integer.parseInt(strings[i])) {
                            stars.add(star);
                        }
                    }
                }
                builder.addAsterism(new Asterism(stars));
            }
        }

    }
}