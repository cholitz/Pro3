import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ListaObservable {
    private final List<String> numeros = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String PROP_NUMEROS = "numeros";
    public static final String PROP_MENSAJE = "mensaje";

    public void addNumero(String numero) {
        int oldSize = numeros.size();
        numeros.add(numero);
        support.firePropertyChange(PROP_NUMEROS, oldSize, numeros.size());
        fireMensaje("Agregado: " + numero);
    }

    public void clear() {
        numeros.clear();
        support.firePropertyChange(PROP_NUMEROS, -1, 0);
        fireMensaje("Lista limpiada");
    }

    public List<String> getNumeros() {
        return new ArrayList<>(numeros);
    }

    public List<Integer> getNumerosAsInt() {
        List<Integer> result = new ArrayList<>();
        for (String s : numeros) {
            try { result.add(Integer.parseInt(s)); } catch (Exception ignored) {}
        }
        return result;
    }

    public int size() { return numeros.size(); }

    public void fireMensaje(String msg) {
        support.firePropertyChange(PROP_MENSAJE, null, msg);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}