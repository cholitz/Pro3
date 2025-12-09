import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JuegoNiveles extends JFrame {
    private PanelNiveles panelJuego;
    private JLabel lblNivel, lblTiempo, lblCategoria, lblEstadisticas;
    private javax.swing.Timer cronometro;
    private int nivelActual = 1;
    private int tiempoRestante = 40; // más tiempo para disfrutar
    private int impostoresDescubiertos = 0;
    private Random random = new Random();
    private String categoriaProhibida;

    public JuegoNiveles() {
        setTitle("NIVELES ANTIFRAUDE - Descubre Impostores");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel controles = new JPanel(new GridLayout(1, 4));
        controles.setBackground(Color.BLACK);
        controles.setPreferredSize(new Dimension(1400, 80));

        lblNivel = new JLabel("Nivel: 1/5", SwingConstants.CENTER);
        lblNivel.setFont(new Font("Arial", Font.BOLD, 32));
        lblNivel.setForeground(Color.CYAN);
        controles.add(lblNivel);

        lblTiempo = new JLabel("Tiempo: 40s", SwingConstants.CENTER);
        lblTiempo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTiempo.setForeground(Color.YELLOW);
        controles.add(lblTiempo);

        lblCategoria = new JLabel("Prohibido: ???", SwingConstants.CENTER);
        lblCategoria.setFont(new Font("Arial", Font.BOLD, 30));
        lblCategoria.setForeground(Color.RED);
        controles.add(lblCategoria);

        lblEstadisticas = new JLabel("Impostores: 0 | Sobrevivientes: 0", SwingConstants.CENTER);
        lblEstadisticas.setFont(new Font("Arial", Font.BOLD, 28));
        lblEstadisticas.setForeground(Color.GREEN);
        controles.add(lblEstadisticas);

        add(controles, BorderLayout.NORTH);
        panelJuego = new PanelNiveles(this);
        add(panelJuego, BorderLayout.CENTER);

        siguienteNivel();
    }

    public void siguienteNivel() {
        if (nivelActual > 5) { finJuego(); return; }

        String[] categorias = {"PRIMOS", "PARES", "IMPARES", "MÚLTIPLOS DE 5", "MÚLTIPLOS DE 7"};
        categoriaProhibida = categorias[random.nextInt(categorias.length)];

        lblNivel.setText("Nivel: " + nivelActual + "/5");
        lblCategoria.setText("Prohibido: " + categoriaProhibida);
        lblTiempo.setText("Tiempo: 40s");
        tiempoRestante = 40;

        // SOLO 15 NÚMEROS POR NIVEL → JUGABLE
        panelJuego.nuevoNivel(categoriaProhibida, 15);

        if (cronometro != null) cronometro.stop();
        cronometro = new javax.swing.Timer(1000, e -> {
            tiempoRestante--;
            lblTiempo.setText("Tiempo: " + tiempoRestante + "s");
            if (tiempoRestante <= 0) {
                cronometro.stop();
                nivelActual++;
                siguienteNivel();
            }
        });
        cronometro.start();
    }

    public void impostorDescubierto(int cantidad) {
        impostoresDescubiertos += cantidad;
        actualizarEstadisticas();
    }

    public void actualizarEstadisticas() {
        lblEstadisticas.setText("Impostores: " + impostoresDescubiertos + " | Sobrevivientes: " + panelJuego.contarNumeros());
    }

    private void finJuego() {
        if (cronometro != null) cronometro.stop();
        int sobrevivientes = panelJuego.contarNumeros();
     String mensaje = "JUEGO TERMINADO\n\n" +
                        "Impostores descubiertos: " + impostoresDescubiertos + "\n" +
                        "Impostores que escaparon: " + sobrevivientes;

        JOptionPane.showMessageDialog(this, mensaje, "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
    }
}