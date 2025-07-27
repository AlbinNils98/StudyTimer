package se.studytimer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class StudyTimer extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(StudyTimer.class.getResource("fxml/StudyTimer.fxml"));
    StackPane page = (StackPane) loader.load();
    Scene scene = new Scene(page);

    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/application.css")).toExternalForm());


    primaryStage.setTitle("Study Timer");
    primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/icon.png"))));
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}