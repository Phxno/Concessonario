import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uni.progetto.homepage.*;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationControllerTest {
    private RegistrationController controller;

    @BeforeEach
    void setUp() {
        controller = new RegistrationController();
    }

    @Test
    void testControlloCampi() {
        assertTrue(controller.controllo_campi("", "Rossi", "1234567890", "2000-01-01", "user", "Pass1!"));
        assertFalse(controller.controllo_campi("Mario", "Rossi", "1234567890", "2000-01-01", "user", "Pass1!"));
    }

    @Test
    void testControllaNumero() {
        assertTrue(controller.controlla_numero("1234567890"));
        assertFalse(controller.controlla_numero("12345"));
        assertFalse(controller.controlla_numero("123456789a"));
    }

    @Test
    void testControllaPassword() {
        assertTrue(controller.controlla_password("Pass1!"));
        assertFalse(controller.controlla_password("pass"));
        assertFalse(controller.controlla_password("PASSWORD1!"));
        assertFalse(controller.controlla_password("Pass!"));
    }

    @Test
    void testDifferentPassword() {
        assertTrue(controller.different_password("Pass1!", "Pass1!"));
        assertFalse(controller.different_password("Pass1!", "pass2!"));
    }

    @Test
    void testCheckEmail() {
        assertTrue(controller.check_email("test@example.com"));
        assertFalse(controller.check_email("test@com"));
        assertFalse(controller.check_email("testexample.com"));
    }

    @Test
    void testCheckIfUserIsPresent() {
        assertFalse(controller.checkIfUserIsPresent("utenteNonEsistente"));
    }
}
