module org.example.studytimer {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;


  opens se.studytimer to javafx.fxml;
  exports se.studytimer;
}