module com.example.git {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;

    opens com.example.git to javafx.fxml;
    exports com.example.git;
    exports com.example.git.transports;
    opens com.example.git.transports to javafx.fxml;
    exports com.example.git.management;
    opens com.example.git.management to javafx.fxml;
    exports com.example.git.AI;
    opens com.example.git.AI to javafx.fxml;
    opens com.example.git.server to javafx.fxml;
}
