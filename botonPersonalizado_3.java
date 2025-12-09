import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.stream.Collectors;

public class botonPersonalizado_3 implements ActionListener, PropertyChangeListener {

    private static final Logger logger = LogManager.getLogger(botonPersonalizado_3.class);

    private final JPanel panelT;
    private final JTextArea resultadoArea;
    private final ListaObservable lista;

    public botonPersonalizado_3(JPanel panelT) {
        this.panelT = panelT;
        this.lista = new ListaObservable();

        this.resultadoArea = new JTextArea();
        this.resultadoArea.setEditable(false);
        this.resultadoArea.setBounds(20, 20, 350, 240);
        this.resultadoArea.setBackground(new java.awt.Color(240, 250, 255));
        panelT.add(resultadoArea);

        this.lista.addPropertyChangeListener(ListaObservable.PROP_NUMEROS, this);
        this.lista.addPropertyChangeListener(this);
    }

    public ListaObservable getLista() {
        return lista;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ListaObservable.PROP_MENSAJE.equals(evt.getPropertyName())) {
            logger.info("[Observer] {}", evt.getNewValue());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String opcion = e.getActionCommand();

        switch (opcion) {
            case "MENOR A MAYOR" -> {
                mostrarResultado("Menor a Mayor:\n" + 
                    lista.getNumerosAsInt().stream()
                        .sorted()
                        .collect(Collectors.toList()));
            }
            case "MAYOR A MENOR" -> {
                mostrarResultado("Mayor a Menor:\n" + 
                    lista.getNumerosAsInt().stream()
                        .sorted((a, b) -> b.compareTo(a))
                        .collect(Collectors.toList()));
            }
            case "PRIMOS" -> {
                List<Integer> primos = lista.getNumerosAsInt().stream()
                    .filter(this::esPrimo)
                    .collect(Collectors.toList());
                mostrarResultado("Primos:\n" + primos);
            }
            case "PARES" -> {
                List<Integer> pares = lista.getNumerosAsInt().stream()
                    .filter(n -> n % 2 == 0)
                    .collect(Collectors.toList());
                mostrarResultado("Pares:\n" + pares);
            }
            case "IMPARES" -> {
                List<Integer> impares = lista.getNumerosAsInt().stream()
                    .filter(n -> n % 2 != 0)
                    .collect(Collectors.toList());
                mostrarResultado("Impares:\n" + impares);
            }
            case "MULTIPLOS" -> {
                String in = JOptionPane.showInputDialog("Múltiplos de:");
                if (in != null && !in.trim().isEmpty()) {
                    try {
                        int n = Integer.parseInt(in.trim());
                        List<Integer> mult = lista.getNumerosAsInt().stream()
                            .filter(x -> x % n == 0)
                            .collect(Collectors.toList());
                        mostrarResultado("Múltiplos de " + n + ":\n" + mult);
                    } catch (Exception ignored) {
                        mostrarResultado("Entrada inválida");
                    }
                }
            }
        }
    }

    private void mostrarResultado(String texto) {
        resultadoArea.setText(texto);
    }

    private boolean esPrimo(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public void limpiar() {
        lista.clear();
        resultadoArea.setText("");
    }
}