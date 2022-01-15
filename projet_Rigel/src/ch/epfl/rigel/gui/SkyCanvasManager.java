package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import javafx.geometry.Point2D;

import java.util.Optional;

/**
 * Représente le gestionnaire du canvas sur lequel le ciel est dessiné
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public class SkyCanvasManager {

    private final static ClosedInterval FIELD_OF_VIEW = ClosedInterval.of(30, 150);
    private final static RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval ALT_INTERVAL = ClosedInterval.of(5, 90);

    private ObservableObjectValue<StereographicProjection> projection;
    private ObservableObjectValue<Transform> planeToCanvas;
    private ObservableObjectValue<ObservedSky> observedSky;
    private ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;

    /**
     * Propriétés (liens) de l'azimut et de l'altitude de la souris , en degrés
     */
    public ObservableObjectValue<Double> mouseAzDeg, mouseAltDeg;

    /**
     * Propriété (lien) pour l'objet céleste le plus proche du curseur
     */
    public ObservableObjectValue<Optional<CelestialObject>> objectUnderMouse;

    private ObjectProperty<Point2D> mousePositon;

    private final Canvas canvas;
    private final SkyCanvasPainter skyCanvasPainter;

    /**
     * Construit le gestionnaire
     *
     * @param starCatalogue le catalogue d'étoiles et d'astérismes
     * @param dateTimeBean le bean contenant la date d'observation
     * @param observerLocationBean le bean contenant la position d'observation
     * @param viewingParametersBean le bean contenant les paramètres qui déterminient quelle portion du ciel est visible
     */

    public SkyCanvasManager(StarCatalogue starCatalogue, DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

        canvas = new Canvas();
        skyCanvasPainter = new SkyCanvasPainter(canvas);

        /**
         * création des liens, ajout des dépendances et des listener.
         */

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()), viewingParametersBean.centerProperty()
        );

        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(),
                        projection.getValue(), starCatalogue),
                dateTimeBean.idObjectProperty(),
                dateTimeBean.timeObjectProperty(),
                dateTimeBean.dateObjectProperty(),
                observerLocationBean.coordinatesProperty(),
                projection
        );
        observedSky.addListener(l -> {
            skyCanvasPainter.clear();
            skyCanvasPainter.drawStars(observedSky.get(),  planeToCanvas.get());
            skyCanvasPainter.drawPlanets(observedSky.get(),  planeToCanvas.get());
            skyCanvasPainter.drawSun(observedSky.get(),  planeToCanvas.get());
            skyCanvasPainter.drawMoon(observedSky.get(), projection.get(), planeToCanvas.get());
            skyCanvasPainter.drawHorizon(projection.get(), planeToCanvas.get());
        });

        planeToCanvas = Bindings.createObjectBinding(
                () -> {
                    double scaleFactor = canvas.widthProperty().get() / (projection.getValue().applyToAngle(Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg())));
                    return Transform.translate(canvas.widthProperty().get() / 2, canvas.heightProperty().get() / 2).createConcatenation(
                            Transform.scale(scaleFactor, -scaleFactor)
                    );
                },
                canvas.heightProperty(),
                canvas.widthProperty(),
                projection,
                viewingParametersBean.fieldOfViewDegProperty()
        );
        planeToCanvas.addListener((p) -> {
            skyCanvasPainter.clear();
            skyCanvasPainter.drawStars(observedSky.get(),  planeToCanvas.get());
            skyCanvasPainter.drawPlanets(observedSky.get(), planeToCanvas.get());
            skyCanvasPainter.drawSun(observedSky.get(),  planeToCanvas.get());
            skyCanvasPainter.drawMoon(observedSky.get(), projection.get(), planeToCanvas.get());
            skyCanvasPainter.drawHorizon( projection.get(), planeToCanvas.get());
        });

        /**
         * Intéraction clavier
         */

        canvas.setOnKeyPressed(k -> {
            double updatedAzDeg, updatedAltDeg;
            double altDeg = viewingParametersBean.getCenter().altDeg(), azDeg = viewingParametersBean.getCenter().azDeg();
            HorizontalCoordinates updatedCoordinates;

            switch (k.getCode()) {
                case RIGHT:
                    updatedAzDeg = azDeg + 10;
                    updatedCoordinates = HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce(updatedAzDeg), ALT_INTERVAL.clip(altDeg));
                    viewingParametersBean.setCenter(updatedCoordinates);
                    k.consume();
                    break;
                case LEFT:
                    updatedAzDeg = azDeg - 10;
                    updatedCoordinates = HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce(updatedAzDeg), ALT_INTERVAL.clip(altDeg));
                    viewingParametersBean.setCenter(updatedCoordinates);
                    k.consume();
                    break;
                case DOWN:
                    updatedAltDeg = altDeg - 5;
                    updatedCoordinates = HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce(azDeg), ALT_INTERVAL.clip(updatedAltDeg));
                    viewingParametersBean.setCenter(updatedCoordinates);
                    k.consume();
                    break;
                case UP:
                    updatedAltDeg = altDeg + 5;
                    updatedCoordinates = HorizontalCoordinates.ofDeg(AZ_INTERVAL.reduce(azDeg), ALT_INTERVAL.clip(updatedAltDeg));
                    viewingParametersBean.setCenter(updatedCoordinates);
                    k.consume();
                    break;
            }

        });

        /**
         * Tout ce qui est en rapport avec la souris
         */

        mousePositon = new SimpleObjectProperty<>(Point2D.ZERO);
        canvas.setOnMouseMoved(e -> mousePositon.set(new Point2D(e.getX(), e.getY())));

        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> {
                    try {
                        var position = planeToCanvas.getValue().inverseTransform(mousePositon.get());
                        return projection.getValue().inverseApply(CartesianCoordinates.of(position.getX(), position.getY()));
                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                },
                projection,
                mousePositon,
                planeToCanvas
        );

        mouseAzDeg = Bindings.createObjectBinding(
                () -> {
                    try {
                        return mouseHorizontalPosition.get().azDeg();
                    } catch (NullPointerException e) {
                        return Double.NaN;
                    }
                },
                mouseHorizontalPosition
        );
        mouseAltDeg = Bindings.createObjectBinding(
                () -> {
                    try {
                        return mouseHorizontalPosition.get().altDeg();
                    } catch (NullPointerException e) {
                        return Double.NaN;
                    }
                },
                mouseHorizontalPosition
        );

        objectUnderMouse = Bindings.createObjectBinding(
                () -> {

                    try {
                        Point2D point2D = planeToCanvas.get().inverseTransform(mousePositon.get().getX(), mousePositon.get().getY());
                        Point2D transformMaxDist = planeToCanvas.get().inverseDeltaTransform(10, 0);
                        return observedSky.get().objectClosestTo(CartesianCoordinates.of(
                                point2D.getX(),
                                point2D.getY()),
                                transformMaxDist.getX());
                    } catch (NonInvertibleTransformException e) {
                        return Optional.empty();
                    }
                },
                observedSky,
                mousePositon,
                planeToCanvas
        );

        canvas.setOnMousePressed(m -> {
            if (m.isPrimaryButtonDown()) {
                canvas.requestFocus();
            }
        });

        canvas.setOnScroll(s -> {
            double fieldOfViewChange = 0;
            if (Math.abs(s.getDeltaX()) > Math.abs(s.getDeltaY())) {
                fieldOfViewChange = s.getDeltaX();
            } else fieldOfViewChange = s.getDeltaY();
            viewingParametersBean.setFieldOfViewDeg(FIELD_OF_VIEW.clip(viewingParametersBean.getFieldOfViewDeg() + fieldOfViewChange));
            s.consume();
        });

    }

    /**
     *
     * @return la propriété de l'objet le plus proche du curseur
     */
    public ObservableObjectValue<Optional<CelestialObject>> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     *
     * @return le canvas sur lequel on dessine
     */

    public Canvas canvas() {
        return canvas;
    }

    /**
     *
     * @return le ciel observé
     */

    public ObservedSky getSky (){
        return observedSky.get();
    }

}
