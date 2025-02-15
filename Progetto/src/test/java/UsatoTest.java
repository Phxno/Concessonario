import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uni.progetto.homepage.*;

import static org.junit.jupiter.api.Assertions.*;

class UsatoTest {
    private ConfiguratoreController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});//inizializza il toolkit di JavaFX che ci permette di testare le classi che estendono Application senza dover creare un'istanza di queste classi
    }


    @BeforeEach
    void setUp() {
        controller = new ConfiguratoreController();//crea un'istanza di RegistrationController per testare i suoi metodi
    }

    @Test
    void testControlloCampi() { // Simula il comportamento con campi vuoti
        assertTrue(controller.controllaCampi("", "2003", "1323"));//nome vuoto
        assertTrue(controller.controllaCampi("Fiat", "", "234412"));//nome non vuoto
        assertTrue(controller.controllaCampi("Ferrari", "2003", ""));//nome non vuoto
        assertFalse(controller.controllaCampi("Porsche", "2008", "7000"));//nome non vuoto
    }

    @Test
    void testControllaAnno() {// Simula il comportamento con anno non valido
        assertTrue(controller.isValidYear("2003"));//anno valido
        assertFalse(controller.isValidYear("200"));//anno non valido
        assertFalse(controller.isValidYear("200a"));
    }

    @Test
    void testKm() {// Simula il comportamento con km non validi
        assertTrue(controller.isValidKm("12345"));//km validi
        assertFalse(controller.isValidKm("12345a"));//km non validi
        assertFalse(controller.isValidKm("12345.5"));
    }
}