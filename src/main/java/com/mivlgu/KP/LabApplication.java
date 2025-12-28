package com.mivlgu.KP;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;



public class LabApplication extends Application {

    private static Stage primaryStage;
    private static Properties property;
    private static final Logger logger =
            LoggerFactory.getLogger(LabApplication.class);
    public static Locale currentLocale;
    public static ResourceBundle bundle;

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Запуск приложения");
        currentLocale = new Locale("ru");
        loadBundle();
        loadProperties();
        dbCon.initDatabase();
        this.primaryStage = stage;
        loadMainView();
        logger.info("Главное окно инициализировано");
    }
    @Override
    public void stop() throws Exception {
        dbCon.stopConnection();
        super.stop();
    }

    public static void loadMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LabApplication.class.getResource("lab-view.fxml"),
                bundle
        );
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("KP!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadBundle(){
        bundle = ResourceBundle.getBundle(
                "com.mivlgu.KP.statements",
                currentLocale
        );
    }

    public void loadProperties() {
        property = new Properties();
        try (InputStream is = getClass().getResourceAsStream(
                "/com/mivlgu/KP/statements.properties")) {
            if (is == null) {
                throw new RuntimeException("Файл statements.properties не найден");
            }
            property.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    public static Properties getProperty() { return property; }
}
