module com.example.git {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.git to javafx.fxml;
    exports com.example.git;
}