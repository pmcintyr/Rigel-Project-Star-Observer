package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

/**
 * Classe nous permettant de dessiner le ciel sur le canvas
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    private final static ClosedInterval CLOSED_INTERVAL = ClosedInterval.of(-2, 5);

    /**
     * Construit un peintre, devant dessiner sur le canvas fourni
     *
     * @param canvas le canvas sur lequel le peintre dessine
     */

    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Méthode qui efface le canvas
     */

    public void clear() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Dessine les étoiles et les astérismes sur le canvas, selon le ciel observé et la transformation donnée
     *
     * @param observedSky   le ciel observé
     * @param planeToCanvas la transformation permettant de passser du repère du plan dans celui du canvas
     */

    public void drawStars(ObservedSky observedSky, Transform planeToCanvas) {


        ctx.setLineWidth(1);
        ctx.setStroke(Color.BLUE);
        for (Asterism asterism : observedSky.getAsterism()) {
            ctx.beginPath();

            double initX = observedSky.getStarsProjectedPositions()[observedSky.getStarsIndex(asterism).get(0)][0];
            double initY = observedSky.getStarsProjectedPositions()[observedSky.getStarsIndex(asterism).get(0)][1];
            Point2D correctedInitPosition = planeToCanvas.transform(initX, initY);
            ctx.moveTo(correctedInitPosition.getX(), correctedInitPosition.getY());

            for (int i = 1; i < observedSky.getStarsIndex(asterism).size(); ++i) {
                double x = observedSky.getStarsProjectedPositions()[observedSky.getStarsIndex(asterism).get(i)][0];
                double y = observedSky.getStarsProjectedPositions()[observedSky.getStarsIndex(asterism).get(i)][1];
                Point2D correctedPosition = planeToCanvas.transform(x, y);

                double previousX = observedSky.getStarsProjectedPositions()[observedSky.getStarsIndex(asterism).get(i - 1)][0];
                double previousY = observedSky.getStarsProjectedPositions()[observedSky.getStarsIndex(asterism).get(i - 1)][1];
                Point2D previousCorrectedPosition = planeToCanvas.transform(previousX, previousY);


                if (canvas.getBoundsInLocal().contains(correctedPosition) || canvas.getBoundsInLocal().contains(previousCorrectedPosition))
                    ctx.lineTo(correctedPosition.getX(), correctedPosition.getY());

            }
            ctx.stroke();
        }


        int starNumber = 0;

        for (Star star : observedSky.getStarList()) {

            ctx.setFill(BlackBodyColor.colorForTemperature(star.colorTemperature()));

            Point2D centerPosition = planeToCanvas.transform(observedSky.getStarsProjectedPositions()[starNumber][0],
                    observedSky.getStarsProjectedPositions()[starNumber][1]);

            double starDiameter = computeDiameterWithMagnitude(star);
            Point2D deltaTransformDiameter = planeToCanvas.deltaTransform(starDiameter, 0);
            double correctedStarDiameter = deltaTransformDiameter.getX();

            ctx.fillOval(centerPosition.getX(), centerPosition.getY(), correctedStarDiameter, correctedStarDiameter);

            starNumber += 1;
        }
    }

    /**
     * Dessine les planètes, selon le ciel observé et la transformation donnée
     *
     * @param observedSky   le ciel observé
     * @param planeToCanvas la transformation permettant de passer du repère du plan dans celui du canvas
     */
    public void drawPlanets(ObservedSky observedSky, Transform planeToCanvas) {
        ctx.setFill(Color.LIGHTGRAY);

        int planetNumber = 0;

        for (Planet planet : observedSky.getPlanets()) {
            double x = observedSky.getPlanetsProjectedPositions()[planetNumber][0];
            double y = observedSky.getPlanetsProjectedPositions()[planetNumber][1];
            Point2D centerPosition = planeToCanvas.transform(x, y);

            double planetDiameter = computeDiameterWithMagnitude(planet);
            Point2D deltaTransformerDiameter = planeToCanvas.deltaTransform(planetDiameter, 0);
            double correctedPlanetDiameter = deltaTransformerDiameter.getX();


            ctx.fillOval(centerPosition.getX(), centerPosition.getY(), correctedPlanetDiameter, correctedPlanetDiameter);

            planetNumber += 1;
        }
    }

    private double computeDiameterWithMagnitude(CelestialObject celestialObject) {
        double clippedMagnitude = CLOSED_INTERVAL.clip(celestialObject.magnitude());
        double sizeFactor = (99 - 17 * clippedMagnitude) / 140;
        return sizeFactor * 2 * Math.tan(Angle.ofDeg(0.5) / 4);
    }

    /**
     * Dessine le soleil selon le ciel observé et la transformation donnée
     *
     * @param observedSky   le ciel observé
     * @param planeToCanvas la transformation permettant de passer du repère du plan dans celui du canvas
     */
    public void drawSun(ObservedSky observedSky, Transform planeToCanvas) {

        Point2D centerPosition = getSunPosition(observedSky,planeToCanvas);

        double sunDiameter = 2 * Math.tan(Angle.ofDeg(0.5) / 4);
        Point2D deltaTransformerDiameter = planeToCanvas.deltaTransform(sunDiameter, 0);

        Color color = Color.YELLOW.deriveColor(0, 0, 0, 0.25);
        ctx.setFill(color);
        double correctedDiameterBigCircle = deltaTransformerDiameter.getX() * 2.2;
        ctx.fillOval(centerPosition.getX(), centerPosition.getY(), correctedDiameterBigCircle, correctedDiameterBigCircle);

        ctx.setFill(Color.YELLOW);
        double correctedDiameterMediumCircle = deltaTransformerDiameter.getX() + 2;
        ctx.fillOval(centerPosition.getX(), centerPosition.getY(), correctedDiameterMediumCircle, correctedDiameterMediumCircle);

        ctx.setFill(Color.WHITE);
        double correctedDiameterLittleCircle = deltaTransformerDiameter.getX();
        ctx.fillOval(centerPosition.getX(), centerPosition.getY(), correctedDiameterLittleCircle, correctedDiameterLittleCircle);

    }


    private Point2D getSunPosition (ObservedSky observedSky, Transform planeToCanvas){
        double x = observedSky.getSunProjectedPosition().x();
        double y = observedSky.getSunProjectedPosition().y();
        return planeToCanvas.transform(x, y);

    }



    /**
     * Dessine la lune selon le ciel observé, la projection et la transformation données
     *
     * @param observedSky             le ciel observé
     * @param stereographicProjection la projection
     * @param planeToCanvas           la transformation permettant de passer du repère du plan dans celui du canvas
     */
    public void drawMoon(ObservedSky observedSky, StereographicProjection stereographicProjection, Transform planeToCanvas) {
        ctx.setFill(Color.WHITE);

        double x = observedSky.getMoonProjectedPosition().x();
        double y = observedSky.getMoonProjectedPosition().y();
        Point2D centerPosition = planeToCanvas.transform(x, y);

        double moonDiameter = stereographicProjection.applyToAngle(observedSky.getMoon().angularSize());
        Point2D deltaTransformerDiameter = planeToCanvas.deltaTransform(moonDiameter, 0);
        double correctedMoonDiameter = deltaTransformerDiameter.getX();

        ctx.fillOval(centerPosition.getX(), centerPosition.getY(), correctedMoonDiameter, correctedMoonDiameter);
    }

    /**
     * Dessine l'horizon, selon la projection et la transformation données
     *
     * @param stereographicProjection la projection
     * @param planeToCanvas           la transformation permettant de passer du repère du plan dans celui du canvas
     */
    public void drawHorizon(StereographicProjection stereographicProjection, Transform planeToCanvas) {
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.TOP);

        HorizontalCoordinates horizontalCoordinates = HorizontalCoordinates.of(0, 0);

        double diameter = 2 * stereographicProjection.circleRadiusForParallel(horizontalCoordinates);
        Point2D deltaTransformDiameter = planeToCanvas.deltaTransform(diameter, 0);
        double correctedDiameter = deltaTransformDiameter.getX();

        CartesianCoordinates circleCenterForParallel = stereographicProjection.circleCenterForParallel(horizontalCoordinates);
        double x = circleCenterForParallel.x();
        double y = circleCenterForParallel.y();
        Point2D centerPosition = planeToCanvas.transform(x, y);
        ctx.strokeOval(centerPosition.getX() - correctedDiameter / 2, centerPosition.getY() - correctedDiameter / 2,
                correctedDiameter, correctedDiameter);

        for (int i = 0; i < 360; i += 45) {
            ctx.setFill(Color.RED);
            String name = computeName(i);
            Point2D textPosition = computePosition(i, stereographicProjection, planeToCanvas);
            ctx.fillText(name, textPosition.getX(), textPosition.getY());

        }
    }

    private String computeName(double azDeg) {
        HorizontalCoordinates horizontalCoordinates = HorizontalCoordinates.ofDeg(azDeg, -0.5);
        return horizontalCoordinates.azOctantName("N", "E", "S", "O");
    }

    private Point2D computePosition(double azDeg, StereographicProjection stereographicProjection, Transform planeToCanvas) {
        CartesianCoordinates cartesianCoordinates = stereographicProjection.apply(HorizontalCoordinates.ofDeg(azDeg, -0.5));
        double x = cartesianCoordinates.x();
        double y = cartesianCoordinates.y();
        return planeToCanvas.transform(x, y);
    }
}
