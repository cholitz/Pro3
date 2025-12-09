import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PanelNiveles extends JPanel {
    private final JuegoNiveles juego;
    private final List<NumeroEntidad> entidades = new ArrayList<>();
    private final Set<NumeroEntidad> seleccionados = new HashSet<>();
    private String categoriaActual = "";

    public PanelNiveles(JuegoNiveles juego) {
        this.juego = juego;
        setBackground(new Color(10, 10, 40));
        setLayout(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                NumeroEntidad clicado = null;
                for (NumeroEntidad ent : new ArrayList<>(entidades)) {
                    if (ent.getBounds().contains(e.getPoint())) {
                        clicado = ent;
                        break;
                    }
                }
                if (clicado == null) return;

                if (seleccionados.contains(clicado)) {
                    seleccionados.remove(clicado);
                    clicado.flashNormal();
                } else {
                    if (!formariaExplosion(clicado)) {
                        for (NumeroEntidad viejo : seleccionados) {
                            viejo.flashNormal();
                        }
                        seleccionados.clear();
                    }
                    seleccionados.add(clicado);
                    clicado.flash();
                }
                chequearExplosion();
            }
        });
    }

    public void nuevoNivel(String categoria, int cantidad) {
        entidades.clear();
        removeAll();
        seleccionados.clear();
        categoriaActual = categoria;

        int cols = 5;
        int spacingX = 240;
        int spacingY = 180;
        int startX = 100;
        int startY = 120;

        for (int i = 0; i < cantidad && i < 15; i++) {
            int valor = (int) (Math.random() * 199) + 1;
            NumeroEntidad ent = new NumeroEntidad(valor);

            int col = i % cols;
            int row = i / cols;
            int x = startX + col * spacingX;
            int y = startY + row * spacingY;

            ent.setLocation(x, y);
            entidades.add(ent);
            add(ent);
        }

        revalidate();
        repaint();
        juego.actualizarEstadisticas();
    }

    private boolean formariaExplosion(NumeroEntidad nuevo) {
        List<Integer> prueba = new ArrayList<>();
        for (NumeroEntidad s : seleccionados) prueba.add(s.getValor());
        prueba.add(nuevo.getValor());
        if (prueba.size() < 2) return false;
        return esCombinacionProhibida(prueba);
    }

    private void chequearExplosion() {
        if (seleccionados.size() < 2) return;

        List<Integer> valores = new ArrayList<>();
        for (NumeroEntidad s : seleccionados) valores.add(s.getValor());

        if (esCombinacionProhibida(valores)) {
            int eliminados = seleccionados.size();
            for (NumeroEntidad ent : new ArrayList<>(seleccionados)) {
                ent.explotar(this);
                entidades.remove(ent);
            }
            seleccionados.clear();
            juego.impostorDescubierto(eliminados);
        }
    }

    // AQUÍ ESTÁ LA MAGIA: PARES, IMPARES, PRIMOS → TODO FUNCIONA PERFECTO
    private boolean esCombinacionProhibida(List<Integer> nums) {
        return switch (categoriaActual) {
            case "PRIMOS"           -> nums.stream().allMatch(n -> n > 1 && esPrimo(n));
            case "PARES"            -> nums.stream().allMatch(n -> n % 2 == 0);           // EXPLOTA SI TODOS SON PARES
            case "IMPARES"          -> nums.stream().allMatch(n -> n % 2 != 0);          // EXPLOTA SI TODOS SON IMPARES
            case "MÚLTIPLOS DE 5"   -> nums.stream().allMatch(n -> n % 5 == 0);
            case "MÚLTIPLOS DE 7"   -> nums.stream().allMatch(n -> n % 7 == 0);
            default                 -> false;
        };
    }

    private boolean esPrimo(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        juego.actualizarEstadisticas(); // Solo actualiza el contador, nada más
    }

    public int contarNumeros() {
        return entidades.size();
    }
}