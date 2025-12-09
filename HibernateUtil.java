import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {
    private static HibernateUtil instance;
    private Object sessionFactory;
    
    private HibernateUtil() {
        try {
            // Simulación de Hibernate
            System.out.println("Hibernate SessionFactory creada (simulada)");
            sessionFactory = new Object(); // Simulación
        } catch (Exception ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static synchronized HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }
    
    public Object getSessionFactory() {
        return sessionFactory;
    }
    
    public void shutdown() {
        System.out.println("Hibernate SessionFactory cerrada");
    }
}