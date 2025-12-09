import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class botonPersonalizado_2 implements ActionListener {

    private static final Logger logger = LogManager.getLogger(botonPersonalizado_2.class);

    private final JPanel panelb;
    private final ListaObservable lista;

    // Constructor que recibe la lista observable
    public botonPersonalizado_2(JPanel panelb, ListaObservable lista) {
        this.panelb = panelb;
        this.lista = lista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // === Crear los componentes ===
        JTextField barra_de_interaccion = new JTextField();
        barra_de_interaccion.setBounds(150, 10, 200, 30);

        JButton agregar = new JButton("Agregar");
        agregar.setBounds(350, 10, 80, 30);

        // === Lógica del botón Agregar ===
        agregar.addActionListener(ev -> {
            String texto = barra_de_interaccion.getText().trim();

            if (texto.isEmpty()) {
                logger.warn("Intento de agregar número vacío");
                return;
            }

            if (lista.size() >= 20) {
                JOptionPane.showMessageDialog(panelb, "LÍMITE ALCANZADO (20 números)", 
                    "Límite", JOptionPane.WARNING_MESSAGE);
                logger.info("Límite de 20 números alcanzado. Se rechazó: {}", texto);
                barra_de_interaccion.setText("");
                return;
            }

            lista.addNumero(texto);  // Aquí se dispara el PropertyChange automáticamente
            logger.info("Número agregado correctamente: {}", texto);
            logger.debug("Lista actual ({} elementos): {}", lista.size(), lista.getNumeros());

            barra_de_interaccion.setText("");
            barra_de_interaccion.requestFocus();
        });

        // === Solo permitir números en el campo ===
        barra_de_interaccion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();  // Ignora teclas no numéricas
                }
            }
        });

        // === Agregar al panel ===
        panelb.add(barra_de_interaccion);
        panelb.add(agregar);
        panelb.revalidate();
        panelb.repaint();

        logger.info("Panel de ingreso de números activado correctamente");
    }
}