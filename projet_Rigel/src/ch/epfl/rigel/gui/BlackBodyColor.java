package ch.epfl.rigel.gui;


import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilisée afin d'obtenir la couleur d'un corps noir , étant donné sa température
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public class BlackBodyColor {


    private final static ClosedInterval CHECKED_TEMPERATURE = ClosedInterval.of(1000, 40000);

    private static boolean isLoaded = false;

    private final static Map<Integer, String> TEMPERATURE_TO_COLOR = new HashMap<>();

    private BlackBodyColor() {
    }

    /**
     * Nous permet d'obtenir la couleur du corps, en focntion de sa température
     *
     * @param KelvinTemperature la température du corps, exprimée en degrés Kelvin
     * @return la couleur correspondant à la température donnée
     */

    public static Color colorForTemperature(int KelvinTemperature) {
        KelvinTemperature = roundTemperature(KelvinTemperature);

        if (!isLoaded) {
            try {

                InputStream stream = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String temperatureString = "";
                String color = "";
                String toRead = "";

                while ((toRead = bufferedReader.readLine()) != null) {

                    if (toRead.charAt(0) != '#') {
                        if (toRead.substring(10, 15).equalsIgnoreCase("10deg")) {
                            if (toRead.charAt(1) == ' ') {
                                temperatureString = toRead.substring(2, 6);
                            } else temperatureString = toRead.substring(1, 6);
                            color = toRead.substring(80, 87);
                            TEMPERATURE_TO_COLOR.put(Integer.parseInt(temperatureString), color);
                        }
                    }
                }

            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            isLoaded = true;
        }
        return Color.web(TEMPERATURE_TO_COLOR.get(KelvinTemperature));
    }

    private static int roundTemperature(int temperature) {
        Preconditions.checkInInterval(CHECKED_TEMPERATURE, temperature);
        if (temperature % 100 != 0) {
            if (temperature % 100 > 50) {
                temperature += 100 - (temperature % 100);
            } else temperature -= temperature % 100;
        }
        return temperature;
    }
}
