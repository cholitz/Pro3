import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class Persona {
    private int id;
    private String nombre;
    private Date fechanacimiento;
    private Float salario;
    private boolean habilitado;
    
    private PropertyChangeSupport changeSupport;
    
    public Persona() {
        this.changeSupport = new PropertyChangeSupport(this);
    }
    
    public Persona(int id, String nombre, Date fechanacimiento, Float salario, boolean habilitado) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.fechanacimiento = fechanacimiento;
        this.salario = salario;
        this.habilitado = habilitado;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        int oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        String oldNombre = this.nombre;
        this.nombre = nombre;
        changeSupport.firePropertyChange("nombre", oldNombre, nombre);
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        Date oldFecha = this.fechanacimiento;
        this.fechanacimiento = fechanacimiento;
        changeSupport.firePropertyChange("fechanacimiento", oldFecha, fechanacimiento);
    }

    public Float getSalario() {
        return salario;
    }

    public void setSalario(Float salario) {
        Float oldSalario = this.salario;
        this.salario = salario;
        changeSupport.firePropertyChange("salario", oldSalario, salario);
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        boolean oldHabilitado = this.habilitado;
        this.habilitado = habilitado;
        changeSupport.firePropertyChange("habilitado", oldHabilitado, habilitado);
    }
    
    // Observer Pattern
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechanacimiento=" + fechanacimiento +
                ", salario=" + salario +
                ", habilitado=" + habilitado +
                '}';
    }
}