module com.example.blackjack {
    requires javafx.controls;
    requires javafx.fxml;

    // Abrir y exportar el paquete donde esta la GUI
    exports com.example.blackjack.Vista;
    opens com.example.blackjack.Vista to javafx.graphics, javafx.fxml;

    // Exportar el resto de paquetes
    exports com.example.blackjack.Controlador;
    exports com.example.blackjack.Modelo;
    exports com.example.blackjack;
}