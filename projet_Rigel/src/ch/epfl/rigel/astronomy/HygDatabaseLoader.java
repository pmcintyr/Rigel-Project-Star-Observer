package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Représente un chargeur de catalogue HYG
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public enum HygDatabaseLoader implements StarCatalogue.Loader {

    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        int hipIndice = 2, properIndice = 7, magIndice = 14, ciIndice = 17, raradIndice = 24, decradIndice = 25,
                bayerIndice = 28, conIndice = 30, distIndice = 10, rvIndice = 13;

        int hip;
        double magnitude, ci, raRad, decRad, distance, rv ;

        String name;

        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))
        ) {

            String toRead = bufferedReader.readLine();

            while ((toRead = bufferedReader.readLine()) != null) {



                String[] strings = toRead.split(",");

                hip = Integer.parseInt(computeString(strings[hipIndice - 1], "0"));


                name = computeString(strings[properIndice - 1], strings[bayerIndice - 1], strings[conIndice - 1]);


                magnitude = Double.parseDouble(computeString(strings[magIndice - 1], "0"));

                ci = Double.parseDouble(computeString(strings[ciIndice - 1],"0"));

                raRad = Angle.normalizePositive(Double.parseDouble(strings[raradIndice - 1]));
                decRad = Double.parseDouble(strings[decradIndice - 1]);

                distance = Double.parseDouble(computeString(strings[distIndice - 1], "0"));
                rv = Double.parseDouble(computeString(strings[rvIndice - 1],"0"));

                EquatorialCoordinates equatorialCoordinates = EquatorialCoordinates.of(raRad, decRad);
                Star star = new Star(hip, name, equatorialCoordinates, (float) magnitude, (float) ci, (float) distance,(float) rv);

                builder.addStar(star);
            }
        }

    }

    private String computeString(String s1, String defaultString) {
        if (!s1.isBlank()) {
            return s1;
        }
        return defaultString;
    }

    private String computeString(String s1, String s2, String s3) {
        if (!s1.isBlank()) {
            return s1;
        } else if (!s2.isBlank()) {
            return s2 + " " + s3;
        }
        return "?" + " " + s3;
    }
}
