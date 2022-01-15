package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Représente la classe principale du programme
 *
 * @author Cyril Golaz (301379)
 * @author Paul McIntyre (302264)
 */

public class Main extends Application {

    private final static String RESET = "\uf0e2", PLAY = "\uf04b", BREAK = "\uf04c", SEARCH = "\u2315", CROSS = "\u26D2";

    private final static HashMap<String, HorizontalCoordinates> NAME_AND_POSITION = new HashMap<>();

    private static boolean hasMaped = false;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("Rigel");

        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        DateTimeBean dateTimeBean = new DateTimeBean();
        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
        TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

        dateTimeBean.setZonedDateTime(ZonedDateTime.now());
        dateTimeBean.setDateObject(LocalDate.now());

        observerLocationBean.setCoordinates(
                GeographicCoordinates.ofDeg(6.57, 46.52));

        viewingParametersBean.setCenter(
                HorizontalCoordinates.ofDeg(180.000000000001, 15));

        viewingParametersBean.setFieldOfViewDeg(100);

        SkyCanvasManager skyCanvasManager = sky(observerLocationBean, viewingParametersBean, dateTimeBean);

        Canvas skyCanvas = skyCanvasManager.canvas();
        StackPane skyStackPane = new StackPane(skyCanvas);

        skyCanvas.widthProperty().bind(skyStackPane.widthProperty());
        skyCanvas.heightProperty().bind(skyStackPane.heightProperty());


        BorderPane root = new BorderPane();

        computeInfo(skyCanvasManager.getSky().getPlanets(),
                skyCanvasManager.getSky().getSun(),
                skyCanvasManager.getSky().getMoon(),
                skyStackPane,
                skyCanvasManager);

        root.setTop(controlBar(observerLocationBean, dateTimeBean, timeAnimator, viewingParametersBean, skyCanvasManager, skyStackPane));
        root.setCenter(skyStackPane);
        root.setBottom(informationBar(skyCanvasManager, viewingParametersBean));

        primaryStage.setScene(new Scene(root));


        addedInfos(root, skyCanvasManager);

        primaryStage.show();

        skyCanvas.requestFocus();


    }

    private HBox controlBar(ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean,
                            TimeAnimator timeAnimator, ViewingParametersBean viewingParametersBean, SkyCanvasManager canvasManager, StackPane stackPane) {
        HBox parentHBox = new HBox();

        Separator separator1 = new Separator(Orientation.VERTICAL);

        Separator separator2 = new Separator(Orientation.VERTICAL);

        parentHBox.getChildren().add(observationPosition(observerLocationBean));
        parentHBox.getChildren().add(observationMoment(dateTimeBean, timeAnimator));
        parentHBox.getChildren().add(timeFlowing(timeAnimator, dateTimeBean, viewingParametersBean, observerLocationBean, canvasManager, stackPane));
        parentHBox.getChildren().add(1, separator1);
        parentHBox.getChildren().add(3, separator2);

        parentHBox.setStyle("-fx-spacing: 4; -fx-padding: 4;");


        return parentHBox;
    }

    private HBox observationPosition(ObserverLocationBean observerLocationBean) {
        HBox HBoxObservationPosition = new HBox();
        HBoxObservationPosition.setStyle("-fx-spacing: inherit; -fx-alignement: baseline-left;");

        Label lon = new Label("Longitude (°)");
        Label lat = new Label("Latitude (°)");

        TextField lonTextField = new TextField();
        TextFormatter<Number> lonTextFormatter = computeTextField("Longitude");
        lonTextFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegPositionProperty());
        lonTextField.setTextFormatter(lonTextFormatter);
        lonTextField.setStyle("-fx-pref-width: 60; -fx-alignement: baseline-rigth;");


        TextField latTextField = new TextField();
        TextFormatter<Number> latTextFormatter = computeTextField("Latitude");
        latTextFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegPositionProperty());
        latTextField.setTextFormatter(latTextFormatter);
        latTextField.setStyle("-fx-pref-width: 60; -fx-alignement: baseline-rigth;");


        HBoxObservationPosition.getChildren().add(lon);
        HBoxObservationPosition.getChildren().add(lonTextField);
        HBoxObservationPosition.getChildren().add(lat);
        HBoxObservationPosition.getChildren().add(latTextField);


        return HBoxObservationPosition;

    }

    private TextFormatter<Number> computeTextField(String string) {
        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> valueFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newDeg =
                        stringConverter.fromString(newText).doubleValue();
                if (string.equals("Latitude")) {
                    return GeographicCoordinates.isValidLatDeg(newDeg)
                            ? change
                            : null;
                }
                return GeographicCoordinates.isValidLonDeg(newDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(stringConverter, 0, valueFilter);
    }

    private HBox observationMoment(DateTimeBean dateTimeBean, TimeAnimator timeAnimator) {
        HBox HBoxObservationMoment = new HBox();
        HBoxObservationMoment.setStyle("-fx-spacing: inherit; -fx-alignement: baseline-left;");

        Label date = new Label("Date: ");
        Label hour = new Label("Heure: ");

        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateObjectProperty());


        TextField timeTextField = new TextField();
        TextFormatter<LocalTime> timeTextFormatter = computeTimeFormatter();

        timeTextField.setStyle("-fx-pref-width: 75; -fx-alignement: baseline-right;");
        timeTextField.setTextFormatter(timeTextFormatter);

        timeTextFormatter.valueProperty().bindBidirectional(dateTimeBean.timeObjectProperty());


        List<String> zoneIdStrings = new ArrayList<>(ZoneId.getAvailableZoneIds());
        Collections.sort(zoneIdStrings);

        List<ZoneId> zoneIds = new ArrayList<>();
        for (String s : zoneIdStrings) {
            zoneIds.add(ZoneId.of(s));
        }

        ComboBox<ZoneId> timeZoneBox = new ComboBox<>(FXCollections.observableList(zoneIds));
        timeZoneBox.setStyle("-fx-pref-width: 180;");


        timeZoneBox.valueProperty().bindBidirectional(dateTimeBean.idObjectProperty());

        HBoxObservationMoment.getChildren().add(date);
        HBoxObservationMoment.getChildren().add(datePicker);
        HBoxObservationMoment.getChildren().add(hour);
        HBoxObservationMoment.getChildren().add(timeTextField);
        HBoxObservationMoment.getChildren().add(timeZoneBox);


        for (Node node : HBoxObservationMoment.getChildren()) {
            node.disableProperty().bind(timeAnimator.runningProperty());
        }

        return HBoxObservationMoment;

    }

    private TextFormatter<LocalTime> computeTimeFormatter() {
        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);

        return new TextFormatter<>(stringConverter);
    }

    private HBox timeFlowing(TimeAnimator timeAnimator, DateTimeBean dateTimeBean,
                             ViewingParametersBean viewingParametersBean, ObserverLocationBean observerLocationBean,
                             SkyCanvasManager canvasManager, StackPane stackPane) {
        HBox HBoxTimeFlow = new HBox();
        HBoxTimeFlow.setStyle("-fx-spacing: inherit;");

        ChoiceBox<NamedTimeAccelerator> acceleratorChoice = new ChoiceBox<>();
        HBoxTimeFlow.getChildren().add(acceleratorChoice);

        List<NamedTimeAccelerator> namedTimeAccelerators = new ArrayList<>();
        namedTimeAccelerators.add(NamedTimeAccelerator.TIMES_1);
        namedTimeAccelerators.add(NamedTimeAccelerator.TIMES_30);
        namedTimeAccelerators.add(NamedTimeAccelerator.TIMES_300);
        namedTimeAccelerators.add(NamedTimeAccelerator.TIMES_3000);
        namedTimeAccelerators.add(NamedTimeAccelerator.DAY);
        namedTimeAccelerators.add(NamedTimeAccelerator.SIDERAL_DAY);

        acceleratorChoice.setItems(FXCollections.observableList(namedTimeAccelerators));

        acceleratorChoice.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.acceleratorProperty().bind(Bindings.select(acceleratorChoice.valueProperty(),
                "accelerator"));


        try (InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {

            Font fontAwesome = Font.loadFont(fontStream, 15);

            Button resetButton = new Button(RESET);
            resetButton.setFont(fontAwesome);
            HBoxTimeFlow.getChildren().add(resetButton);
            resetButton.setOnMousePressed(e -> {

                dateTimeBean.setZonedDateTime(ZonedDateTime.now());
                dateTimeBean.setDateObject(LocalDate.now());

                observerLocationBean.setCoordinates(
                        GeographicCoordinates.ofDeg(6.57, 46.52));

                viewingParametersBean.setCenter(
                        HorizontalCoordinates.ofDeg(180.000000000001, 15));

                viewingParametersBean.setFieldOfViewDeg(100.0);

            });
            resetButton.disableProperty().bind(timeAnimator.runningProperty());

            Tooltip tooltipReset = new Tooltip();
            tooltipReset.setText("Reset");
            resetButton.setTooltip(tooltipReset);
            tooltipReset.setShowDelay(Duration.ZERO);
            tooltipReset.setShowDuration(Duration.seconds(3));


            Button button = new Button();
            button.setFont(fontAwesome);
            button.setOnMousePressed(e -> {
                if (timeAnimator.runningProperty().get()) {
                    timeAnimator.stop();
                } else timeAnimator.start();
            });

            button.textProperty().bind(Bindings.when(timeAnimator.runningProperty()).then(BREAK).otherwise(PLAY));
            HBoxTimeFlow.getChildren().add(button);

            Tooltip tooltipPlayOrBreak = new Tooltip();
            tooltipPlayOrBreak.textProperty().bind(Bindings.when(timeAnimator.runningProperty()).
                    then("Pause").
                    otherwise("Play"));
            tooltipPlayOrBreak.setShowDuration(Duration.seconds(3));
            tooltipPlayOrBreak.setShowDelay(Duration.seconds(0.5));
            button.setTooltip(tooltipPlayOrBreak);

            Button searchButton = new Button(SEARCH);
            HBoxTimeFlow.getChildren().add(searchButton);

            Tooltip searchTip = new Tooltip();
            searchTip.setText("Trouvez un objet céleste !");
            searchTip.setShowDelay(Duration.seconds(0.5));
            searchTip.setShowDuration(Duration.seconds(3));
            searchButton.setTooltip(searchTip);

            Button close = new Button(CROSS);
            Tooltip closeTip = new Tooltip("Fermer la recherche");
            closeTip.setShowDuration(Duration.seconds(3));
            closeTip.setShowDelay(Duration.seconds(0.5));
            close.setTooltip(closeTip);

            TextField textField = new TextField();
            HBox hBox = new HBox(textField);
            hBox.getChildren().add(close);


            searchButton.setOnMousePressed(e -> {
                if (!stackPane.getChildren().contains(hBox))
                    stackPane.getChildren().add(hBox);

                hBox.setAlignment(Pos.TOP_RIGHT);

            });

            hBox.setOnKeyPressed(k -> {
                if (k.getCode() == KeyCode.ENTER) {
                    computeSearch(textField, canvasManager, viewingParametersBean, observerLocationBean, dateTimeBean, stackPane, hBox);
                    timeAnimator.stop();
                    k.consume();
                }
            });

            close.setOnMousePressed(e -> stackPane.getChildren().remove(hBox));


        } catch (IOException e) {

        }
        return HBoxTimeFlow;
    }


    private SkyCanvasManager sky(ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean,
                                 DateTimeBean dateTimeBean) throws IOException {

        try (InputStream starStream = resourceStream("/hygdata_v3.csv");
             InputStream asterismStream = resourceStream("/asterisms.txt")) {

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(starStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();


            return new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

        }
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    private BorderPane informationBar(SkyCanvasManager canvasManager, ViewingParametersBean viewingParametersBean) {
        BorderPane infoPane = new BorderPane();
        infoPane.setStyle("-fx-padding: 4; -fx-background-color: white;");

        Text leftText = new Text();
        Text centerText = new Text();
        Text rightText = new Text();

        infoPane.setCenter(centerText);
        infoPane.setLeft(leftText);
        infoPane.setRight(rightText);


        canvasManager.objectUnderMouseProperty().addListener(e -> {

            try {
                if (canvasManager.objectUnderMouseProperty().get().isPresent()) {
                    centerText.setText(canvasManager.objectUnderMouseProperty().get().get().info());

                }

            } catch (NullPointerException e1) {
            }


        });

        leftText.textProperty().bind(Bindings.format("Champ de vue: %.1f°", viewingParametersBean.fieldOfViewDegProperty()));

        rightText.textProperty().bind(Bindings.format("Azimut: %.2f°, Hauteur: %.2f°",
                canvasManager.mouseAzDeg, canvasManager.mouseAltDeg));

        return infoPane;

    }

    private void computeInfo(List<Planet> planets, Sun sun, Moon moon,
                             StackPane stackPane, SkyCanvasManager canvasManager) throws IOException {

        HashMap<String, Tooltip> objectTip = new HashMap<>();
        HashMap<CelestialObject, ImageView> objectsAndImages = new HashMap<>();

        for (Planet planet : planets) {

            try (InputStream objectStream = resourceStream("/" + planet.name() + ".jpg")) {
                Image objectImage = new Image(objectStream);
                ImageView objectImageView = new ImageView(objectImage);
                objectsAndImages.put(planet, objectImageView);
            }
        }

        try (InputStream sunStream = resourceStream("/Soleil.jpg")) {
            Image sunImage = new Image(sunStream);
            ImageView sunImageView = new ImageView(sunImage);
            objectsAndImages.put(sun, sunImageView);
        }

        try (InputStream moonStream = resourceStream("/Lune.jpg")) {
            Image moonImage = new Image(moonStream);
            ImageView moonImageView = new ImageView(moonImage);
            objectsAndImages.put(moon, moonImageView);
        }


        for (CelestialObject object : objectsAndImages.keySet()) {
            objectTip.put(object.name(), computeTip(objectsAndImages.get(object), object));
        }

        canvasManager.objectUnderMouse.addListener(e -> {
            if (canvasManager.objectUnderMouse.get().isPresent()) {
                if (objectTip.containsKey((canvasManager.objectUnderMouse.get().get().name()))) {
                    Tooltip.install(stackPane, objectTip.get(canvasManager.objectUnderMouse.get().get().name()));
                } else Tooltip.uninstall(stackPane, objectTip.get(canvasManager.objectUnderMouse.get().get().name()));
            }
        });

    }

    private Tooltip computeTip(ImageView imageView, CelestialObject celestialObject) {
        imageView.setPreserveRatio(true);
        Tooltip objectTip = new Tooltip();
        objectTip.setGraphic(imageView);
        objectTip.setShowDelay(Duration.seconds(0.5));
        objectTip.setShowDuration(Duration.seconds(7));

        switch (celestialObject.name()) {
            case "Mercure":
                objectTip.setText("Planète la plus proche du soleil," +
                        "\net la moins massive du système solaire !");
                break;

            case "Vénus":
                objectTip.setText("La planète Vénus a été baptisée" +
                        "\n du nom de la déesse Vénus " +
                        "\nde la mythologie romaine !");
                break;

            case "Mars":
                objectTip.setText("La période de rotation de Mars " +
                        "\nest du même ordre que celle de la Terre " +
                        "\net son obliquité lui confère un cycle des " +
                        "\nsaisons similaire à celui nous connaissons");
                break;

            case "Jupiter":
                objectTip.setText("Jupiter est la cinquième et la " +
                        "\nplus grosse planète du système solaire");
                break;

            case "Saturne":
                objectTip.setText("D'un diamètre d'environ neuf fois " +
                        "\net demi celui de la Terre, elle est " +
                        "\nmajoritairement composée d'hydrogène et d'hélium");
                break;

            case "Uranus":
                objectTip.setText("Uranus est la première planète" +
                        "\n découverte à l’époque moderne");
                break;

            case "Neptune":
                objectTip.setText("Neptune est le premier objet " +
                        "\net la seule des huit planètes du système solaire" +
                        "\n à avoir été découverte par déduction grâce au calcul " +
                        "\navant l'observation directe");
                break;

            case "Soleil":
                objectTip.setText("Etoile de type naine jaune");
                break;

            case "Lune":
                objectTip.setText("C'est le cinquième plus grand satellite naturel" +
                        "\n du Système solaire et le plus grand des satellites planétaires" +
                        "\n par rapport à la taille de la planète autour de laquelle elle orbite");
                break;
        }
        return objectTip;
    }

    private void computeSearch(TextField textField, SkyCanvasManager canvasManager,
                               ViewingParametersBean viewingParametersBean, ObserverLocationBean observerLocationBean,
                               DateTimeBean dateTimeBean, StackPane stackPane, HBox hBox) {

        if (!hasMaped) {

            EquatorialToHorizontalConversion conversion = new EquatorialToHorizontalConversion(dateTimeBean.getZonedDateTime(),
                    observerLocationBean.getCoordinates());

            for (Planet planet : canvasManager.getSky().getPlanets()) {
                NAME_AND_POSITION.put(planet.name(), conversion.apply(planet.equatorialPos()));
            }
            for (Star star : canvasManager.getSky().getStarList()) {
                NAME_AND_POSITION.put(star.name(), conversion.apply(star.equatorialPos()));
            }
            NAME_AND_POSITION.put("Lune", conversion.apply(canvasManager.getSky().getMoon().equatorialPos()));
            NAME_AND_POSITION.put("Soleil", conversion.apply(canvasManager.getSky().getSun().equatorialPos()));
            hasMaped = true;
        }


        if (NAME_AND_POSITION.containsKey(textField.getText())) {
            viewingParametersBean.setCenter(NAME_AND_POSITION.get(textField.getText()));
            viewingParametersBean.setFieldOfViewDeg(60);
            textField.clear();
            stackPane.getChildren().remove(hBox);
        } else {
            textField.setText("Objet non répertorié");
        }

    }

    private void addedInfos(BorderPane borderPane, SkyCanvasManager canvasManager) {
        BorderPane infosPane = new BorderPane();

        Text topText = new Text();
        Text centerText = new Text();

        infosPane.setTop(topText);
        infosPane.setCenter(centerText);
        infosPane.setMinWidth(150);

        canvasManager.objectUnderMouseProperty().addListener(e ->{
            if (canvasManager.objectUnderMouse.get().isPresent()){
                topText.textProperty().bind(Bindings.format("Vitesse radial : %.2f km/s", canvasManager.objectUnderMouse.get().get().rv()));
                centerText.textProperty().bind(Bindings.format("Distance : %.2f parsec", canvasManager.objectUnderMouse.get().get().distance()));
            }
        });

        borderPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.I) {
                borderPane.setLeft(infosPane);
            }


            if (e.getCode() == KeyCode.O)
                borderPane.getChildren().remove(infosPane);
        });

    }

}
