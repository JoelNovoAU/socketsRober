module com.example.socketsrober {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.socketsrober to javafx.fxml;
    exports com.example.socketsrober;
}