package se.studytimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class timerController {


  @FXML
  private Label header;
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

  @FXML
  private void initialize() {
    cancelBtn.setVisible(false);
    frequencyChoice.setItems(FXCollections.observableArrayList(1, 30, 60, 120));
    frequencyChoice.getSelectionModel().selectFirst();

    endTimeChoice.setItems(getFutureHours());
    endTimeChoice.getSelectionModel().selectFirst();

    startBtn.setText("Start timer");
    startBtn.setOnAction(event -> startTimer());
  }

  public void startTimer(){
    Integer frequency = frequencyChoice.getValue();
    LocalTime endTime = endTimeChoice.getValue();

    header.setText("The timer is active. You will be alerted every " + frequency + " minutes");
    timerOptions.setVisible(false);
    cancelBtn.setVisible(true);


    lastAlertTime = LocalTime.now();

    LocalTime now = LocalTime.now();
    if (endTime.isBefore(now)){
      endDateTime = LocalDateTime.now().plusDays(1).with(endTime);
    }else {
      endDateTime = LocalDateTime.now().with(endTime);
    }

    timeline = new Timeline(new KeyFrame(Duration.seconds(1), eventAction -> {
      cancelBtn.setOnAction(event -> {
        timeline.stop();
        header.setText("Study Timer");
        timerOptions.setVisible(true);
        cancelBtn.setVisible(false);
      });
      if (LocalDateTime.now().isAfter(endDateTime)) {
        timeline.stop();
        header.setText("Study Timer");
        timerOptions.setVisible(true);
        cancelBtn.setVisible(false);
        return;
      }

      if (LocalTime.now().isAfter(lastAlertTime.plusMinutes(frequency))) {
        alertWindow();
        lastAlertTime = LocalTime.now();
      }
    }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

  }

  private void alertWindow(){
    var alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Timer");
    alert.setHeaderText(null);
    alert.setContentText("Time for a break!");

    alert.show();
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