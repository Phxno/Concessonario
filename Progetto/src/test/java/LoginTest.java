import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uni.progetto.homepage.HomepageController;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {
    private HomepageController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});//inizializza il toolkit di JavaFX che ci permette di testare le classi che estendono Application senza dover creare un'istanza di queste classi
    }


    @BeforeEach
    void setUp() {
        controller = new HomepageController();//crea un'istanza di HomepageController per testare i suoi metodi
    }


    @Test
    void testControlloCampiVuoti() {
        // Simula il comportamento con campi vuoti
        assertTrue(controller.campi_vuoti("", ""));
        assertFalse(controller.campi_vuoti("ciao", "ciao"));
    }



    @Test
    void testCampiCorretti() {
        // Simula il comportamento con campi corretti
        assertFalse(controller.campi_corretti("hacker","Hack1!", "hacker","hack1!"));//username e password diversi
        assertFalse(controller.campi_corretti("Sbagliato","sbagliato","giusto","giusto"));//
        assertFalse(controller.campi_corretti("sbagliato","Sbagliato","SBAGLIATO","Sbagliato"));
        assertTrue(controller.campi_corretti("Elvis", "Elvis", "Elvis03!", "Elvis03!"));//username e password corretti


    }
}