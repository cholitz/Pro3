import java.sql.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersonaDao {
    private static final Logger logger = LogManager.getLogger(PersonaDao.class);
    
    public Persona get(int id) {
        logger.info("HIBERNATE GET: Buscando persona ID: {}", id);
        
        Conexion conexion = Conexion.getInstancia();
        String query = "SELECT id, nombre, fechanacimiento, salario, habilitado from personas where id = ?";
        Persona resultado = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conexion.getConnection().prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int identifier = rs.getInt("id");
                String n = rs.getString("nombre");
                java.sql.Date fn = rs.getDate("fechanacimiento");
                float s = rs.getFloat("salario");
                boolean h = rs.getBoolean("habilitado");
                resultado = new Persona(identifier, n, fn, s, h);
                
                // Agregar Observer
                resultado.addPropertyChangeListener(evt -> {
                    logger.debug("OBSERVER HIBERNATE: {} cambiado de '{}' a '{}'", 
                               evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                });
                
                logger.info("✅ HIBERNATE: Persona encontrada: {}", resultado.getNombre());
            }
        } catch (SQLException ex) {
            logger.error("❌ Error HIBERNATE GET: {}", ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt);
        }
        return resultado;
    }
    
    public List<Persona> getAll() {
        logger.info("HIBERNATE GET ALL: Obteniendo todas las personas");
        
        Conexion conexion = Conexion.getInstancia();
        String query = "SELECT id, nombre, fechanacimiento, salario, habilitado from personas";
        List<Persona> personas = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conexion.getConnection().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int identifier = rs.getInt("id");
                String n = rs.getString("nombre");
                java.sql.Date fn = rs.getDate("fechanacimiento");
                float s = rs.getFloat("salario");
                boolean h = rs.getBoolean("habilitado");
                Persona persona = new Persona(identifier, n, fn, s, h);
                
                // Agregar Observer a cada persona
                persona.addPropertyChangeListener(evt -> {
                    logger.debug("OBSERVER HIBERNATE [ID: {}]: {} = {} -> {}", 
                               persona.getId(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                });
                
                personas.add(persona);
            }
            logger.info("✅ HIBERNATE: {} personas encontradas", personas.size());
        } catch (SQLException ex) {
            logger.error("❌ Error HIBERNATE GET ALL: {}", ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt);
        }
        return personas;
    }
    
    public boolean save(Persona persona) {
        logger.info("HIBERNATE SAVE: Guardando persona: {}", persona.getNombre());
        
        if (persona.getId() == 0) {
            return insert(persona);
        } else {
            return update(persona);
        }
    }
    
    public boolean insert(Persona persona) {
        logger.info("HIBERNATE INSERT: Insertando nueva persona");
        
        Conexion conexion = Conexion.getInstancia();
        String query = "INSERT INTO personas (nombre, fechanacimiento, salario, habilitado) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = null;
        
        try {
            stmt = conexion.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, persona.getNombre());
            stmt.setDate(2, persona.getFechanacimiento());
            stmt.setFloat(3, persona.getSalario());
            stmt.setBoolean(4, persona.isHabilitado());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        persona.setId(generatedKeys.getInt(1));
                    }
                }
                
                // Agregar Observer
                persona.addPropertyChangeListener(evt -> {
                    logger.debug("OBSERVER HIBERNATE INSERT [ID: {}]: {} = {} -> {}", 
                               persona.getId(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                });
                
                logger.info("✅ HIBERNATE INSERT: Persona insertada - ID: {}", persona.getId());
                return true;
            }
        } catch (SQLException ex) {
            logger.error("❌ Error HIBERNATE INSERT: {}", ex.getMessage());
        } finally {
            cerrarRecursos(null, stmt);
        }
        return false;
    }
    
    public boolean update(Persona persona) {
        logger.info("HIBERNATE UPDATE: Actualizando persona ID: {}", persona.getId());
        
        Conexion conexion = Conexion.getInstancia();
        String query = "UPDATE personas SET nombre = ?, fechanacimiento = ?, salario = ?, habilitado = ? WHERE id = ?";
        PreparedStatement stmt = null;
        
        try {
            stmt = conexion.getConnection().prepareStatement(query);
            stmt.setString(1, persona.getNombre());
            stmt.setDate(2, persona.getFechanacimiento());
            stmt.setFloat(3, persona.getSalario());
            stmt.setBoolean(4, persona.isHabilitado());
            stmt.setInt(5, persona.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("✅ HIBERNATE UPDATE: Persona actualizada - ID: {}", persona.getId());
                return true;
            }
        } catch (SQLException ex) {
            logger.error("❌ Error HIBERNATE UPDATE: {}", ex.getMessage());
        } finally {
            cerrarRecursos(null, stmt);
        }
        return false;
    }
    
    public boolean delete(int id) {
        logger.info("HIBERNATE DELETE: Eliminando persona ID: {}", id);
        
        Conexion conexion = Conexion.getInstancia();
        String query = "DELETE FROM personas WHERE id = ?";
        PreparedStatement stmt = null;
        
        try {
            stmt = conexion.getConnection().prepareStatement(query);
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("✅ HIBERNATE DELETE: Persona eliminada - ID: {}", id);
                return true;
            } else {
                logger.warn("⚠️ HIBERNATE DELETE: Persona no encontrada - ID: {}", id);
            }
        } catch (SQLException ex) {
            logger.error("❌ Error HIBERNATE DELETE: {}", ex.getMessage());
        } finally {
            cerrarRecursos(null, stmt);
        }
        return false;
    }
    
    private void cerrarRecursos(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException ex) {
            logger.warn("Error al cerrar recursos: {}", ex.getMessage());
        }
    }
}