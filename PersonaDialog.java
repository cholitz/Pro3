import javax.swing.*;
import java.awt.*;

public class PersonaDialog extends JDialog {
    private Persona persona;
    private PersonaDao personaDao;
    private boolean guardado = false;
    
    private JTextField txtNombre;
    private JTextField txtFecha;
    private JTextField txtSalario;
    private JCheckBox chkHabilitado;
    
    public PersonaDialog(JFrame parent, Persona persona, PersonaDao personaDao) {
        super(parent, persona == null ? "Nueva Persona" : "Editar Persona", true);
        this.persona = persona == null ? new Persona() : persona;
        this.personaDao = personaDao;
        
        initUI();
        cargarDatos();
        
        setSize(400, 250);
        setLocationRelativeTo(parent);
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setResizable(false);
        
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        txtNombre = new JTextField(20);
        txtFecha = new JTextField(20);
        txtSalario = new JTextField(20);
        chkHabilitado = new JCheckBox();
        
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panelForm.add(txtFecha);
        panelForm.add(new JLabel("Salario:"));
        panelForm.add(txtSalario);
        panelForm.add(new JLabel("Habilitado:"));
        panelForm.add(chkHabilitado);
        
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void cargarDatos() {
        if (persona.getId() != 0) {
            txtNombre.setText(persona.getNombre());
            txtFecha.setText(persona.getFechanacimiento().toString());
            txtSalario.setText(String.format("%.2f", persona.getSalario()));
            chkHabilitado.setSelected(persona.isHabilitado());
        } else {
            txtFecha.setText("2000-01-01");
            txtSalario.setText("1000.00");
            chkHabilitado.setSelected(true);
        }
    }
    
    private void guardar() {
        try {
            // Usar java.sql.Date explícitamente
            persona.setNombre(txtNombre.getText());
            persona.setFechanacimiento(java.sql.Date.valueOf(txtFecha.getText()));
            persona.setSalario(Float.parseFloat(txtSalario.getText()));
            persona.setHabilitado(chkHabilitado.isSelected());
            
            boolean exito = personaDao.save(persona);
            
            if (exito) {
                guardado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar la persona", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Error en los datos: " + e.getMessage() + 
                "\nFormato fecha: YYYY-MM-DD\nSalario: número decimal", 
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isGuardado() {
        return guardado;
    }
}