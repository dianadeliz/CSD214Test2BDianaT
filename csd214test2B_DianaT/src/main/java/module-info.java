module com.trios.csd214test2b_dianat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.trios.csd214test2b_dianat to javafx.fxml;
    exports com.trios.csd214test2b_dianat;
}