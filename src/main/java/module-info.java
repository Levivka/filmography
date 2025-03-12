module com.example.kinopoisk {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    exports com.example.kinopoisk;
    exports com.example.kinopoisk.Controllers;
    exports com.example.kinopoisk.Entities;
    exports com.example.kinopoisk.DAO;
    opens com.example.kinopoisk to javafx.fxml;
    opens com.example.kinopoisk.Controllers to javafx.fxml;
    opens com.example.kinopoisk.Entities to org.hibernate.orm.core;
    opens com.example.kinopoisk.DAO to javafx.fxml;
}