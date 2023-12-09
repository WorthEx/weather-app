package com.example.weatherapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class WeatherApp extends Application {
    public static final String appName = "Kumorika";
    static AppController controller;
    final Screen screen = Screen.getPrimary();
    final double SCREEN_WIDTH = screen.getBounds().getWidth();
    final double SCREEN_HEIGHT = screen.getBounds().getHeight();
    static double WINDOW_WIDTH;
    static double WINDOW_HEIGHT;

    @Override
    public void start(Stage stage) throws IOException, AWTException {
//        if (SCREEN_WIDTH < 2560.0 && SCREEN_HEIGHT < 1440.0) {
//            WINDOW_WIDTH = 1125;
//            WINDOW_HEIGHT = 750;
//        } else {
//            WINDOW_WIDTH = 1500;
//            WINDOW_HEIGHT = 1000;
//        }
        WINDOW_WIDTH = 1200;
        WINDOW_HEIGHT = 800;
        System.setProperty("prism.lcdtext", "false");
        // applying the FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppFXML.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        controller = fxmlLoader.getController();

        // Getting FXML objects
        AnchorPane MainAnchorPane = (AnchorPane) scene.lookup("#MainAnchorPane");
        javafx.scene.control.TextField searchBarTextField = (javafx.scene.control.TextField) scene.lookup("#searchBarTextField");
        HBox CurrentDailyHBox = controller.CurrentDailyHBox;

        // applying the CSS
        String cssFile = Objects.requireNonNull(getClass().getResource("AppCSS.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);

        // customizing the stage and main scene
        javafx.scene.image.Image icon = new javafx.scene.image.Image(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/images/weather-icon-blue-2.png")));
        stage.getIcons().add(icon);
        stage.setResizable(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCodeCombination.NO_MATCH);
        stage.setTitle(appName);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        MainAnchorPane.setBackground(Background.fill(Color.TRANSPARENT));
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        // Creating tray thingies
        Platform.setImplicitExit(false);
        createTrayIcon(stage);

        // setting up key events
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F && !searchBarTextField.isFocused()) {
                controller.switchFullScreen();
            } else if (e.getCode() == KeyCode.ESCAPE && searchBarTextField.isFocused()) {
                searchBarTextField.clear();
                MainAnchorPane.requestFocus();
            } else if (e.getCode() == KeyCode.ENTER && searchBarTextField.isFocused()) {
                try {
                    controller.searchLocation();
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                MainAnchorPane.requestFocus();
            } else if (e.getCode() == KeyCode.ENTER && !searchBarTextField.isFocused()) {
                searchBarTextField.clear();
                searchBarTextField.requestFocus();
            }
        });
        CurrentDailyHBox.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !stage.isFullScreen() && !controller.isExpanded) {
                controller.expand();
            } else if (mouseEvent.getClickCount() == 2 && !stage.isFullScreen() && controller.isExpanded) {
                controller.shrink();
            }
        });
        stage.show();
    }

    // Reverting background image to window
    private static void revertBackground(Scene scene) {
        ImageView clouds = (ImageView) scene.lookup("#clouds");
        clouds.setFitWidth(WINDOW_WIDTH);
        Rectangle clipRect = controller.clipRect;
        clipRect.setArcWidth(20);
        clipRect.setArcHeight(20);
    }

    // Converting background image to fullscreen
    private static void convertBackground(Scene scene) {
        final Screen screen = Screen.getPrimary();
        ImageView clouds = (ImageView) scene.lookup("#clouds");
        clouds.setFitWidth(screen.getBounds().getWidth());
        Rectangle clipRect = controller.clipRect;
        clipRect.setArcWidth(0);
        clipRect.setArcHeight(0);
    }


    // returns tray icon and adds a popup menu on click events
    private TrayIcon getTrayIcon(Stage stage, java.awt.Image trayIconImage) {
        AnchorPane MainAnchorPane = controller.MainAnchorPane;
        ActionListener closeListener = e -> System.exit(0);
        ActionListener showListener = e -> Platform.runLater(() -> {
            stage.show();
            MainAnchorPane.requestFocus();
        });

        // Creating a popup
        PopupMenu popup = new PopupMenu();

        MenuItem showItem = new MenuItem("Show");
        showItem.addActionListener(showListener);
        popup.add(showItem);

        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(closeListener);
        popup.add(closeItem);

        TrayIcon trayIcon = new TrayIcon(trayIconImage, appName, popup);
        trayIcon.addActionListener(showListener);
        return trayIcon;

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createTrayIcon(Stage stage) throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(WeatherApp.class.getResourceAsStream("/images/weather-trayicon-pure-white.png")));
                stage.setOnCloseRequest(e -> stage.hide());
                TrayIcon trayIcon = getTrayIcon(stage, image);
                tray.add(trayIcon);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } else {
            Platform.setImplicitExit(true);
        }
    }

    static void switchFullScreen(Stage stage) {
        if (stage.isFullScreen()) {
            revertBackground(stage.getScene());
            stage.setFullScreen(false);
            stage.centerOnScreen();
        } else {
            convertBackground(stage.getScene());
            stage.setFullScreen(true);
        }
    }
}