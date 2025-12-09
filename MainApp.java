import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp {
    private static final Logger logger = LogManager.getLogger(MainApp.class);
    
    public static void main(String[] args) {
        logger.info("=== INICIANDO APLICACIÓN HIBERNATE ===");
        
        // Configurar para Linux
        System.setProperty("jdk.gtk.version", "2");
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("Error configurando Look and Feel: {}", e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            PersonaFrame frame = new PersonaFrame();
            frame.setVisible(true);
        });
    }
}