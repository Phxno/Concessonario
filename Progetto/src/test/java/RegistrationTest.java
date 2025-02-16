import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uni.progetto.homepage.*;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationControllerTest {
    private RegistrationController controller;

    @BeforeEach
    void setUp() {
        controller = new RegistrationController();//crea un'istanza di RegistrationController per testare i suoi metodi
    }

    @Test
    void testControlloCampi() { // Simula il comportamento con campi vuoti
        assertTrue(controller.controllo_campi("", "Rossi", "1234567890", "2000-01-01", "user", "Pass1!"));//nome vuoto
        assertFalse(controller.controllo_campi("Mario", "Rossi", "1234567890", "2000-01-01", "user", "Pass1!"));//nome non vuoto
    }

    @Test
    void testControllaNumero() {// Simula il comportamento con numero di telefono non valido
        assertTrue(controller.controlla_numero("1234567890"));//numero di telefono valido
        assertFalse(controller.controlla_numero("12345"));//numero di telefono non valido
        assertFalse(controller.controlla_numero("123456789a"));
    }

    @Test
    void testControllaPassword() {// Simula il comportamento con password non valida
        assertTrue(controller.controlla_password("Pass1!"));//password valida
        assertFalse(controller.controlla_password("pass"));//password non valida (minore di 5 caratteri)
        assertFalse(controller.controlla_password("PASSWORD1!"));//password non valida (tutti i caratteri maiuscoli)
        assertFalse(controller.controlla_password("Pass!"));
    }

    @Test
    void testDifferentPassword() {// Simula il comportamento con password diverse
        assertTrue(controller.different_password("Pass1!", "Pass1!"));
        assertFalse(controller.different_password("Pass1!", "pass2!"));
    }

    @Test
    void testCheckEmail() {// Simula il comportamento con email non valida
        assertTrue(controller.check_email("test@example.com"));
        assertFalse(controller.check_email("test@com")); //email non valida (mancanza dominio)
        assertFalse(controller.check_email("testexample.com"));
    }

    @Test
    void testCheckIfUserIsPresent() {// Simula il comportamento con utente non presente
        assertFalse(controller.checkIfUserIsPresent("utenteNonEsistente"));
    }
}
