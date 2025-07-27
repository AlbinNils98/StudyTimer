package se.studytimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class timerController {


  @FXML
  private Label header;
  @FXML
  private ChoiceBox<Integer> breakTimeChoice;
  @FXML
  private ChoiceBox<Integer> frequencyChoice;
  @FXML
  private ChoiceBox<LocalTime> endTimeChoice;
  @FXML
  private Button startBtn;
  @FXML
  private Button cancelBtn;
  @FXML
  private VBox timerOptions;

  private Timeline timeline;
  private LocalTime lastAlertTime;
  private LocalDateTime endDateTime;
  private boolean isOnBreak = false;
  private java.time.Duration breakDuration;

  @FXML
  private void initialize() {
    header.setVisible(false);
    cancelBtn.setVisible(false);

    ObservableList<Integer> breakTimes = FXCollections.observableArrayList();

    for (int i = 5; i <= 60; i += 5) {
      breakTimes.add(i);
    }

    breakTimeChoice.setItems(FXCollections.observableArrayList(breakTimes));
    breakTimeChoice.getSelectionModel().selectFirst();

    ObservableList<Integer> frequencyTimes = FXCollections.observableArrayList();

    for (int i = 30; i <= 240; i += 30) {
      frequencyTimes.add(i);
    }

    frequencyChoice.setItems(FXCollections.observableArrayList(frequencyTimes));
    frequencyChoice.getSelectionModel().selectFirst();

    endTimeChoice.setItems(getFutureHours());
    endTimeChoice.getSelectionModel().selectFirst();

    startBtn.setOnAction(event -> startTimer());
  }

  public void startTimer(){
    if (timeline != null) {
      timeline.stop();
    }

    Integer frequency = frequencyChoice.getValue();
    LocalTime endTime = endTimeChoice.getValue();
    Integer breakMinutes = breakTimeChoice.getValue();
    breakDuration = java.time.Duration.ofMinutes(breakMinutes);

    timerOptionsVisability(false, frequency);


    lastAlertTime = LocalTime.now();

    LocalTime now = LocalTime.now();

    if (endTime.isBefore(now)){
      endDateTime = LocalDateTime.now().plusDays(1).with(endTime);
    }else {
      endDateTime = LocalDateTime.now().with(endTime);
    }

    cancelBtn.setOnAction(event -> {
      if (timeline != null) {
        timeline.stop();
      }
      timerOptionsVisability(true, frequency);
    });

    timeline = new Timeline(new KeyFrame(Duration.seconds(1), eventAction -> {
      if (LocalDateTime.now().isAfter(endDateTime)) {
        timeline.stop();
       timerOptionsVisability(true, frequency);
        return;
      }

      if (!isOnBreak && LocalTime.now().isAfter(lastAlertTime.plusMinutes(frequency))) {
        alertWindow("Time for a break!");
        lastAlertTime = LocalTime.now();
        isOnBreak = true;
      } else if (isOnBreak && LocalTime.now().isAfter(lastAlertTime.plus(breakDuration))) {
        alertWindow("Back to studying!");
        lastAlertTime = LocalTime.now();
        isOnBreak = false;
      }
    }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

  }

  private void alertWindow(String message) {
    Platform.runLater(() -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Timer");
      alert.setHeaderText(null);
      alert.setContentText(message);

      // Get the current window from any control, e.g. your root or any node:
      javafx.stage.Window owner = startBtn.getScene().getWindow();
      alert.initOwner(owner);  // Set owner window to keep alert in front

      Toolkit.getDefaultToolkit().beep();
      alert.showAndWait();
    });
  }

  private void timerOptionsVisability(boolean bool, int frequency){
    header.setVisible(!bool);
    header.setText("The timer is active. You will be alerted every " + frequency + " minutes.");
    timerOptions.setVisible(bool);
    startBtn.setVisible(bool);
    cancelBtn.setVisible(!bool);
  }

  private ObservableList<LocalTime> getFutureHours(){
    LocalTime currentTime = LocalTime.now();
    ObservableList<LocalTime> comingHours = FXCollections.observableArrayList();
    for (int i = 1; i <= 8; i++) {
      LocalTime nextTime = currentTime.plusHours(i);

      nextTime = nextTime.truncatedTo(ChronoUnit.HOURS);

      comingHours.add(nextTime);
    }
    return comingHours;
  }
}