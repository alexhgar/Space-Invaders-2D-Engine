module alexjulenerik.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens alexjulenerik.demo to javafx.fxml;
    exports alexjulenerik.demo;
    opens alexjulenerik.demo.controller to javafx.fxml;
    exports alexjulenerik.demo.controller to javafx.fxml;
}