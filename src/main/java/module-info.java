module com.mivlgu.KP {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires org.slf4j;


    opens com.mivlgu.KP to javafx.fxml;
    exports com.mivlgu.KP;
}