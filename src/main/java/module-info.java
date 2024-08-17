module org.example.hexgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens edu.erciyes.FX to javafx.graphics;
    opens org.example.hexgame to javafx.fxml;
    exports org.example.hexgame;
}