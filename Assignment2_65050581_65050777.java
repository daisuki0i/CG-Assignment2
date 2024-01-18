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

        drawRogerFaceFront(g2d);

        g.drawImage(mainBuffer, 0, 0, null);
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

    private void drawRogerFaceFront(Graphics g) {
        // Face
        drawArc(g, new Point(113, 305), new Point(76, 72), new Point(501, 87), new Point(466, 289), 1, MyColor.BLACK);
        drawArc(g, new Point(113, 305), new Point(121, 337), new Point(139, 407), new Point(150, 427), 1, MyColor.BLACK);
        drawArc(g, new Point(150, 427), new Point(169, 450), new Point(212, 497), new Point(236, 498), 1, MyColor.BLACK);
        g.drawLine(236, 498, 333, 498);
        drawArc(g, new Point(333, 498), new Point(344, 496), new Point(379, 477), new Point(428, 427), 1, MyColor.BLACK);
        drawArc(g, new Point(428, 427), new Point(442, 400), new Point(463, 329), new Point(466, 289), 1, MyColor.BLACK);

        // Left Ear
        drawArc(g, new Point(116, 316), new Point(109, 299), new Point(93, 265), new Point(86, 261), 1, MyColor.BLACK);
        drawArc(g, new Point(86, 261), new Point(78, 256), new Point(59, 295), new Point(69, 337), 1, MyColor.BLACK);
        drawArc(g, new Point(69, 337), new Point(79, 360), new Point(104, 382), new Point(133, 407), 1, MyColor.BLACK);
        drawArc(g, new Point(133, 407), new Point(135, 408), new Point(140, 408), new Point(141, 405), 1, MyColor.BLACK);

        // Right Ear
        drawArc(g, new Point(461, 323), new Point(470, 300), new Point(493, 257), new Point(508, 271), 1, MyColor.BLACK);
        drawArc(g, new Point(508, 271), new Point(517, 285), new Point(514, 314), new Point(511, 328), 1, MyColor.BLACK);
        drawArc(g, new Point(511, 328), new Point(508, 343), new Point(487, 383), new Point(446, 407), 1, MyColor.BLACK);
        drawArc(g, new Point(446, 407), new Point(441, 410), new Point(438, 412), new Point(437, 407), 1, MyColor.BLACK);

        // Neck
        g.drawLine(167, 446, 175, 510);
        g.drawLine(175, 510, 397, 511);
        g.drawLine(397, 511, 404, 450);

        // Face Shadow
        drawArc(g, new Point(114, 304), new Point(125, 290), new Point(132, 289), new Point(152, 302), 1, MyColor.BLACK);
        drawArc(g, new Point(152, 302), new Point(193, 326), new Point(216, 322), new Point(279, 267), 1, MyColor.BLACK);
        drawArc(g, new Point(279, 267), new Point(284, 263), new Point(294, 263), new Point(300, 267), 1, MyColor.BLACK);
        drawArc(g, new Point(300, 267), new Point(345, 298), new Point(381, 349), new Point(458, 282), 1, MyColor.BLACK);
        drawArc(g, new Point(458, 282), new Point(460, 283), new Point(465, 286), new Point(466, 290), 1, MyColor.BLACK);

        // Hair
        g.drawLine(122, 222, 55, 249);
        drawArc(g, new Point(55, 249), new Point(55, 293), new Point(34, 334), new Point(19, 343), 1, MyColor.BLACK);
        drawArc(g, new Point(19, 343), new Point(29, 343), new Point(56, 335), new Point(66, 315), 1, MyColor.BLACK);
        drawArc(g, new Point(66, 315), new Point(63, 326), new Point(62, 361), new Point(68, 393), 1, MyColor.BLACK);
        drawArc(g, new Point(68, 393), new Point(73, 382), new Point(76, 364), new Point(81, 362), 1, MyColor.BLACK);
        drawArc(g, new Point(81, 362), new Point(83, 374), new Point(86, 401), new Point(84, 415), 1, MyColor.BLACK);
        drawArc(g, new Point(84, 415), new Point(89, 410), new Point(99, 399), new Point(99, 393), 1, MyColor.BLACK);
        drawArc(g, new Point(99, 393), new Point(99, 399), new Point(99, 434), new Point(114, 460), 1, MyColor.BLACK);
        drawArc(g, new Point(114, 460), new Point(114, 452), new Point(116, 433), new Point(122, 425), 1, MyColor.BLACK);
        drawArc(g, new Point(122, 425), new Point(125, 435), new Point(131, 455), new Point(138, 465), 1, MyColor.BLACK);
        drawArc(g, new Point(138, 465), new Point(140, 459), new Point(145, 447), new Point(145, 442), 1, MyColor.BLACK);
        drawArc(g, new Point(145, 442), new Point(145, 437), new Point(150, 455), new Point(153, 465), 1, MyColor.BLACK);

        drawArc(g, new Point(460, 222), new Point(520, 237), new Point(517, 253), new Point(517, 260), 1, MyColor.BLACK);
        drawArc(g, new Point(517, 260), new Point(517, 284), new Point(526, 332), new Point(543, 338), 1, MyColor.BLACK);
        drawArc(g, new Point(543, 338), new Point(535, 336), new Point(518, 330), new Point(514, 327), 1, MyColor.BLACK);
        drawArc(g, new Point(514, 327), new Point(513, 342), new Point(513, 378), new Point(518, 400), 1, MyColor.BLACK);
        drawArc(g, new Point(518, 400), new Point(512, 394), new Point(498, 379), new Point(496, 371), 1, MyColor.BLACK);
        drawArc(g, new Point(496, 371), new Point(494, 361), new Point(497, 415), new Point(512, 429), 1, MyColor.BLACK);
        drawArc(g, new Point(512, 429), new Point(499, 429), new Point(477, 417), new Point(470, 403), 1, MyColor.BLACK);
        drawArc(g, new Point(470, 403), new Point(470, 411), new Point(468, 429), new Point(454, 440), 1, MyColor.BLACK);
        drawArc(g, new Point(454, 440), new Point(454, 445), new Point(455, 457), new Point(463, 470), 1, MyColor.BLACK);
        drawArc(g, new Point(463, 470), new Point(455, 466), new Point(438, 455), new Point(433, 443), 1, MyColor.BLACK);
        drawArc(g, new Point(433, 443), new Point(432, 449), new Point(429, 461), new Point(433, 471), 1, MyColor.BLACK);
        drawArc(g, new Point(433, 471), new Point(430, 468), new Point(423, 458), new Point(421, 448), 1, MyColor.BLACK);
        drawArc(g, new Point(421, 448), new Point(421, 452), new Point(420, 460), new Point(418, 463), 1, MyColor.BLACK);
    }
}