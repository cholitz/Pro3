import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PersonaFrame extends JFrame {
    private PersonaDao personaDao;
    private DefaultListModel<Persona> listModel;
    private JList<Persona> personaList;
    
    public PersonaFrame() {
        personaDao = new PersonaDao();
        initUI();
        cargarPersonas();
    }
    
    private void initUI() {
        setTitle("ABM de Personas - Hibernate");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel superior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JButton btnNuevo = new JButton("Nueva Persona");
        JButton btnActualizar = new JButton("Actualizar Lista");
        
        btnNuevo.addActionListener(e -> mostrarDialogoPersona(null));
        btnActualizar.addActionListener(e -> cargarPersonas());
        
        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        
        // Lista de personas
        listModel = new DefaultListModel<>();
        personaList = new JList<>(listModel);
        personaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personaList.setCellRenderer(new PersonaCellRenderer());
        
        // Detectar clics en la lista (para botones Editar/Eliminar)
        personaList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = personaList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Persona persona = listModel.getElementAt(index);
                    Rectangle cellBounds = personaList.getCellBounds(index, index);
                    
                    // Calcular posición de los botones (derecha de la celda)
                    int btnWidth = 80;
                    int btnHeight = 25;
                    int startX = cellBounds.x + cellBounds.width - (btnWidth * 2 + 20);
                    
                    // Verificar si se hizo clic en el botón Editar
                    if (e.getX() >= startX && e.getX() <= startX + btnWidth && 
                        e.getY() >= cellBounds.y && e.getY() <= cellBounds.y + btnHeight) {
                        mostrarDialogoPersona(persona);
                    }
                    
                    // Verificar si se hizo clic en el botón Eliminar
                    else if (e.getX() >= startX + btnWidth + 10 && e.getX() <= startX + btnWidth * 2 + 10 &&
                             e.getY() >= cellBounds.y && e.getY() <= cellBounds.y + btnHeight) {
                        eliminarPersona(persona);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(personaList);
        
        add(panelBotones, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Cerrar conexión al salir
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Conexion.getInstancia().cerrarConexion();
            }
        });
    }
    
    private void cargarPersonas() {
        SwingWorker<List<Persona>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Persona> doInBackground() {
                return personaDao.getAll();
            }
            
            @Override
            protected void done() {
                try {
                    List<Persona> personas = get();
                    listModel.clear();
                    for (Persona persona : personas) {
                        listModel.addElement(persona);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PersonaFrame.this, 
                        "Error al cargar personas: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void mostrarDialogoPersona(Persona persona) {
        PersonaDialog dialog = new PersonaDialog(this, persona, personaDao);
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarPersonas();
        }
    }
    
    private void eliminarPersona(Persona persona) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar a " + persona.getNombre() + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (personaDao.delete(persona.getId())) {
                cargarPersonas();
                JOptionPane.showMessageDialog(this, 
                    "Persona eliminada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar la persona", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Renderer personalizado para mostrar botones en cada item
    private class PersonaCellRenderer extends JPanel implements ListCellRenderer<Persona> {
        private JLabel lblInfo;
        
        public PersonaCellRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            lblInfo = new JLabel();
            lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            JButton btnEditar = new JButton("Editar");
            JButton btnEliminar = new JButton("Eliminar");
            
            // Los botones son solo visuales, la funcionalidad está en el MouseListener
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            
            panelBotones.add(btnEditar);
            panelBotones.add(btnEliminar);
            
            add(lblInfo, BorderLayout.CENTER);
            add(panelBotones, BorderLayout.EAST);
        }
        
        @Override
        public Component getListCellRendererComponent(JList<? extends Persona> list, 
                                                     Persona persona, 
                                                     int index, 
                                                     boolean isSelected, 
                                                     boolean cellHasFocus) {
            
            String texto = String.format("<html><b>%s</b> (ID: %d)<br>Salario: $%.2f | %s</html>",
                persona.getNombre(),
                persona.getId(),
                persona.getSalario(),
                persona.isHabilitado() ? "Habilitado" : "No habilitado"
            );
            lblInfo.setText(texto);
            
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            
            return this;
        }
    }
}