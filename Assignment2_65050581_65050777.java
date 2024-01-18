import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.*;

public class Assignment2_65050581_65050777 extends JPanel implements Runnable {
    public static void main(String[] args) {
        Assignment2_65050581_65050777 m = new Assignment2_65050581_65050777();
        m.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("x: " + x + " y: " + y);
            }
        });

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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage mainBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mainBuffer.createGraphics();

        g2d.setColor(Color.BLACK);
        // drawStrawHatStyle1(g2d);
        // drawStrawHatStyle2(g2d);

        g.drawImage(mainBuffer, 0, 0, null);
    }

    private void drawStrawHatStyle1 (Graphics g){
        drawArc(g, new Point(187,302), new Point(174,126), new Point(477,122), new Point(451,304), 1, MyColor.BLACK);
        drawArc(g, new Point(181,303), new Point(254,299), new Point(410,301), new Point(455,305), 1, MyColor.BLACK);
        drawArc(g, new Point(181,303), new Point(179,306), new Point(181,311), new Point(185,315), 1, MyColor.BLACK);
        drawArc(g, new Point(185,315), new Point(183,317), new Point(180,321), new Point(180,323), 1, MyColor.BLACK);
        drawArc(g, new Point(180,323), new Point(180,324), new Point(182,328), new Point(184,331), 1, MyColor.BLACK);
        drawArc(g, new Point(184,331), new Point(182,332), new Point(179,334), new Point(180,336), 1, MyColor.BLACK);
        g.drawLine(180,336, 185,345);
        drawArc(g, new Point(185,345), new Point(255,364), new Point(398,365), new Point(450,351), 1, MyColor.BLACK);
        drawArc(g, new Point(450,351), new Point(453,349), new Point(456,342), new Point(456,339), 1, MyColor.BLACK);
        drawArc(g, new Point(456,339), new Point(455,337), new Point(452,334), new Point(451,334), 1, MyColor.BLACK);
        drawArc(g, new Point(451,334), new Point(453,331), new Point(457,327), new Point(456,324), 1, MyColor.BLACK);
        drawArc(g, new Point(456,324), new Point(455,321), new Point(450,317), new Point(449,317), 1, MyColor.BLACK);
        drawArc(g, new Point(449,317), new Point(452,313), new Point(458,306), new Point(454,304), 1, MyColor.BLACK);
        drawArc(g, new Point(180,336), new Point(118,347), new Point(14,342), new Point(14,368), 1, MyColor.BLACK);
        drawArc(g, new Point(14,368), new Point(47,407), new Point(536,427), new Point(576,383), 1, MyColor.BLACK);
        drawArc(g, new Point(576,383), new Point(583,375), new Point(589,360), new Point(552,355), 1, MyColor.BLACK);
        drawArc(g, new Point(552,355), new Point(589,360), new Point(504,347), new Point(456,339), 1, MyColor.BLACK);
        drawArc(g, new Point(238,252), new Point(237,254), new Point(237,261), new Point(238,264), 1, MyColor.BLACK);
        drawArc(g, new Point(245,252), new Point(244,254), new Point(243,261), new Point(245,264), 1, MyColor.BLACK);
        g.drawLine(252, 254, 252, 264);
        g.drawLine(262, 256, 262, 261);
        drawArc(g, new Point(219,284), new Point(218,288), new Point(216,296), new Point(218,299), 1, MyColor.BLACK);
        drawArc(g, new Point(225,279), new Point(224,283), new Point(222,292), new Point(224,297), 1, MyColor.BLACK);
        drawArc(g, new Point(232,278), new Point(230,282), new Point(230,292), new Point(231,298), 1, MyColor.BLACK);
        drawArc(g, new Point(243,274), new Point(242,279), new Point(241,289), new Point(244,298), 1, MyColor.BLACK);
        drawArc(g, new Point(255,276), new Point(253,280), new Point(252,291), new Point(255,295), 1, MyColor.BLACK);
        drawArc(g, new Point(266,284), new Point(265,288), new Point(264,294), new Point(267,297), 1, MyColor.BLACK);
        drawArc(g, new Point(411,260), new Point(412,263), new Point(412,268), new Point(411,269), 1, MyColor.BLACK);
        drawArc(g, new Point(423,257), new Point(423,260), new Point(423,266), new Point(423,267), 1, MyColor.BLACK);
        drawArc(g, new Point(428,253), new Point(428,256), new Point(429,263), new Point(428,266), 1, MyColor.BLACK);
        g.drawLine(390,296, 390,299);
        g.drawLine(396,290, 396,299);
        drawArc(g, new Point(401,285), new Point(401,288), new Point(401,297), new Point(400,300), 1, MyColor.BLACK);
        g.drawLine(421,282, 420,302);
        g.drawLine(421,282, 420,302);
        g.drawLine(428,278, 428,297);
        g.drawLine(433,285, 433,294);
        drawArc(g, new Point(213,316), new Point(278,320), new Point(405,322), new Point(406,318), 1, MyColor.BLACK);
        drawArc(g, new Point(224,321), new Point(247,327), new Point(320,351), new Point(433,327), 1, MyColor.BLACK);
        g.drawLine(199,333, 252,351);
        drawArc(g, new Point(389,354), new Point(405,356), new Point(434,345), new Point(447,340), 1, MyColor.BLACK);
        drawArc(g, new Point(186,355), new Point(198,356), new Point(227,358), new Point(246,361), 1, MyColor.BLACK);
        drawArc(g, new Point(171,360), new Point(195,361), new Point(244,362), new Point(246,364), 1, MyColor.BLACK);
        drawArc(g, new Point(156,369), new Point(172,369), new Point(205,371), new Point(210,374), 1, MyColor.BLACK);
        drawArc(g, new Point(116,369), new Point(141,370), new Point(193,374), new Point(195,378), 1, MyColor.BLACK);
        drawArc(g, new Point(278,383), new Point(293,384), new Point(330,387), new Point(355,386), 1, MyColor.BLACK);
        drawArc(g, new Point(394,363), new Point(408,361), new Point(437,358), new Point(448,358), 1, MyColor.BLACK);
        drawArc(g, new Point(420,367), new Point(435,364), new Point(467,360), new Point(466,358), 1, MyColor.BLACK);
        drawArc(g, new Point(431,371), new Point(443,371), new Point(470,371), new Point(476,369), 1, MyColor.BLACK);
        drawArc(g, new Point(446,378), new Point(463,379), new Point(500,379), new Point(508,377), 1, MyColor.BLACK);
    }

   

    private void plot(Graphics g, int x, int y) {
        plot(g, x, y, 1);
    }

    private void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x, y, size, size);
    }

    private void midpointEllipse(Graphics g, int xc, int yc, int a, int b) {
        int a2 = a * a, b2 = b * b;
        int twoA2 = 2 * a2, twoB2 = 2 * b2;

        // region 1
        int x = 0;
        int y = b;
        int D = Math.round(b2 - a2 * b + a2 / 4);
        int Dx = 0, Dy = twoA2 * y;

        while (Dx <= Dy) {
            plot(g, xc + x, yc + y);
            plot(g, xc + x, yc - y);
            plot(g, xc - x, yc - y);
            plot(g, xc - x, yc + y);

            x++;
            Dx += twoB2;
            D += Dx + b2;

            if (D >= 0) {
                y--;
                Dy -= twoA2;
                D -= Dy;
            }
        }

        // region 2
        x = a;
        y = 0;
        D = Math.round(a2 - b2 * a + b2 / 4);
        Dx = twoB2 * x;
        Dy = 0;

        while (Dx >= Dy) {
            plot(g, xc + x, yc + y);
            plot(g, xc + x, yc - y);
            plot(g, xc - x, yc - y);
            plot(g, xc - x, yc + y);

            y++;
            Dy += twoA2;
            D += Dy + a2;

            if (D >= 0) {
                x--;
                Dx -= twoB2;
                D -= Dx;
            }
        }
    }

    public BufferedImage floodFill(BufferedImage m, Point p, Color target_colour, Color replacement_colour) {
        // Perform flood fill algorithm to replace target_colour with replacement_colour
        Queue<Point> q = new LinkedList<>();
        int targetRGB = target_colour.getRGB();
        int replacementRGB = replacement_colour.getRGB();

        if (targetRGB == replacementRGB) {
            return m;
        }

        int width = m.getWidth();
        int height = m.getHeight();
        int originalRGB = m.getRGB(p.x, p.y);

        if (originalRGB == replacementRGB) {
            return m;
        }

        q.add(p);

        while (!q.isEmpty()) {
            Point currentPoint = q.poll();
            int x = currentPoint.x;
            int y = currentPoint.y;

            if (m.getRGB(x, y) != targetRGB) {
                continue;
            }

            m.setRGB(x, y, replacementRGB);

            if (x > 0 && m.getRGB(x - 1, y) == targetRGB) {
                q.add(new Point(x - 1, y));
            }
            if (x < width - 1 && m.getRGB(x + 1, y) == targetRGB) {
                q.add(new Point(x + 1, y));
            }
            if (y > 0 && m.getRGB(x, y - 1) == targetRGB) {
                q.add(new Point(x, y - 1));
            }
            if (y < height - 1 && m.getRGB(x, y + 1) == targetRGB) {
                q.add(new Point(x, y + 1));
            }
        }

        return m;
    }

    private void drawArc(Graphics g, Point p1, Point p2, Point p3, Point p4, int thickness, Color color) {
        g.setColor(color);
        int n = 1000;
        for (int i = 0; i <= n; i++) {
            double t = (double) i / n;
            int x = (int)(Math.pow(1 - t, 3) * p1.x + 3 * t * Math.pow(1 - t, 2) * p2.x +
                3 * Math.pow(t, 2) * (1 - t) * p3.x + Math.pow(t, 3) * p4.x);
            int y = (int)(Math.pow(1 - t, 3) * p1.y + 3 * t * Math.pow(1 - t, 2) * p2.y +
                3 * Math.pow(t, 2) * (1 - t) * p3.y + Math.pow(t, 3) * p4.y);
            for (int j = -thickness / 2; j <= thickness / 2; j++) {
                for (int k = -thickness / 2; k <= thickness / 2; k++) {
                    plot(g, x + j, y + k);
                }
            }
        }
    }
}