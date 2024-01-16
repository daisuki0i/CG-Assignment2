import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Assignment2_65050581_65050777 extends JPanel implements Runnable {
    public static void main(String[] args) {
        Assignment2_65050581_65050777 m = new Assignment2_65050581_65050777();

        JFrame frame = new JFrame("Assignment2_65050581_65050777");
        frame.add(m);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        (new Thread(m)).start();
    }

    @Override
    public void run() {
        double lastTime = System.currentTimeMillis();
        double currentTime, elapsedTime;
        while (true) {
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            repaint();

        }
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
    }
}
