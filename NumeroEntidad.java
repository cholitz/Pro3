import javax.swing.*;
import java.awt.*;

public class NumeroEntidad extends JLabel {
    private double x, y, vx, vy;
    private final int valor;
    private float alpha = 1.0f;

    public NumeroEntidad(int valor) {
        this.valor = valor;
        setText(String.valueOf(valor));
        setFont(new Font("Consolas", Font.BOLD, 90));  // GIGANTE
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(Color.WHITE);
        setSize(180, 180);  // MÁS GRANDE
        setOpaque(true);
        setBackground(getFondo());
        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 6)); // BORDE BRILLANTE
        vx = (Math.random() - 0.5) * 6;
        vy = 0;
    }

    private Color getFondo() {
        if (valor % 2 == 0) return new Color(0, 100, 255);
        if (esPrimo(valor)) return new Color(180, 0, 180);
        return new Color(0, 180, 0);
    }

    public boolean esPrimo(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }

    public void flash() {
        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 12));
        new Timer(400, e -> { setBorder(BorderFactory.createLineBorder(Color.YELLOW, 6)); ((Timer)e.getSource()).stop(); }).start();
    }

    public void flashNormal() {
        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 6));
    }

    public void explotar(JPanel panel) {
        setBackground(Color.RED.brighter());
        Timer t = new Timer(70, e -> {
            alpha -= 0.2f;
            if (alpha <= 0) {
                panel.remove(this);
                panel.revalidate();
                panel.repaint();
                ((Timer)e.getSource()).stop();
            } else repaint();
        });
        t.start();
    }

    public void mover() {
        vy += 0.2;
        x += vx;
        y += vy;
    }

    public void actualizarPosicion() { setLocation((int)x, (int)y); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2);
        g2.dispose();
    }

    // getters y setters
    public double getPosX() { return x; }
    public double getPosY() { return y; }
    public void setPosX(double x) { this.x = x; }
    public void setPosY(double y) { this.y = y; }
    public double getVelX() { return vx; }
    public double getVelY() { return vy; }
    public void setVelX(double vx) { this.vx = vx; }
    public void setVelY(double vy) { this.vy = vy; }
    public int getValor() { return valor; }
}