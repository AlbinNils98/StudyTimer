package se.studytimer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class StudyTimer extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(StudyTimer.class.getResource("StudyTimer.fxml"));
    StackPane page = (StackPane) loader.load();
    Scene scene = new Scene(page);


    primaryStage.setTitle("Study Timer");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}