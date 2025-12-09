import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;

public class ventana extends JFrame {

    class Fondo extends JPanel {
        private Image imagen;
        public Fondo(String ruta) {
            this.imagen = new ImageIcon(ruta).getImage();
        }
        @Override protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

   /*  private PanelVideojuego panelJuego = null;*/
    private JPanel panelS;
    private JPanel panelT;

    public ventana() {
        setTitle("Ventana");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);  // TU LAYOUT ORIGINAL

        // TU PANEL PRINCIPAL (MetaV.jpg)
        JPanel panelP = new Fondo("MetaV.jpg");
        panelP.setBounds(0, 0, 500, 800);
        panelP.setLayout(null);
        add(panelP);

        // TU BOTÓN INVISIBLE QUE FUNCIONA
        JButton botonInicio = new JButton();
        botonInicio.setBounds(230, 180, 80, 30);
        botonInicio.setOpaque(false);
        botonInicio.setContentAreaFilled(false);
        botonInicio.setBorderPainted(false);
        panelP.add(botonInicio);

        // TU PANEL SECUNDARIO (Fondo2.gif)
        panelS = new Fondo("Fondo2.gif");
        panelS.setBounds(0, 0, 500, 750);
        panelS.setLayout(null);
        panelS.setVisible(false);
        add(panelS);

        JLabel sticker999 = new JLabel(new ImageIcon("juicesimb-unscreen.gif"));
        sticker999.setBounds(370, 630, 120, 120);
        panelS.add(sticker999);

        panelT = new JPanel();
        panelT.setBackground(Color.WHITE);
        panelT.setBounds(75, 120, 390, 290);
        panelT.setLayout(null);
        panelS.add(panelT);

        botonPersonalizado_3 miBoton3 = new botonPersonalizado_3(panelT);
        ListaObservable lista = miBoton3.getLista();
        
        // TU BOTÓN NUEVO Y ORDENAR
        JButton boton2 = new JButton("Nuevo");
        boton2.setBounds(120, 500, 100, 50);
        panelS.add(boton2);
        boton2.addActionListener(new botonPersonalizado_2(panelS, lista));
        boton2.addActionListener(e -> miBoton3.limpiar());

        JButton boton3 = new JButton("Ordenar");
        boton3.setBounds(280, 500, 100, 50);
        panelS.add(boton3);

        JPopupMenu menuBar = new JPopupMenu();
        String[] ops = {"MULTIPLOS", "MENOR A MAYOR", "MAYOR A MENOR", "PRIMOS", "PARES", "IMPARES"};
        for (String op : ops) {
            JMenuItem item = new JMenuItem(op);
            item.addActionListener(miBoton3);
            menuBar.add(item);
        }
        boton3.addActionListener(e -> menuBar.show(boton3, 0, boton3.getHeight()));

        // TU BOTÓN INVISIBLE FUNCIONA
        botonInicio.addActionListener(e -> {
            panelP.setVisible(false);
            panelS.setVisible(true);
        });

        JButton btnNiveles = new JButton("MODO NIVELES");
        btnNiveles.setBounds(100, 670, 300, 60);
        btnNiveles.setFont(new Font("Arial", Font.BOLD, 20));
        btnNiveles.setBackground(Color.DARK_GRAY);
        btnNiveles.setForeground(Color.ORANGE);
        panelS.add(btnNiveles);
        btnNiveles.addActionListener(e -> new JuegoNiveles().setVisible(true));

        setVisible(true);
    }
}