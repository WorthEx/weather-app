package com.example.weatherapp;

import com.example.weatherapp.dataClasses.CurrentWeather;
import com.example.weatherapp.dataClasses.DailyWeather;
import com.example.weatherapp.dataClasses.HourlyWeather;
import com.example.weatherapp.dataClasses.MoonhaseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class AppController implements Initializable {
    protected double xOffset = 0;
    protected double yOffset = 0;
    protected double fontSizeSmall = 15;
    protected double fontSizeNormal = 23;
    protected double fontSizeHuge = 55;
    protected double fontSizeFullScreen = 85;
    protected boolean setToKeepInTray = true;
    protected Location currentLocation;
    public Rectangle clipRect = new Rectangle();
    private Tooltip CApproximateTemp;
    private boolean isPressureInhPa;
    protected boolean isExpanded = false;
    private int selectedDay = 0;

    // @FXML
    // Main Pane
    @FXML
    protected ImageView clouds;
    @FXML
    protected AnchorPane MainAnchorPane;

    // Title bar things
    @FXML
    protected HBox TitleBar;
    @FXML
    protected ImageView appIcon;
    @FXML
    protected Button ExitButton;
    @FXML
    protected Button MinimizeButton;
    @FXML
    protected Button FullScreenButton;
    @FXML
    protected HBox TitleBarButtons;
    @FXML
    protected Region upperRegion1;

    // Search bar (a part of title bar)
    @FXML
    protected AnchorPane searchBarAnchorPane;
    @FXML
    protected TextField searchBarTextField;
    @FXML
    protected Button searchButton;

    // Current Box
    @FXML
    protected HBox CurrentDailyHBox;
    @FXML
    protected HBox ForecastHBox;
    @FXML
    protected Label currentLocationLabel;
    @FXML
    protected ImageView currentIcon;
    @FXML
    protected Label currentStateLabel;

    @FXML
    protected Label currentTempLabel;
    @FXML
    protected GridPane currentUpperGridPane;
    @FXML
    protected GridPane currentLowerGridPane;
    @FXML
    private RowConstraints currentUpperGridPaneRow0;
    @FXML
    private GridPane currentGridPane;
    @FXML
    private VBox addVBox;
    @FXML
    private StackPane currentIconStackPane;
    @FXML
    private Label currentHumidityLabel;
    @FXML
    private Label currentPressureLabel;
    @FXML
    private Label currentCloudCoverLabel;
    @FXML
    private Label currentWindSpeedLabel;
    @FXML
    private ImageView UVIcon;
    @FXML
    private Label UVLabel;
    @FXML
    private VBox UVVBox;
    @FXML
    private GridPane dailyLowerGridPane;
    @FXML
    private GridPane dailyUpperGridPane;
    @FXML
    private Label dateLabel;
    @FXML
    private ImageView moonIcon;
    @FXML
    private Label moonLabel;
    @FXML
    private VBox moonVBox;
    @FXML
    private VBox currentPressureVBox;
    @FXML
    private GridPane dailyGridPane;
    @FXML
    private Label dayPartLabel1;
    @FXML
    private Label dayPartLabel2;
    @FXML
    private Label dayPartLabel3;
    @FXML
    private Label dayPartLabel4;
    @FXML
    private HBox dayPartHBox1;
    @FXML
    private HBox dayPartHBox2;
    @FXML
    private HBox dayPartHBox3;
    @FXML
    private HBox dayPartHBox4;
    @FXML
    private GridPane dailyMiddleGridPane;
    @FXML
    private GridPane dailyLowerGridPane2;
    @FXML
    private ToggleButton implicitExitToggle;
    @FXML
    private Label maxTempLabel;
    @FXML
    private Label minTempLabel;
    @FXML
    private Label sunriseLabel;
    @FXML
    private Label sunsetLabel;
    @FXML
    private ImageView maxWindSpeedImage;
    @FXML
    private Label maxWindSpeedLabel;
    @FXML
    private Label precipitationProbabilityLabel;
    @FXML
    private Label morningTempLabel;
    @FXML
    private Label morningWindSpeedLabel;
    @FXML
    private Label middayTempLabel;
    @FXML
    private Label middayWindSpeedLabel;
    @FXML
    private Label eveningTempLabel;
    @FXML
    private Label eveningWindSpeedLabel;
    @FXML
    private Label nighttimeTempLabel;
    @FXML
    private Label nighttimeWindSpeedLabel;
    @FXML
    private ImageView morningWeatherIcon;
    @FXML
    private ImageView middayWeatherIcon;
    @FXML
    private ImageView eveningWeatherIcon;
    @FXML
    private ImageView nighttimeWeatherIcon;
    @FXML
    private VBox day0;
    @FXML
    private ImageView day0Icon;
    @FXML
    private ImageView day1Icon;
    @FXML
    private ImageView day2Icon;
    @FXML
    private ImageView day3Icon;
    @FXML
    private ImageView day4Icon;
    @FXML
    private ImageView day5Icon;
    @FXML
    private VBox day6;
    @FXML
    private ImageView day6Icon;

    // Fonts
    Font SFProLightUnderNormal = Font.loadFont(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/fonts/SF-Pro-Text/SFProText-Light.ttf")), fontSizeNormal - 5);
    Font SFProRegularSmall = Font.loadFont(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/fonts/SF-Pro-Text/SFProText-Regular.ttf")), fontSizeSmall);
    Font SFProRegularUnderNormal = Font.loadFont(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/fonts/SF-Pro-Text/SFProText-Regular.ttf")), fontSizeNormal - 5);
    Font SFProBoldUnderNormal = Font.loadFont(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/fonts/SF-Pro-Text/SFProText-Bold.ttf")), fontSizeNormal - 5);
    Font SFProHeavyHuge = Font.loadFont(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/fonts/SF-Pro-Text/SFProText-Heavy.ttf")), fontSizeHuge);

    // Initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainAnchorPane.requestFocus();
        Platform.runLater(() -> MainAnchorPane.requestFocus());
        System.setProperty("prism.lcdtext", "false");

        // Clipping a background image
        clipRect.setWidth(WeatherApp.WINDOW_WIDTH);
        clipRect.setHeight(WeatherApp.WINDOW_HEIGHT);
        clipRect.setArcHeight(20);
        clipRect.setArcWidth(20);
        clipRect.setTranslateY(200);

        clouds.setTranslateY(200);
        clouds.setFitWidth(WeatherApp.WINDOW_WIDTH);

        clouds.setClip(clipRect);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = clouds.snapshot(parameters, null);
        clouds.setClip(null);
        clouds.setImage(image);

        currentLocationLabel.setPrefWidth(currentGridPane.getWidth() / 2 - 50);
        currentStateLabel.setWrapText(true);

        //Setting events
        for (Node node : TitleBarButtons.getChildren()) {
            if (node instanceof Button) {
                node.setOnMouseEntered(event -> inTransition(node));
                node.setOnMouseExited(event -> outTransition(node));
            }
        }

        currentPressureVBox.setOnMouseEntered(event -> upTransition(currentPressureVBox));
        currentPressureVBox.setOnMouseExited(event -> downTransition(currentPressureVBox));
        implicitExitToggle.setFocusTraversable(false);
        implicitExitToggle.setOnMouseEntered(event -> {
            if (implicitExitToggle.getOpacity() == 1 && setToKeepInTray) {
                inTransition(implicitExitToggle);
            }
        });
        implicitExitToggle.setOnMouseExited(event -> {
            if (implicitExitToggle.getOpacity() != 1 && setToKeepInTray) {
                outTransition(implicitExitToggle);
            }
        });
        implicitExitToggle.setOnAction(e -> setToKeepInTray = !setToKeepInTray);
        //
        dayPartHBox1.setOnMouseEntered(e -> {
            dayPartHBox2.setOpacity(.2);
            dayPartLabel2.setOpacity(.2);
            dayPartHBox3.setOpacity(.2);
            dayPartLabel3.setOpacity(.2);
            dayPartHBox4.setOpacity(.2);
            dayPartLabel4.setOpacity(.2);
        });
        dayPartHBox1.setOnMouseExited(e -> {
            dayPartHBox2.setOpacity(1);
            dayPartLabel2.setOpacity(1);
            dayPartHBox3.setOpacity(1);
            dayPartLabel3.setOpacity(1);
            dayPartHBox4.setOpacity(1);
            dayPartLabel4.setOpacity(1);
        });
        dayPartLabel1.setOnMouseEntered(e -> {
            dayPartHBox2.setOpacity(.2);
            dayPartLabel2.setOpacity(.2);
            dayPartHBox3.setOpacity(.2);
            dayPartLabel3.setOpacity(.2);
            dayPartHBox4.setOpacity(.2);
            dayPartLabel4.setOpacity(.2);
        });
        dayPartLabel1.setOnMouseExited(e -> {
            dayPartHBox2.setOpacity(1);
            dayPartLabel2.setOpacity(1);
            dayPartHBox3.setOpacity(1);
            dayPartLabel3.setOpacity(1);
            dayPartHBox4.setOpacity(1);
            dayPartLabel4.setOpacity(1);
        });
        dayPartHBox2.setOnMouseEntered(e -> {
            dayPartHBox1.setOpacity(.2);
            dayPartLabel1.setOpacity(.2);
            dayPartHBox3.setOpacity(.2);
            dayPartLabel3.setOpacity(.2);
            dayPartHBox4.setOpacity(.2);
            dayPartLabel4.setOpacity(.2);
        });
        dayPartHBox2.setOnMouseExited(e -> {
            dayPartHBox1.setOpacity(1);
            dayPartLabel1.setOpacity(1);
            dayPartHBox3.setOpacity(1);
            dayPartLabel3.setOpacity(1);
            dayPartHBox4.setOpacity(1);
            dayPartLabel4.setOpacity(1);
        });
        dayPartLabel2.setOnMouseEntered(e -> {
            dayPartHBox1.setOpacity(.2);
            dayPartLabel1.setOpacity(.2);
            dayPartHBox3.setOpacity(.2);
            dayPartLabel3.setOpacity(.2);
            dayPartHBox4.setOpacity(.2);
            dayPartLabel4.setOpacity(.2);
        });
        dayPartLabel2.setOnMouseExited(e -> {
            dayPartHBox1.setOpacity(1);
            dayPartLabel1.setOpacity(1);
            dayPartHBox3.setOpacity(1);
            dayPartLabel3.setOpacity(1);
            dayPartHBox4.setOpacity(1);
            dayPartLabel4.setOpacity(1);
        });
        dayPartHBox3.setOnMouseEntered(e -> {
            dayPartHBox1.setOpacity(.2);
            dayPartLabel1.setOpacity(.2);
            dayPartHBox2.setOpacity(.2);
            dayPartLabel2.setOpacity(.2);
            dayPartHBox4.setOpacity(.2);
            dayPartLabel4.setOpacity(.2);
        });
        dayPartHBox3.setOnMouseExited(e -> {
            dayPartHBox1.setOpacity(1);
            dayPartLabel1.setOpacity(1);
            dayPartHBox2.setOpacity(1);
            dayPartLabel2.setOpacity(1);
            dayPartHBox4.setOpacity(1);
            dayPartLabel4.setOpacity(1);
        });
        dayPartLabel3.setOnMouseEntered(e -> {
            dayPartHBox1.setOpacity(.2);
            dayPartLabel1.setOpacity(.2);
            dayPartHBox2.setOpacity(.2);
            dayPartLabel2.setOpacity(.2);
            dayPartHBox4.setOpacity(.2);
            dayPartLabel4.setOpacity(.2);
        });
        dayPartLabel3.setOnMouseExited(e -> {
            dayPartHBox1.setOpacity(1);
            dayPartLabel1.setOpacity(1);
            dayPartHBox2.setOpacity(1);
            dayPartLabel2.setOpacity(1);
            dayPartHBox4.setOpacity(1);
            dayPartLabel4.setOpacity(1);
        });
        dayPartHBox4.setOnMouseEntered(e -> {
            dayPartHBox1.setOpacity(.2);
            dayPartLabel1.setOpacity(.2);
            dayPartHBox2.setOpacity(.2);
            dayPartLabel2.setOpacity(.2);
            dayPartHBox3.setOpacity(.2);
            dayPartLabel3.setOpacity(.2);
        });
        dayPartHBox4.setOnMouseExited(e -> {
            dayPartHBox1.setOpacity(1);
            dayPartLabel1.setOpacity(1);
            dayPartHBox2.setOpacity(1);
            dayPartLabel2.setOpacity(1);
            dayPartHBox3.setOpacity(1);
            dayPartLabel3.setOpacity(1);
        });
        dayPartLabel4.setOnMouseEntered(e -> {
            dayPartHBox1.setOpacity(.2);
            dayPartLabel1.setOpacity(.2);
            dayPartHBox2.setOpacity(.2);
            dayPartLabel2.setOpacity(.2);
            dayPartHBox3.setOpacity(.2);
            dayPartLabel3.setOpacity(.2);
        });
        dayPartLabel4.setOnMouseExited(e -> {
            dayPartHBox1.setOpacity(1);
            dayPartLabel1.setOpacity(1);
            dayPartHBox2.setOpacity(1);
            dayPartLabel2.setOpacity(1);
            dayPartHBox3.setOpacity(1);
            dayPartLabel3.setOpacity(1);
        });
        //
        for (int i = 0; i < 7; i++) {
            int finalI = i;
            ForecastHBox.getChildren().get(i).setOnMouseClicked(e -> {
                if (selectedDay != finalI) {
                    selectDay(finalI);
                    selectedDay = finalI;

                    try {
                        Weather weatherData = requestWeatherData(currentLocation);
                        setDailyData(currentLocation, weatherData.getDTimeArray(), weatherData.getDUVIndexArray(),
                                weatherData.getDSunriseArray(), weatherData.getDSunsetArray(), weatherData.getDMinTempArray(),
                                weatherData.getDMaxTempArray(), selectedDay, weatherData.getDMaxTempArray(), weatherData.getDPrecipitationProbabilityArray(),
                                weatherData.getDWeatherCodeArray(), weatherData.getHTempArray(), weatherData.getHWeatherCodeArray(), weatherData.getHWindSpeedArray());
                    } catch (IOException | URISyntaxException | ParseException ex) {
                        callConnectionAlert();
                    }
                }
            });
        }

        // Creating separators
        for (int i = 0; i < 4; i++) {
            Separator separator = new Separator();
            separator.setOrientation(Orientation.VERTICAL);
            separator.setMaxHeight(Double.MAX_VALUE);
            separator.setId("HBoxSeparator");
            separator.setOpacity(.35);
            if (i == 1) {
                CurrentDailyHBox.getChildren().add(i, separator);
                continue;
            }
            if (i == 3) {
                CurrentDailyHBox.getChildren().add(i, separator);
                separator.setVisible(false);
            }
        }

        // Creating a search button
        Image searchButtonImage = new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/images/UI-Icons/ui-icon-33-1.png")));
        ImageView searchButtonImageView = new ImageView(searchButtonImage);
        searchButtonImageView.setFitWidth(searchButton.getPrefWidth());
        searchButtonImageView.setPreserveRatio(true);
        searchButton.setGraphic(searchButtonImageView);

        // Setting the fonts
        searchBarTextField.setFont(SFProRegularSmall);
        currentLocationLabel.setFont(SFProBoldUnderNormal);
        currentTempLabel.setFont(SFProHeavyHuge);
        currentStateLabel.setFont(SFProRegularUnderNormal);
        //
        dateLabel.setFont(SFProBoldUnderNormal);
        UVLabel.setFont(SFProRegularUnderNormal);
        moonLabel.setFont(SFProRegularUnderNormal);
        //
        dayPartLabel1.setFont(SFProBoldUnderNormal);
        dayPartLabel2.setFont(SFProBoldUnderNormal);
        dayPartLabel3.setFont(SFProBoldUnderNormal);
        dayPartLabel4.setFont(SFProBoldUnderNormal);
        //
        for (HBox hBox : new HBox[]{dayPartHBox1, dayPartHBox2, dayPartHBox3, dayPartHBox4}) {
            setFontToAllLabelsOf(hBox, SFProRegularUnderNormal);
        }
        for (Node currentLowerDataVBox : currentLowerGridPane.getChildren()) {
            setFontToAllLabelsOf((VBox) currentLowerDataVBox, SFProRegularUnderNormal);
        }
        //
        for (int i = 0; i < 7; i++) {
            ((Label) ((VBox) ForecastHBox.getChildren().get(i)).getChildren().get(0)).setFont(SFProBoldUnderNormal);
            ((Label) ((VBox) ForecastHBox.getChildren().get(i)).getChildren().get(2)).setFont(SFProRegularUnderNormal);
        }

        // Customizing tooltips
        ArrayList<Tooltip> tooltips = new ArrayList<>();
        Tooltip CHumidityTT = new Tooltip("Current\nhumidity");
        Tooltip.install(currentLowerGridPane.getChildren().get(0), CHumidityTT);
        tooltips.add(CHumidityTT);
        Tooltip CCloudCoverTT = new Tooltip("Current\ncloud cover");
        Tooltip.install(currentLowerGridPane.getChildren().get(1), CCloudCoverTT);
        tooltips.add(CCloudCoverTT);
        Tooltip CPressureTT = new Tooltip("Current\npressure");
        Tooltip.install(currentLowerGridPane.getChildren().get(2), CPressureTT);
        tooltips.add(CPressureTT);
        Tooltip CWindSpeedTT = new Tooltip("Current\nwind speed");
        Tooltip.install(currentLowerGridPane.getChildren().get(3), CWindSpeedTT);
        tooltips.add(CWindSpeedTT);

        Tooltip UVIndexTT = new Tooltip("UV Index");
        Tooltip.install(dailyMiddleGridPane.getChildren().get(0), UVIndexTT);
        tooltips.add(UVIndexTT);
        Tooltip MoonPhaseTT = new Tooltip("Moon Phase");
        Tooltip.install(dailyMiddleGridPane.getChildren().get(1), MoonPhaseTT);
        tooltips.add(MoonPhaseTT);

        Tooltip SunriseTT = new Tooltip("Sunrise time");
        Tooltip.install(dailyLowerGridPane2.getChildren().get(0), SunriseTT);
        tooltips.add(SunriseTT);
        Tooltip SunsetTT = new Tooltip("Sunset time");
        Tooltip.install(dailyLowerGridPane2.getChildren().get(1), SunsetTT);
        tooltips.add(SunsetTT);
        Tooltip MaxTempTT = new Tooltip("Maximum temperature");
        Tooltip.install(dailyLowerGridPane2.getChildren().get(2), MaxTempTT);
        tooltips.add(MaxTempTT);
        Tooltip MinTempTT = new Tooltip("Minimum temperature");
        Tooltip.install(dailyLowerGridPane2.getChildren().get(3), MinTempTT);
        tooltips.add(MinTempTT);
        Tooltip PrecipitationProbabilityTT = new Tooltip("Precipitation probability");
        Tooltip.install(dailyLowerGridPane2.getChildren().get(4), PrecipitationProbabilityTT);
        tooltips.add(PrecipitationProbabilityTT);
        Tooltip MaxWindSpeedTTM = new Tooltip("Maximum wind speed");
        Tooltip.install(dailyLowerGridPane2.getChildren().get(5), MaxWindSpeedTTM);
        tooltips.add(MaxWindSpeedTTM);

        Tooltip ImpicitExitTT = new Tooltip("Activate to keep in tray after closing");
        Tooltip.install(implicitExitToggle, ImpicitExitTT);
        tooltips.add(ImpicitExitTT);

        for (Tooltip tt : tooltips) {
            tt.setFont(SFProLightUnderNormal);
            tt.setMaxWidth(200);
            tt.setWrapText(true);
            tt.setStyle("-fx-background-color: rgba(0, 0, 0, 0.69);");
            tt.setTextAlignment(TextAlignment.CENTER);
            tt.setShowDelay(new Duration(300));
        }
        ImpicitExitTT.setMaxWidth(250);

        Image ttImage = new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/images/UI-Icons/ui-icon-38.png")));
        ImageView ttImageView = new ImageView(ttImage);
        ttImageView.setFitWidth(35);
        ttImageView.setPreserveRatio(true);
        CApproximateTemp = new Tooltip("Apparent temperature:\n-0°C");
        CApproximateTemp.setStyle("-fx-background-color: rgba(0, 0, 0, 0.69);");
        CApproximateTemp.setFont(SFProLightUnderNormal);
        CApproximateTemp.setTextAlignment(TextAlignment.CENTER);
        CApproximateTemp.setShowDelay(new Duration(300));
        CApproximateTemp.setGraphic(ttImageView);
        CApproximateTemp.setGraphicTextGap(10);
        Tooltip.install(currentTempLabel, CApproximateTemp);

        // Getting default location info from default.txt
        try {
            FileReader in = new FileReader("default.txt");
            BufferedReader br = new BufferedReader(in);
            ArrayList<String> defaultData = new ArrayList<>();
            for (String line; (line = br.readLine()) != null; ) {
                defaultData.add(line);
            }
            in.close();
            br.close();
            currentLocation = new Location(defaultData.get(0), Double.parseDouble(defaultData.get(1)), Double.parseDouble(defaultData.get(2)));
            isPressureInhPa = Boolean.parseBoolean(defaultData.get(3));
            setToKeepInTray = Boolean.parseBoolean(defaultData.get(4));
            dateLabel.setText(getDate(Double.parseDouble(defaultData.get(1)), Double.parseDouble(defaultData.get(2))));
            if (setToKeepInTray) {
                implicitExitToggle.setOpacity(1);
            } else {
                implicitExitToggle.setOpacity(.5);
            }

        } catch (Exception e) {
            currentLocation = new Location("Karaganda", 49.8019, 73.1021);
            writeDefaultData(currentLocation);
        }


        // Setting data from default location
        try {
            Weather weatherData = requestWeatherData(currentLocation);
            setWeatherData(weatherData);
        } catch (Exception e) {
            callConnectionAlert();
        }
        selectDay(selectedDay);
    }

    private void setWeatherData(Weather weatherData) throws ParseException, URISyntaxException, IOException {
        setCurrentData(currentLocation.getName(),
                weatherData.getCWeatherCode(),
                weatherData.getCTemp(),
                weatherData.getCHumidity(),
                weatherData.getCPressure(),
                weatherData.getCCloudCover(),
                weatherData.getCWindSpeed(),
                weatherData.getCAppTemp(),
                weatherData.getCIsDay());
        setDailyData(currentLocation,
                weatherData.getDTimeArray(),
                weatherData.getDUVIndexArray(),
                weatherData.getDSunriseArray(),
                weatherData.getDSunsetArray(),
                weatherData.getDMinTempArray(),
                weatherData.getDMaxTempArray(),
                0,
                weatherData.getDMaxWindSpeedArray(),
                weatherData.getDPrecipitationProbabilityArray(),
                weatherData.getDWeatherCodeArray(),
                weatherData.getHTempArray(),
                weatherData.getHWeatherCodeArray(),
                weatherData.getHWindSpeedArray());
    }

    private Weather requestWeatherData(Location location) throws IOException, URISyntaxException {
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        String timeZone = Objects.requireNonNull(getTimeZoneData(location.getLatitude(), location.getLongitude())).getTimeZone();
        final URL requestURL = new URI("https://api.open-meteo.com/v1/forecast?latitude=" + location.getLatitude() + "&longitude=" + location.getLongitude() + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,cloud_cover,surface_pressure,wind_speed_10m&daily=weather_code,precipitation_probability_max,wind_speed_10m_max,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max&hourly=temperature_2m,weather_code,wind_speed_10m&wind_speed_unit=ms&timezone=" + timeZone).toURL();
        HttpsURLConnection connection = (HttpsURLConnection) requestURL.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            StringBuilder jsonResponse = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            JsonObject currentObject = GSON.fromJson(jsonResponse.toString(), JsonObject.class).getAsJsonObject("current");
            JsonObject dailyObject = GSON.fromJson(jsonResponse.toString(), JsonObject.class).getAsJsonObject("daily");
            JsonObject hourlyObject = GSON.fromJson(jsonResponse.toString(), JsonObject.class).getAsJsonObject("hourly");

            return new Weather(GSON.fromJson(currentObject, CurrentWeather.class), GSON.fromJson(dailyObject, DailyWeather.class), GSON.fromJson(hourlyObject, HourlyWeather.class));
        } else {
            callConnectionAlert();
            return null;
        }
    }

    private TimeZoneData getTimeZoneData(double latitude, double longtitude) throws IOException {
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        final URL requestURL = new URL(String.format("https://timeapi.io/api/TimeZone/coordinate?latitude=%f&longitude=%f", latitude, longtitude).replace(',', '.'));
        HttpsURLConnection connection = (HttpsURLConnection) requestURL.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            StringBuilder jsonResponse = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            return GSON.fromJson(jsonResponse.toString(), TimeZoneData.class);
        } else {
            callConnectionAlert();
            return null;
        }
    }

    private Calendar getSpecificCalendar(double latitude, double longtitude) throws IOException, ParseException {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Objects.requireNonNull(getTimeZoneData(latitude, longtitude)).getTimeZone()));
        String localDateTime = Objects.requireNonNull(getTimeZoneData(latitude, longtitude)).getCurrentLocalTime();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        cal.setTime(parser.parse(localDateTime));
        return cal;
    }

    private Calendar getDynamicCalendar(String requiredDateTime, boolean extended) throws ParseException {
        SimpleDateFormat parser;
        Calendar cal = Calendar.getInstance();
        if (extended)
            parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        else
            parser = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(parser.parse(requiredDateTime));
        return cal;
    }

    private String getDayOfMonth(double latitude, double longtitude) throws IOException, ParseException {
        int dayOfMonth = getSpecificCalendar(latitude, longtitude).get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth % 10 == 1) {
            return dayOfMonth + "st";
        }
        if (dayOfMonth % 10 == 2) {
            return dayOfMonth + "nd";
        }
        if (dayOfMonth % 10 == 3) {
            return dayOfMonth + "rd";
        }
        return dayOfMonth + "th";
    }

    private String getMonthName(double latitude, double longtitude) throws IOException, ParseException {
        int monthNumber = getSpecificCalendar(latitude, longtitude).get(Calendar.MONTH);
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] months = dfs.getMonths();
        return months[monthNumber];
    }

    private String getDayOfWeekName(Calendar calendar) {
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] daysOfWeek = dfs.getWeekdays();
        return daysOfWeek[dayOfWeekNumber];
    }

    private String getDate(double latitude, double longtitude) throws IOException, ParseException {
        return String.format("%s %s,\n%s", getMonthName(latitude, longtitude), getDayOfMonth(latitude, longtitude), getDayOfWeekName(getSpecificCalendar(latitude, longtitude)));
    }

    private String getPickedDate(String[] TimeArray, int pickedDayOfWeek) throws ParseException {
        Calendar cal = getDynamicCalendar(TimeArray[pickedDayOfWeek], false);

        int monthNumber = cal.get(Calendar.MONTH);
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String monthName = dfs.getMonths()[monthNumber];

        String dayOfMonthName;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth % 10 == 1) {
            dayOfMonthName = dayOfMonth + "st";
        } else if (dayOfMonth % 10 == 2) {
            dayOfMonthName = dayOfMonth + "nd";
        } else if (dayOfMonth % 10 == 3) {
            dayOfMonthName = dayOfMonth + "rd";
        } else dayOfMonthName = dayOfMonth + "th";

        int dayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekName = dfs.getWeekdays()[dayOfWeekNumber];

        return String.format("%s %s,\n%s", monthName, dayOfMonthName, dayOfWeekName);
    }

    private String getStateIcon(int WC, int CIsDay) {
        if (CIsDay == 1) {
            return switch (WC) {
                // clouds / fog
                case 0, 1 -> "25";
                case 2 -> "22";
                case 3 -> "19";
                case 45, 48 -> "31";
                // drizzle / rain / freezing rain
                case 51, 56, 61 -> "2";
                case 53, 63 -> "5";
                case 55, 57, 65 -> "15";
                case 66, 67 -> "32";
                // snowfalls
                case 71, 77 -> "14-1";
                case 73 -> "13";
                case 75 -> "14";
                // rain showers / snow showers
                case 80 -> "15";
                case 81 -> "8";
                case 82 -> "10";
                case 85 -> "14-1";
                case 86 -> "14";
                // thunderstorms (probably won't work with open-meteo :P)
                case 95 -> "16";
                case 96 -> "8";
                case 99 -> "9";

                default -> "fallback";
            };
        } else {
            return switch (WC) {
                // clouds / fog
                case 0, 1 -> "26";
                case 2 -> "24";
                case 3 -> "21";
                case 45, 48 -> "31";
                // drizzle / rain / freezing rain
                case 51, 56, 61 -> "1";
                case 53, 63 -> "4";
                case 55, 57, 65 -> "15";
                case 66, 67 -> "32";
                // snowfalls
                case 71, 77 -> "14-1";
                case 73 -> "12";
                case 75 -> "14";
                // rain showers / snow showers
                case 80 -> "15";
                case 81 -> "7";
                case 82 -> "10";
                case 85 -> "14-1";
                case 86 -> "14";
                // thunderstorms (probably won't work with open-meteo :P)
                case 95 -> "18";
                case 96 -> "7";
                case 99 -> "11";

                default -> "fallback";
            };
        }
    }

    private String getState(int WC) {
        HashMap<Integer, String> weatherCodes = new HashMap<>();
        weatherCodes.put(0, "Clear Sky");
        weatherCodes.put(1, "Mainly clear");
        weatherCodes.put(2, "Partly Cloudy");
        weatherCodes.put(3, "Overcast");

        weatherCodes.put(45, "Fog");
        weatherCodes.put(48, "Depositing rime fog");

        weatherCodes.put(51, "Light drizzle");
        weatherCodes.put(53, "Moderate drizzle");
        weatherCodes.put(55, "Dense drizzle");
        weatherCodes.put(56, "Light freezing drizzle");
        weatherCodes.put(57, "Dense freezing drizzle");
        weatherCodes.put(61, "Slight rain");
        weatherCodes.put(63, "Moderate rain");
        weatherCodes.put(65, "Heavy rain");
        weatherCodes.put(66, "Light freezing rain");
        weatherCodes.put(67, "Heavy freezing rain");

        weatherCodes.put(71, "Slight snowfall");
        weatherCodes.put(73, "Moderate snowfall");
        weatherCodes.put(75, "Heavy snowfall");
        weatherCodes.put(77, "Snow grains");

        weatherCodes.put(80, "Slight rain showers");
        weatherCodes.put(81, "moderate rain showers");
        weatherCodes.put(82, "Violent rain showers");
        weatherCodes.put(85, "Slight snow showers");
        weatherCodes.put(86, "Heavy snow showers");

        weatherCodes.put(95, "Thunderstorm");
        weatherCodes.put(96, "Slight hail thunderstorm");
        weatherCodes.put(99, "Heavy hail thunderstorm");
        if (weatherCodes.get(WC) != null) {
            return weatherCodes.get(WC);
        }
        return "- KEY ERROR -";
    }

    private void setCurrentData(String CLocation, int CWeatherCode, int CTemp, int CHumidity, double CPressure, int CCloudCover, double CWindSpeed, int CAppTemp, int CIsDay) {
        currentLocationLabel.setText("Right now in\n" + CLocation);
        currentStateLabel.setText(getState(CWeatherCode));
        currentTempLabel.setText(CTemp + "°C");
        currentHumidityLabel.setText(CHumidity + " %");
        currentCloudCoverLabel.setText(CCloudCover + " %");
        currentWindSpeedLabel.setText(CWindSpeed + " m/s");
        CApproximateTemp.setText(String.format("Apparent temperature:\n%d°C", CAppTemp));
        currentPressureLabel.setText(getChoosablePressure(CPressure));
        currentIcon.setImage(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream(String.format("/images/UI-Icons/ui-icon-%s.png", getStateIcon(CWeatherCode, CIsDay))))));
    }

    private void setDailyData(Location location, String[] DTimeArray, double[] DUVindex, String[] DSunriseTime,
                              String[] DSunsetTime, double[] DMinTemp, double[] DMaxTemp, int pickedDayOfWeek,
                              double[] DMaxWindSpeed, int[] DPrecipitationProbability, int[] DWeatherCodeArray, double[] HTempArray,
                              int[] HWeatherCodeArray, double[] HWindSpeedArray) throws ParseException, URISyntaxException, IOException {
        dateLabel.setText(getPickedDate(DTimeArray, pickedDayOfWeek));
        UVLabel.setText(DUVindex[pickedDayOfWeek] + " / 11");
        setMoonPhaseData(requestMoonPhaseData(location, DTimeArray[pickedDayOfWeek]));
        sunriseLabel.setText(String.format("%s:%s", getDynamicCalendar(DSunriseTime[pickedDayOfWeek], true).get(Calendar.HOUR_OF_DAY), getDynamicCalendar(DSunriseTime[pickedDayOfWeek], true).get(Calendar.MINUTE)));
        sunsetLabel.setText(String.format("%s:%s", getDynamicCalendar(DSunsetTime[pickedDayOfWeek], true).get(Calendar.HOUR_OF_DAY), getDynamicCalendar(DSunsetTime[pickedDayOfWeek], true).get(Calendar.MINUTE)));
        minTempLabel.setText(DMinTemp[pickedDayOfWeek] + "°C");
        maxTempLabel.setText(DMaxTemp[pickedDayOfWeek] + "°C");
        maxWindSpeedLabel.setText(DMaxWindSpeed[pickedDayOfWeek] + " m/s");
        precipitationProbabilityLabel.setText(DPrecipitationProbability[pickedDayOfWeek] + " %");
        setHourlyData(HTempArray, HWeatherCodeArray, HWindSpeedArray, pickedDayOfWeek);
        setForecastData(DTimeArray, DMinTemp, DMaxTemp, DWeatherCodeArray);
    }

    private void setHourlyData(double[] HTempArray, int[] HWeatherCodeArray, double[] HWindSpeedArray, int pickedDayOfWeek) {
        morningTempLabel.setText(HTempArray[getHourlyIndex(0, pickedDayOfWeek)] + "°");
        middayTempLabel.setText(HTempArray[getHourlyIndex(1, pickedDayOfWeek)] + "°");
        eveningTempLabel.setText(HTempArray[getHourlyIndex(2, pickedDayOfWeek)] + "°");
        nighttimeTempLabel.setText(HTempArray[getHourlyIndex(3, pickedDayOfWeek)] + "°");
        //
        morningWindSpeedLabel.setText(HWindSpeedArray[getHourlyIndex(0, pickedDayOfWeek)] + "  m/s");
        middayWindSpeedLabel.setText(HWindSpeedArray[getHourlyIndex(1, pickedDayOfWeek)] + " m/s");
        eveningWindSpeedLabel.setText(HWindSpeedArray[getHourlyIndex(2, pickedDayOfWeek)] + " m/s");
        nighttimeWindSpeedLabel.setText(HWindSpeedArray[getHourlyIndex(3, pickedDayOfWeek)] + " m/s");
        //
        morningWeatherIcon.setImage(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream(String.format("/images/UI-Icons/ui-icon-%s.png",
                getStateIcon(HWeatherCodeArray[getHourlyIndex(0, pickedDayOfWeek)], 1))))));
        middayWeatherIcon.setImage(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream(String.format("/images/UI-Icons/ui-icon-%s.png",
                getStateIcon(HWeatherCodeArray[getHourlyIndex(1, pickedDayOfWeek)], 1))))));
        eveningWeatherIcon.setImage(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream(String.format("/images/UI-Icons/ui-icon-%s.png",
                getStateIcon(HWeatherCodeArray[getHourlyIndex(2, pickedDayOfWeek)], 0))))));
        nighttimeWeatherIcon.setImage(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream(String.format("/images/UI-Icons/ui-icon-%s.png",
                getStateIcon(HWeatherCodeArray[getHourlyIndex(3, pickedDayOfWeek)], 0))))));
    }

    private int getHourlyIndex(int dayPart, int pickedDayOfWeek) {
        int[] thisDayHours = IntStream.rangeClosed(pickedDayOfWeek * 24, (pickedDayOfWeek + 1) * 24 - 1).toArray();
        return switch (dayPart) {
            case 0 -> thisDayHours[8];
            case 1 -> thisDayHours[13];
            case 2 -> thisDayHours[18];
            case 3 -> thisDayHours[23];
            default -> 0;
        };
    }

    private void setForecastData(String[] TimeArray, double[] DMinTemp, double[] DMaxTemp, int[] DWeatherCodeArray) throws ParseException {
        Calendar cal = getDynamicCalendar(TimeArray[0], false);
        ImageView[] dayIcons = {day0Icon, day1Icon, day2Icon, day3Icon, day4Icon, day5Icon, day6Icon};
        for (Node vbox : ForecastHBox.getChildren()) {
            ((Label) ((VBox) vbox).getChildren().get(0)).setText(String.format("%02d.%02d\n%s", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, getDayOfWeekName(cal)));
            cal.add(Calendar.DATE, 1);
        }
        for (int i = 0; i < 7; i++) {
            dayIcons[i].setImage(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream(String.format("/images/UI-Icons/ui-icon-%s.png",
                    getStateIcon(DWeatherCodeArray[i], 1))))));
            ((Label) ((VBox) ForecastHBox.getChildren().get(i)).getChildren().get(2)).setText(String.format("%s° | %s°", DMinTemp[i], DMaxTemp[i]));
        }
    }

    private double requestMoonPhaseData(Location location, String date) throws URISyntaxException, IOException {
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        final URL requestURL = new URI("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + location.getLatitude() + "," + location.getLongitude() + "/" + date + "?unitGroup=us&key=HM6RN835LXWGU8DLSSD43XHJH&include=days&elements=moonphase").toURL();
        HttpsURLConnection connection = (HttpsURLConnection) requestURL.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            StringBuilder jsonResponse = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }

            MoonhaseData mfd = GSON.fromJson(jsonResponse.toString(), MoonhaseData.class);
            return mfd.getDays().get(0).getMoonphase();
        } else {
            callConnectionAlert();
            return 0;
        }
    }

    private String getMoonPhaseState(double moonPhase) {
        if (moonPhase == 0) return "New moon";
        if (moonPhase > 0 && moonPhase < 0.25) return "Waxing crescent";
        if (moonPhase == 0.25) return "First quarter";
        if (moonPhase > 0.25 && moonPhase < 0.5) return "Waxing gibbous";
        if (moonPhase == 0.5) return "Full moon";
        if (moonPhase > 0.5 && moonPhase < 0.75) return "Waning gibbous";
        if (moonPhase == 0.75) return "Last quarter";
        if (moonPhase > 0.75 && moonPhase <= 1) return "Waning crescent";
        else return "-KEY ERROR-";
    }

    private void setMoonPhaseData(double moonPhase) {
        String moonPhaseImageName = "";
        if (moonPhase == 0) {
            moonPhaseImageName = "new-moon";
        }
        if (moonPhase > 0 && moonPhase < 0.25) {
            moonPhaseImageName = "waxing-crescent";
        }
        if (moonPhase == 0.25) {
            moonPhaseImageName = "first-quarter";
        }
        if (moonPhase > 0.25 && moonPhase < 0.5) {
            moonPhaseImageName = "waxing-gibbous";
        }
        if (moonPhase == 0.5) {
            moonPhaseImageName = "full-moon";
        }
        if (moonPhase > 0.5 && moonPhase < 0.75) {
            moonPhaseImageName = "waning-gibbous";
        }
        if (moonPhase == 0.75) {
            moonPhaseImageName = "last-quarter";
        }
        if (moonPhase > 0.75 && moonPhase <= 1) {
            moonPhaseImageName = "waning-crescent";
        }
        moonLabel.setText(getMoonPhaseState(moonPhase));
        moonIcon.setImage(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/images/UI-Icons/moon-phases/ui-icon-" + moonPhaseImageName + ".png"))));
    }

    private void setFontToAllLabelsOf(Pane pane, Font font) {
        for (Node label : pane.getChildren()) {
            if (label instanceof Label) {
                ((Label) label).setFont(font);
            }
        }
    }

    @FXML
    protected void switchPressureUnits() {
        if (isPressureInhPa) {
            isPressureInhPa = false;
            String pressureString = currentPressureLabel.getText().substring(0, currentPressureLabel.getText().length() - 3);
            currentPressureLabel.setText(round(Double.parseDouble(pressureString) / 1.33322387415, 2) + " mm Hg");
        } else {
            isPressureInhPa = true;
            String pressureString = currentPressureLabel.getText().substring(0, currentPressureLabel.getText().length() - 5);
            currentPressureLabel.setText(round(Double.parseDouble(pressureString) * 1.33322387415, 2) + " hPa");
        }
    }

    public double round(double value, int places) {
        if (places < 0)
            createErrorAlert("Make sure to beat the crap out of dev.", "Something went wrong", "An exception has been caught.");
        BigDecimal bd = BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    protected String getChoosablePressure(double pressure) {
        if (isPressureInhPa) {
            return pressure + " hPa";
        }
        return round(pressure / 1.33322387415, 2) + " mm Hg";
    }

    protected void expand() {
        currentGridPane.setMaxWidth(380);
        AnchorPane.setLeftAnchor(CurrentDailyHBox, 50.0);
        AnchorPane.setRightAnchor(CurrentDailyHBox, 50.0);
        dailyGridPane.getColumnConstraints().get(0).setPercentWidth(50);
        dailyGridPane.getColumnConstraints().get(1).setPercentWidth(50);
        dailyGridPane.getRowConstraints().get(1).setPercentHeight(20);
        dailyGridPane.getRowConstraints().get(2).setPercentHeight(60);
        GridPane.setColumnSpan(dailyUpperGridPane, 2);
        GridPane.setColumnIndex(dailyMiddleGridPane, 1);
        GridPane.setRowIndex(dailyLowerGridPane, 1);
        GridPane.setRowSpan(dailyLowerGridPane, 2);
        dailyLowerGridPane2.setVisible(true);
        isExpanded = !isExpanded;
    }

    protected void shrink() {
        currentGridPane.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(CurrentDailyHBox, 190.0);
        AnchorPane.setRightAnchor(CurrentDailyHBox, 190.0);
        dailyGridPane.getColumnConstraints().get(0).setPercentWidth(100);
        dailyGridPane.getColumnConstraints().get(1).setPercentWidth(0);
        dailyLowerGridPane2.setVisible(false);
        GridPane.setColumnSpan(dailyUpperGridPane, 1);
        GridPane.setColumnIndex(dailyMiddleGridPane, 0);
        GridPane.setRowIndex(dailyLowerGridPane, 2);
        GridPane.setRowSpan(dailyLowerGridPane, 1);
        isExpanded = !isExpanded;
    }

    // Changing fields to display fullscreen mode properly
    @FXML
    public void switchFullScreen() {
//        Stage stage = (Stage) MainAnchorPane.getScene().getWindow();
//        if (stage.isFullScreen()) {
//            // FROM FULLSCREEN
//            clipRect.setArcHeight(15);
//            clipRect.setArcWidth(15);
//
//            for (Node currentLowerDataVBox : currentLowerGridPane.getChildren()) {
//                for (Node currentDataIcon : ((VBox) currentLowerDataVBox).getChildren()) {
//                    if (currentDataIcon instanceof ImageView) {
//                        scaleTo(currentDataIcon, 1);
//                        currentDataIcon.setTranslateY(0);
//                    }
//                }
//            }
//            for (Node dailyLowerDataVBox2 : dailyLowerGridPane2.getChildren()) {
//                for (Node dailyDataIcon : ((VBox) dailyLowerDataVBox2).getChildren()) {
//                    if (dailyDataIcon instanceof ImageView) {
//                        scaleTo(dailyDataIcon, 1);
//                        dailyDataIcon.setTranslateY(0);
//                    }
//                }
//            }
//            for (Node dailyLowerDataHBox : dailyLowerGridPane.getChildren()) {
//                if (dailyLowerDataHBox instanceof HBox) {
//                    ((HBox) dailyLowerDataHBox).setSpacing(20);
//                    for (Node hourlyDataIcon : ((HBox) dailyLowerDataHBox).getChildren()) {
//                        if (hourlyDataIcon instanceof ImageView) {
//                            scaleTo(hourlyDataIcon, 1);
//                        }
//                    }
//                }
//            }
//            scaleTo(currentIcon, .6);
//            scaleTo(UVIcon, 1);
//            scaleTo(moonIcon, 1);
//            UVIcon.setTranslateY(0);
//            moonIcon.setTranslateY(0);
//            UVVBox.setPadding(new Insets(0, 0, 15, 0));
//            moonVBox.setPadding(new Insets(0, 0, 15, 0));
//
//            for (Node node : dailyLowerGridPane.getChildren()) {
//                if (node instanceof Label) {
//                    ((Label) node).setAlignment(Pos.CENTER_LEFT);
//                } else if (node instanceof HBox) {
//                    ((HBox) node).setAlignment(Pos.CENTER);
//                }
//            }
//
//            CurrentDailyHBox.setStyle("-fx-background-radius: 10;");
//            ForecastHBox.setStyle("-fx-background-radius: 10;");
//            day0.setStyle("-fx-background-radius: 10 0 0 10;");
//            day6.setStyle("-fx-background-radius: 0 10 10 0;");
//
//            setFonts(SFProHeavyHuge, SFProRegularUnderNormal);
//            currentLocationLabel.setFont(SFProBoldUnderNormal);
//            dateLabel.setFont(SFProBoldUnderNormal);
//            for (Label label : new Label[]{dayPartLabel1, dayPartLabel2, dayPartLabel3, dayPartLabel4})
//                label.setFont(SFProBoldUnderNormal);
//            for (int i = 0; i < 7; i++) {
//                ((Label) ((VBox) ForecastHBox.getChildren().get(i)).getChildren().get(0)).setFont(SFProBoldUnderNormal);
//                ((Label) ((VBox) ForecastHBox.getChildren().get(i)).getChildren().get(2)).setFont(SFProRegularUnderNormal);
//            }
//
//            dailyGridPane.getColumnConstraints().get(0).setPercentWidth(100);
//            dailyGridPane.getColumnConstraints().get(1).setPercentWidth(0);
//            dailyLowerGridPane2.setVisible(false);
//            GridPane.setColumnSpan(dailyUpperGridPane, 1);
//            GridPane.setColumnIndex(dailyMiddleGridPane, 0);
//            GridPane.setRowIndex(dailyLowerGridPane, 2);
//            GridPane.setRowSpan(dailyLowerGridPane, 1);
//            if (isExpanded) {
//                expand();
//                isExpanded = true;
//            }
//
//            dayPartLabel1.setText(dayPartLabel1.getText().substring(0, dayPartLabel1.getText().length() - 6));
//            dayPartLabel2.setText(dayPartLabel2.getText().substring(0, dayPartLabel2.getText().length() - 6));
//            dayPartLabel3.setText(dayPartLabel3.getText().substring(0, dayPartLabel3.getText().length() - 6));
//            dayPartLabel4.setText(dayPartLabel4.getText().substring(0, dayPartLabel4.getText().length() - 6));
//        } else {
//            // TO FULLSCREEN
//            clipRect.setArcHeight(0);
//            clipRect.setArcWidth(0);
//
//            for (Node currentLowerDataVBox : currentLowerGridPane.getChildren()) {
//                for (Node currentDataIcon : ((VBox) currentLowerDataVBox).getChildren()) {
//                    if (currentDataIcon instanceof ImageView) {
//                        scaleTo(currentDataIcon, 1.5);
//                        currentDataIcon.setTranslateY(-15);
//                    }
//                }
//            }
//            for (Node dailyLowerDataVBox2 : dailyLowerGridPane2.getChildren()) {
//                for (Node dailyDataIcon : ((VBox) dailyLowerDataVBox2).getChildren()) {
//                    if (dailyDataIcon instanceof ImageView) {
//                        scaleTo(dailyDataIcon, 1.5);
//                        dailyDataIcon.setTranslateY(-15);
//                    }
//                }
//            }
//            for (Node dailyLowerDataHBox : dailyLowerGridPane.getChildren()) {
//                if (dailyLowerDataHBox instanceof HBox) {
//                    ((HBox) dailyLowerDataHBox).setSpacing(40);
//                    for (Node hourlyDataIcon : ((HBox) dailyLowerDataHBox).getChildren()) {
//                        if (hourlyDataIcon instanceof ImageView) {
//                            scaleTo(hourlyDataIcon, 1.5);
//                        }
//                    }
//                }
//            }
//
//            scaleTo(currentIcon, 1);
//            scaleTo(UVIcon, 1.5);
//            scaleTo(moonIcon, 1.5);
//            UVIcon.setTranslateY(-15);
//            moonIcon.setTranslateY(-15);
//            UVVBox.setPadding(new Insets(0));
//            moonVBox.setPadding(new Insets(0));
//
//            for (Node node : dailyLowerGridPane.getChildren()) {
//                if (node instanceof Label) {
//                    ((Label) node).setAlignment(Pos.CENTER);
//                } else if (node instanceof HBox) {
//                    ((HBox) node).setAlignment(Pos.CENTER);
//                }
//            }
//
//            CurrentDailyHBox.setStyle("-fx-background-radius: 20;");
//            ForecastHBox.setStyle("-fx-background-radius: 20;");
//            day0.setStyle("-fx-background-radius: 20 0 0 20;");
//            day6.setStyle("-fx-background-radius: 0 20 20 0;");
//
//            setFonts(SFProHeavyFullScreen, SFProRegularNormal);
//            currentLocationLabel.setFont(SFProBoldNormal);
//            dateLabel.setFont(SFProBoldNormal);
//            for (Label label : new Label[]{dayPartLabel1, dayPartLabel2, dayPartLabel3, dayPartLabel4})
//                label.setFont(SFProBoldNormal);
//            for (int i = 0; i < 7; i++) {
//                ((Label) ((VBox) ForecastHBox.getChildren().get(i)).getChildren().get(0)).setFont(SFProBoldNormal);
//                ((Label) ((VBox) ForecastHBox.getChildren().get(i)).getChildren().get(2)).setFont(SFProRegularNormal);
//            }
//            MainAnchorPane.requestFocus();
//
//            currentGridPane.setMaxWidth(600);
//            AnchorPane.setLeftAnchor(CurrentDailyHBox, 330.0);
//            AnchorPane.setRightAnchor(CurrentDailyHBox, 330.0);
//
//            dailyGridPane.getColumnConstraints().get(0).setPercentWidth(50);
//            dailyGridPane.getColumnConstraints().get(1).setPercentWidth(50);
//            dailyGridPane.getRowConstraints().get(1).setPercentHeight(20);
//            dailyGridPane.getRowConstraints().get(2).setPercentHeight(60);
//            GridPane.setColumnSpan(dailyUpperGridPane, 2);
//            GridPane.setColumnIndex(dailyMiddleGridPane, 1);
//            GridPane.setRowIndex(dailyLowerGridPane, 1);
//            GridPane.setRowSpan(dailyLowerGridPane, 2);
//            dailyLowerGridPane2.setVisible(true);
//
//            dayPartLabel1.setText(dayPartLabel1.getText() + "\n08:00");
//            dayPartLabel2.setText(dayPartLabel2.getText() + "\n13:00");
//            dayPartLabel3.setText(dayPartLabel3.getText() + "\n18:00");
//            dayPartLabel4.setText(dayPartLabel4.getText() + "\n23:00");
//        }
//        WeatherApp.switchFullScreen(stage);
    }

    private void setFonts(Font bigLabelFont, Font regularLabelFont) {
        currentTempLabel.setFont(bigLabelFont);
        currentStateLabel.setFont(regularLabelFont);
        for (Node currentLowerDataVBox : currentLowerGridPane.getChildren())
            setFontToAllLabelsOf((VBox) currentLowerDataVBox, regularLabelFont);
        for (Node dailyLowerGridPaneVBox : dailyLowerGridPane2.getChildren())
            setFontToAllLabelsOf((VBox) dailyLowerGridPaneVBox, regularLabelFont);
        for (HBox hBox : new HBox[]{dayPartHBox1, dayPartHBox2, dayPartHBox3, dayPartHBox4})
            setFontToAllLabelsOf(hBox, regularLabelFont);
        UVLabel.setFont(regularLabelFont);
        moonLabel.setFont(regularLabelFont);

    }

    private void selectDay(int selectedDay) {
        ForecastHBox.getChildren().get(selectedDay).getStyleClass().add("selectedDay");
        for (int i = 0; i < 7; i++) {
            if (i != selectedDay) {
                ForecastHBox.getChildren().get(i).getStyleClass().clear();
            }
        }
    }

    // Title Bar Buttons
    @FXML
    protected void exit() {
        if (currentLocation != null) {
            writeDefaultData(currentLocation);
        }
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        if (setToKeepInTray)
            stage.close();
        else System.exit(0);
    }

    @FXML
    protected void minimize() {
        Stage stage = (Stage) MinimizeButton.getScene().getWindow();
        searchBarTextField.requestFocus();
        stage.setIconified(true);

//        if (!stage.isFullScreen()) expand();
    }

    private void scaleTo(Node node, double scale) {
        node.setScaleX(scale);
        node.setScaleY(scale);
        node.setScaleZ(scale);
    }

    // Window dragging
    @FXML
    protected void dragWindow(MouseEvent e) {
        Stage stage = (Stage) MainAnchorPane.getScene().getWindow();
        xOffset = stage.getX() - e.getScreenX();
        yOffset = stage.getY() - e.getScreenY();
    }

    @FXML
    protected void moveWindow(MouseEvent e) {
        Stage stage = (Stage) MainAnchorPane.getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setX(e.getScreenX() + xOffset);
            stage.setY(e.getScreenY() + yOffset);
        }
    }

    private void runAway(Node node) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(.3), new KeyValue(node.translateYProperty(), -300))
        );
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }

    private void shake(Node node) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(.05), new KeyValue(node.translateXProperty(), -3)),
                new KeyFrame(Duration.seconds(.1), new KeyValue(node.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(.15), new KeyValue(node.translateXProperty(), 3)),
                new KeyFrame(Duration.seconds(.2), new KeyValue(node.translateXProperty(), 0))
        );
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }

    private void getBack(Node node) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.translateYProperty(), -300)),
                new KeyFrame(Duration.seconds(.3), new KeyValue(node.translateYProperty(), 0))
        );
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }

    // Title bar buttons animation
    protected void upTransition(Node node) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(.15), new KeyValue(node.translateYProperty(), -5))
        );
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }

    protected void downTransition(Node node) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.translateYProperty(), -5)),
                new KeyFrame(Duration.seconds(.15), new KeyValue(node.translateYProperty(), 0))
        );
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }

    protected void inTransition(Node node) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(.1), new KeyValue(node.opacityProperty(), .3))
        );
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }

    protected void outTransition(Node node) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.opacityProperty(), .3)),
                new KeyFrame(Duration.seconds(.1), new KeyValue(node.opacityProperty(), 1))
        );
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }


    // converts string with spaces to a URL form with plues instead
    private String spacesToPluses(String rawString) {
        if (rawString.indexOf(' ') != -1) {
            return rawString.replace(' ', '+');
        }
        return rawString;
    }

    protected void createErrorAlert(String text, String title, String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/images/weather-trayicon-pure-black.png"))));
        Image errorGraphic = new Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/images/error-icon-pure-black.png")));
        ImageView errorGraphicViewer = new ImageView(errorGraphic);
        errorGraphicViewer.setPreserveRatio(true);
        errorGraphicViewer.setFitWidth(50);
        alert.setGraphic(errorGraphicViewer);
        alert.show();
    }

    private void writeDefaultData(Location location) {
        try (BufferedWriter bw = new BufferedWriter(new PrintWriter("default.txt"))) {
            bw.write(location.getName() + "\n" + location.getLatitude() + "\n" + location.getLongitude() + "\n" + isPressureInhPa + "\n" + setToKeepInTray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // throwing connection alert thing
    private void callConnectionAlert() {
        createErrorAlert("Please try again later.", "Something went wrong", "The connection could not be established.");
    }

    // searches location by the entered location
    @FXML
    protected void searchLocation() throws IOException, URISyntaxException {
        String inputText = searchBarTextField.getText().trim();
        if (!inputText.isEmpty()) {
            Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            final URL requestURL = new URI("https://nominatim.openstreetmap.org/search?addressDetails=1&accept-language=en-US&format=json&limit=1&q=" + spacesToPluses(inputText)).toURL();
            HttpsURLConnection connection = (HttpsURLConnection) requestURL.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                StringBuilder jsonResponse = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }

                // getting rid of [ and ] :P
                jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                jsonResponse.deleteCharAt(0);
                try {
                    currentLocation = GSON.fromJson(jsonResponse.toString(), Location.class);
                    currentLocation.minimizeName();
                    writeDefaultData(currentLocation);
                    currentLocationLabel.setText("Right now in\n" + currentLocation.getName());

                    Weather weatherData = Objects.requireNonNull(requestWeatherData(currentLocation));
                    setWeatherData(weatherData);
                    selectDay(0);
                } catch (Exception e) {
                    searchBarTextField.clear();
                    searchBarTextField.requestFocus();
                    createErrorAlert("Try entering another one", "Error.", "Entered location doesn't exist!");
                }
            } else {
                callConnectionAlert();
            }
        }
    }
}