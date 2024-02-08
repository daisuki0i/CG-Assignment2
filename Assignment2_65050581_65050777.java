import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Assignment2_65050581_65050777 extends JPanel implements Runnable {
    private boolean doDrawTitle = true;
    private boolean doDrawTitleHat = false;
    private boolean doDrawRogerFull = false;
    private boolean doDrawFrames = false;

    private float titleOpacity = 0;
    private double titleHatScale = 0;
    private float titleHatOpacity = 1;
    private double rogerFullX = -1200;
    private double currentFrame = 30;

    private double animatedTime = 0;

    private static Point currentPoint = new Point(0, 0);

    public static void main(String[] args) {
        Assignment2_65050581_65050777 m = new Assignment2_65050581_65050777();
        m.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.printf("buffer = floodFill(buffer, new Point(%d, %d), MyColor.PLACEHOLDER, MyColor.OAKUM);%n", x, y);
                currentPoint = new Point(x, y);
                m.repaint();
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

            // increase text opacity to 1 in 3 seconds
            if (titleOpacity < 1) {
                titleOpacity += (elapsedTime / 4000);
                if (titleOpacity > 1) {
                    titleOpacity = 1;
                }
            }
            if (titleOpacity == 1) {
                doDrawTitleHat = true;
                titleHatScale += (elapsedTime / 500);
                if (titleHatScale > 4) {
                    doDrawTitle = false;
                    titleHatOpacity -= (elapsedTime / 500);
                    if (titleHatOpacity < 0) {
                        doDrawTitleHat = false;
                        titleHatOpacity = 0;
                    }
                }
            }

            // if the intro is ended, start the main animation
            if (!doDrawTitle && !doDrawTitleHat) {
                animatedTime += elapsedTime / 1000.0;
                // System.out.println(Math.floor(animatedTime));
            }

            if (animatedTime >= 1) {
                doDrawRogerFull = true;
                rogerFullX += 600 * (elapsedTime / 1000);
                if (rogerFullX > -310) {
                    doDrawRogerFull = false;
                    doDrawFrames = true;
                    // rogerFullX = -310;
                }
            }

            if (currentFrame <= 382 && doDrawFrames) {
                // 30 frames per second
                currentFrame += 30 * elapsedTime / 1000;
            }

            repaint();

        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage mainBuffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mainBuffer.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 601, 601);
        // mainBuffer = drawStrawHatStyle1();
        // mainBuffer = drawStrawHatStyle2();
        // mainBuffer = drawStrawHatStyle3();
        // mainBuffer = drawStrawHatStyle4();
        // mainBuffer = drawStrawHatStyle5();
        // mainBuffer = drawLuffyBoy();
        // mainBuffer = drawLuffyChild();
        // mainBuffer = drawLuffyChildStandWithBack();
        // mainBuffer = drawRogerFaceFront();
        // mainBuffer = drawRogerFaceSide1();
        mainBuffer = drawLogoOnePiece();
        
        if (doDrawFrames) {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File("frames/" + (int)currentFrame + ".jpg"));
            } catch (IOException e) {
               e.printStackTrace();
            }
            g2d.drawImage(img, 0, 0, 600, 600, null);
        }

        if (doDrawTitle) {
            BufferedImage title = drawTitle(titleOpacity);
            g2d.drawImage(title, 0, 0, null);
        }

        if (doDrawTitleHat) {
            BufferedImage titleHat = drawStrawHatStyle2();

            AffineTransform saveTransform = g2d.getTransform();
            g2d.translate(320, 400);
            g2d.scale(titleHatScale, titleHatScale);
            g2d.translate(-320, -400);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleHatOpacity));
            g2d.drawImage(titleHat, 0, 0, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.setTransform(saveTransform);
        }

        if (doDrawRogerFull) {
            BufferedImage rogerFull1 = new BufferedImage(1200, 601, BufferedImage.TYPE_INT_ARGB);
            BufferedImage rogerFaceFront = drawRogerFaceFront();
            BufferedImage strawHatStyle1 = drawStrawHatStyle1();
            rogerFull1.getGraphics().drawImage(rogerFaceFront, 100, 80, null);
            rogerFull1.getGraphics().drawImage(strawHatStyle1, -10, -190, 750, 750, null);
            g2d.drawImage(rogerFull1, (int) rogerFullX, -310, (int)(1200 * 1.6), (int)(601 * 1.6), null);
        }

        g.drawImage(mainBuffer, 0, 0, null);

    }

    private BufferedImage drawTitle(float opacity) {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(Color.WHITE);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        g.setFont(new Font("TimesRoman", Font.BOLD, 32));
        drawStringCenteredScreen(g, "From Babies to the New Era", -25);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        drawStringCenteredScreen(g, "- Computer Graphics Assignment 2 -", 25);

        return buffer;
    }

    private void drawStringCenteredScreen(Graphics g, String s, int yAdjust) {
        FontMetrics fm = g.getFontMetrics();
        int x = (600 - fm.stringWidth(s)) / 2;
        int y = (600 - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(s, x, y + yAdjust);
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
            int x = (int) (Math.pow(1 - t, 3) * p1.x + 3 * t * Math.pow(1 - t, 2) * p2.x +
                    3 * Math.pow(t, 2) * (1 - t) * p3.x + Math.pow(t, 3) * p4.x);
            int y = (int) (Math.pow(1 - t, 3) * p1.y + 3 * t * Math.pow(1 - t, 2) * p2.y +
                    3 * Math.pow(t, 2) * (1 - t) * p3.y + Math.pow(t, 3) * p4.y);
            for (int j = -thickness / 2; j <= thickness / 2; j++) {
                for (int k = -thickness / 2; k <= thickness / 2; k++) {
                    plot(g, x + j, y + k);
                }
            }
        }
    }

    private BufferedImage drawStrawHatStyle1() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        drawArc(g, new Point(187, 302), new Point(174, 126), new Point(477, 122), new Point(451, 304), 1,
                MyColor.BLACK);
        drawArc(g, new Point(181, 303), new Point(254, 299), new Point(410, 301), new Point(455, 305), 1,
                MyColor.BLACK);
        drawArc(g, new Point(181, 303), new Point(179, 306), new Point(181, 311), new Point(185, 315), 1,
                MyColor.BLACK);
        drawArc(g, new Point(185, 315), new Point(183, 317), new Point(180, 321), new Point(180, 323), 1,
                MyColor.BLACK);
        drawArc(g, new Point(180, 323), new Point(180, 324), new Point(182, 328), new Point(184, 331), 1,
                MyColor.BLACK);
        drawArc(g, new Point(184, 331), new Point(182, 332), new Point(179, 334), new Point(180, 336), 1,
                MyColor.BLACK);
        g.drawLine(180, 336, 185, 345);
        drawArc(g, new Point(185, 345), new Point(255, 364), new Point(398, 365), new Point(450, 351), 1,
                MyColor.BLACK);
        drawArc(g, new Point(450, 351), new Point(453, 349), new Point(456, 342), new Point(456, 339), 1,
                MyColor.BLACK);
        drawArc(g, new Point(456, 339), new Point(455, 337), new Point(452, 334), new Point(451, 334), 1,
                MyColor.BLACK);
        drawArc(g, new Point(451, 334), new Point(453, 331), new Point(457, 327), new Point(456, 324), 1,
                MyColor.BLACK);
        drawArc(g, new Point(456, 324), new Point(455, 321), new Point(450, 317), new Point(449, 317), 1,
                MyColor.BLACK);
        drawArc(g, new Point(449, 317), new Point(452, 313), new Point(458, 306), new Point(454, 304), 1,
                MyColor.BLACK);
        drawArc(g, new Point(180, 336), new Point(118, 347), new Point(14, 342), new Point(14, 368), 1, MyColor.BLACK);
        drawArc(g, new Point(14, 368), new Point(47, 407), new Point(536, 427), new Point(576, 383), 1, MyColor.BLACK);
        drawArc(g, new Point(576, 383), new Point(583, 375), new Point(589, 360), new Point(552, 355), 1,
                MyColor.BLACK);
        drawArc(g, new Point(552, 355), new Point(589, 360), new Point(504, 347), new Point(456, 339), 1,
                MyColor.BLACK);
        drawArc(g, new Point(238, 252), new Point(237, 254), new Point(237, 261), new Point(238, 264), 1,
                MyColor.BLACK);
        drawArc(g, new Point(245, 252), new Point(244, 254), new Point(243, 261), new Point(245, 264), 1,
                MyColor.BLACK);
        g.drawLine(252, 254, 252, 264);
        g.drawLine(262, 256, 262, 261);
        drawArc(g, new Point(219, 284), new Point(218, 288), new Point(216, 296), new Point(218, 299), 1,
                MyColor.BLACK);
        drawArc(g, new Point(225, 279), new Point(224, 283), new Point(222, 292), new Point(224, 297), 1,
                MyColor.BLACK);
        drawArc(g, new Point(232, 278), new Point(230, 282), new Point(230, 292), new Point(231, 298), 1,
                MyColor.BLACK);
        drawArc(g, new Point(243, 274), new Point(242, 279), new Point(241, 289), new Point(244, 298), 1,
                MyColor.BLACK);
        drawArc(g, new Point(255, 276), new Point(253, 280), new Point(252, 291), new Point(255, 295), 1,
                MyColor.BLACK);
        drawArc(g, new Point(266, 284), new Point(265, 288), new Point(264, 294), new Point(267, 297), 1,
                MyColor.BLACK);
        drawArc(g, new Point(411, 260), new Point(412, 263), new Point(412, 268), new Point(411, 269), 1,
                MyColor.BLACK);
        drawArc(g, new Point(423, 257), new Point(423, 260), new Point(423, 266), new Point(423, 267), 1,
                MyColor.BLACK);
        drawArc(g, new Point(428, 253), new Point(428, 256), new Point(429, 263), new Point(428, 266), 1,
                MyColor.BLACK);
        g.drawLine(390, 296, 390, 299);
        g.drawLine(396, 290, 396, 299);
        drawArc(g, new Point(401, 285), new Point(401, 288), new Point(401, 297), new Point(400, 300), 1,
                MyColor.BLACK);
        g.drawLine(421, 282, 420, 302);
        g.drawLine(421, 282, 420, 302);
        g.drawLine(428, 278, 428, 297);
        g.drawLine(433, 285, 433, 294);
        drawArc(g, new Point(213, 316), new Point(278, 320), new Point(405, 322), new Point(406, 318), 1,
                MyColor.BLACK);
        drawArc(g, new Point(224, 321), new Point(247, 327), new Point(320, 351), new Point(433, 327), 1,
                MyColor.BLACK);
        g.drawLine(199, 333, 252, 351);
        drawArc(g, new Point(389, 354), new Point(405, 356), new Point(434, 345), new Point(447, 340), 1,
                MyColor.BLACK);
        drawArc(g, new Point(186, 355), new Point(198, 356), new Point(227, 358), new Point(246, 361), 1,
                MyColor.BLACK);
        drawArc(g, new Point(171, 360), new Point(195, 361), new Point(244, 362), new Point(246, 364), 1,
                MyColor.BLACK);
        drawArc(g, new Point(156, 369), new Point(172, 369), new Point(205, 371), new Point(210, 374), 1,
                MyColor.BLACK);
        drawArc(g, new Point(116, 369), new Point(141, 370), new Point(193, 374), new Point(195, 378), 1,
                MyColor.BLACK);
        drawArc(g, new Point(278, 383), new Point(293, 384), new Point(330, 387), new Point(355, 386), 1,
                MyColor.BLACK);
        drawArc(g, new Point(394, 363), new Point(408, 361), new Point(437, 358), new Point(448, 358), 1,
                MyColor.BLACK);
        drawArc(g, new Point(420, 367), new Point(435, 364), new Point(467, 360), new Point(466, 358), 1,
                MyColor.BLACK);
        drawArc(g, new Point(431, 371), new Point(443, 371), new Point(470, 371), new Point(476, 369), 1,
                MyColor.BLACK);
        drawArc(g, new Point(446, 378), new Point(463, 379), new Point(500, 379), new Point(508, 377), 1,
                MyColor.BLACK);

        buffer = floodFill(buffer, new Point(327, 243), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(87, 371), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(307, 330), MyColor.PLACEHOLDER, MyColor.HEADDAND);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawStrawHatStyle2() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        drawArc(g, new Point(187, 361), new Point(165, 179), new Point(496, 175), new Point(452, 389), 1,
                MyColor.BLACK);
        drawArc(g, new Point(180, 358), new Point(235, 377), new Point(368, 412), new Point(452, 389), 1,
                MyColor.BLACK);
        drawArc(g, new Point(180, 358), new Point(177, 359), new Point(173, 364), new Point(177, 372), 1,
                MyColor.BLACK);
        drawArc(g, new Point(177, 372), new Point(175, 374), new Point(171, 378), new Point(171, 385), 1,
                MyColor.BLACK);
        drawArc(g, new Point(171, 385), new Point(168, 387), new Point(162, 391), new Point(165, 398), 1,
                MyColor.BLACK);
        drawArc(g, new Point(165, 398), new Point(219, 426), new Point(353, 474), new Point(455, 442), 1,
                MyColor.BLACK);
        drawArc(g, new Point(455, 442), new Point(452, 437), new Point(449, 428), new Point(455, 425), 1,
                MyColor.BLACK);
        drawArc(g, new Point(455, 425), new Point(453, 421), new Point(449, 412), new Point(455, 406), 1,
                MyColor.BLACK);
        drawArc(g, new Point(455, 406), new Point(456, 401), new Point(457, 392), new Point(452, 389), 1,
                MyColor.BLACK);
        g.drawLine(166, 390, 43, 398);
        drawArc(g, new Point(43, 398), new Point(38, 395), new Point(25, 389), new Point(17, 388), 1, MyColor.BLACK);
        drawArc(g, new Point(17, 388), new Point(14, 390), new Point(8, 400), new Point(13, 409), 1, MyColor.BLACK);
        drawArc(g, new Point(13, 409), new Point(17, 411), new Point(27, 416), new Point(31, 425), 1, MyColor.BLACK);
        drawArc(g, new Point(31, 425), new Point(73, 449), new Point(342, 524), new Point(492, 494), 1, MyColor.BLACK);
        g.drawLine(492, 494, 577, 494);
        drawArc(g, new Point(577, 494), new Point(581, 492), new Point(590, 487), new Point(592, 481), 1,
                MyColor.BLACK);
        drawArc(g, new Point(592, 481), new Point(588, 478), new Point(581, 469), new Point(578, 461), 1,
                MyColor.BLACK);
        g.drawLine(578, 461, 542, 458);
        g.drawLine(542, 458, 456, 425);
        drawArc(g, new Point(231, 271), new Point(227, 276), new Point(219, 288), new Point(219, 299), 1,
                MyColor.BLACK);
        drawArc(g, new Point(238, 286), new Point(236, 290), new Point(231, 301), new Point(232, 310), 1,
                MyColor.BLACK);
        drawArc(g, new Point(200, 333), new Point(197, 339), new Point(191, 353), new Point(194, 361), 1,
                MyColor.BLACK);
        drawArc(g, new Point(215, 316), new Point(211, 327), new Point(206, 352), new Point(210, 362), 1,
                MyColor.BLACK);
        drawArc(g, new Point(229, 329), new Point(226, 336), new Point(220, 353), new Point(224, 367), 1,
                MyColor.BLACK);
        drawArc(g, new Point(253, 358), new Point(251, 360), new Point(247, 367), new Point(248, 373), 1,
                MyColor.BLACK);
        drawArc(g, new Point(435, 309), new Point(436, 311), new Point(439, 319), new Point(437, 328), 1,
                MyColor.BLACK);
        g.drawLine(450, 322, 450, 331);
        drawArc(g, new Point(426, 360), new Point(427, 366), new Point(426, 378), new Point(425, 386), 1,
                MyColor.BLACK);
        drawArc(g, new Point(437, 341), new Point(437, 353), new Point(436, 380), new Point(433, 386), 1,
                MyColor.BLACK);
        g.drawLine(447, 370, 447, 390);
        drawArc(g, new Point(171, 385), new Point(198, 396), new Point(257, 422), new Point(274, 437), 1,
                MyColor.BLACK);
        drawArc(g, new Point(264, 408), new Point(311, 417), new Point(412, 424), new Point(439, 412), 1,
                MyColor.BLACK);
        drawArc(g, new Point(264, 408), new Point(288, 418), new Point(317, 443), new Point(439, 422), 1,
                MyColor.BLACK);
        drawArc(g, new Point(439, 422), new Point(440, 420), new Point(441, 415), new Point(439, 412), 1,
                MyColor.BLACK);
        g.drawLine(283, 446, 324, 453);
        drawArc(g, new Point(231, 448), new Point(247, 448), new Point(286, 454), new Point(304, 459), 1,
                MyColor.BLACK);
        drawArc(g, new Point(224, 454), new Point(249, 457), new Point(305, 464), new Point(326, 473), 1,
                MyColor.BLACK);
        drawArc(g, new Point(188, 448), new Point(194, 451), new Point(211, 459), new Point(228, 461), 1,
                MyColor.BLACK);
        drawArc(g, new Point(228, 461), new Point(242, 460), new Point(273, 461), new Point(288, 471), 1,
                MyColor.BLACK);
        drawArc(g, new Point(443, 451), new Point(449, 451), new Point(464, 451), new Point(468, 446), 1,
                MyColor.BLACK);
        drawArc(g, new Point(455, 456), new Point(461, 457), new Point(476, 456), new Point(481, 452), 1,
                MyColor.BLACK);
        drawArc(g, new Point(472, 464), new Point(479, 464), new Point(494, 463), new Point(502, 459), 1,
                MyColor.BLACK);
        drawArc(g, new Point(472, 464), new Point(479, 464), new Point(494, 463), new Point(502, 459), 1,
                MyColor.BLACK);
        drawArc(g, new Point(472, 471), new Point(485, 470), new Point(514, 467), new Point(526, 458), 1,
                MyColor.BLACK);

        buffer = floodFill(buffer, new Point(324, 339), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(152, 423), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(245, 390), MyColor.PLACEHOLDER, MyColor.HEADDAND);
        buffer = floodFill(buffer, new Point(308, 420), MyColor.PLACEHOLDER, MyColor.HEADDAND_SHADOW);
        buffer = floodFill(buffer, new Point(201, 408), MyColor.PLACEHOLDER, MyColor.HEADDAND_SHADOW);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawStrawHatStyle3() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        drawArc(g, new Point(185, 329), new Point(140, 146), new Point(441, 84), new Point(426, 323), 1, MyColor.BLACK);
        drawArc(g, new Point(185, 329), new Point(190, 351), new Point(383, 351), new Point(426, 323), 1,
                MyColor.BLACK);
        drawArc(g, new Point(185, 329), new Point(177, 329), new Point(175, 343), new Point(177, 374), 1,
                MyColor.BLACK);
        drawArc(g, new Point(177, 374), new Point(216, 400), new Point(403, 417), new Point(441, 354), 1,
                MyColor.BLACK);
        drawArc(g, new Point(441, 354), new Point(441, 350), new Point(440, 343), new Point(434, 339), 1,
                MyColor.BLACK);
        drawArc(g, new Point(434, 339), new Point(434, 335), new Point(434, 326), new Point(426, 323), 1,
                MyColor.BLACK);
        drawArc(g, new Point(176, 346), new Point(138, 346), new Point(101, 366), new Point(91, 366), 1, MyColor.BLACK);
        drawArc(g, new Point(91, 366), new Point(70, 369), new Point(25, 370), new Point(25, 374), 1, MyColor.BLACK);
        drawArc(g, new Point(25, 374), new Point(15, 376), new Point(1, 393), new Point(25, 408), 1, MyColor.BLACK);
        drawArc(g, new Point(25, 408), new Point(99, 441), new Point(312, 454), new Point(418, 432), 1, MyColor.BLACK);
        drawArc(g, new Point(418, 432), new Point(437, 424), new Point(483, 407), new Point(519, 404), 1,
                MyColor.BLACK);
        drawArc(g, new Point(519, 404), new Point(526, 403), new Point(542, 399), new Point(550, 393), 1,
                MyColor.BLACK);
        drawArc(g, new Point(550, 393), new Point(556, 386), new Point(574, 372), new Point(589, 368), 1,
                MyColor.BLACK);
        drawArc(g, new Point(589, 368), new Point(591, 360), new Point(592, 343), new Point(586, 339), 1,
                MyColor.BLACK);
        g.drawLine(586, 339, 504, 337);
        drawArc(g, new Point(504, 337), new Point(497, 331), new Point(455, 323), new Point(431, 327), 1,
                MyColor.BLACK);
        g.drawLine(203, 259, 203, 283);
        g.drawLine(211, 264, 211, 294);
        drawArc(g, new Point(193, 295), new Point(193, 306), new Point(195, 330), new Point(197, 336), 1,
                MyColor.BLACK);
        drawArc(g, new Point(203, 303), new Point(202, 311), new Point(202, 329), new Point(207, 339), 1,
                MyColor.BLACK);
        drawArc(g, new Point(214, 320), new Point(213, 324), new Point(211, 334), new Point(214, 339), 1,
                MyColor.BLACK);
        drawArc(g, new Point(222, 321), new Point(222, 325), new Point(222, 335), new Point(226, 342), 1,
                MyColor.BLACK);
        g.drawLine(237, 332, 237, 343);
        g.drawLine(388, 250, 391, 279);
        g.drawLine(387, 298, 389, 335);
        g.drawLine(395, 295, 395, 321);
        drawArc(g, new Point(398, 251), new Point(400, 253), new Point(404, 260), new Point(406, 272), 1,
                MyColor.BLACK);
        drawArc(g, new Point(407, 275), new Point(409, 283), new Point(414, 301), new Point(413, 317), 1,
                MyColor.BLACK);
        drawArc(g, new Point(219, 358), new Point(253, 364), new Point(339, 369), new Point(412, 347), 1,
                MyColor.BLACK);
        drawArc(g, new Point(412, 347), new Point(398, 358), new Point(364, 376), new Point(341, 379), 1,
                MyColor.BLACK);
        drawArc(g, new Point(229, 373), new Point(236, 377), new Point(258, 384), new Point(286, 385), 1,
                MyColor.BLACK);
        drawArc(g, new Point(174, 396), new Point(182, 397), new Point(202, 401), new Point(218, 401), 1,
                MyColor.BLACK);
        drawArc(g, new Point(149, 401), new Point(157, 403), new Point(180, 407), new Point(205, 407), 1,
                MyColor.BLACK);
        drawArc(g, new Point(117, 404), new Point(127, 407), new Point(158, 413), new Point(197, 417), 1,
                MyColor.BLACK);
        drawArc(g, new Point(380, 397), new Point(395, 393), new Point(430, 383), new Point(447, 373), 1,
                MyColor.BLACK);
        drawArc(g, new Point(405, 399), new Point(415, 397), new Point(439, 391), new Point(456, 382), 1,
                MyColor.BLACK);
        drawArc(g, new Point(430, 403), new Point(445, 399), new Point(476, 387), new Point(487, 379), 1,
                MyColor.BLACK);

        buffer = floodFill(buffer, new Point(303, 282), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(304, 423), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(301, 380), MyColor.PLACEHOLDER, MyColor.HEADDAND);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawStrawHatStyle4() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        drawArc(g, new Point(164, 349), new Point(81, 208), new Point(386, 27), new Point(452, 273), 1, MyColor.BLACK);
        drawArc(g, new Point(164, 349), new Point(186, 334), new Point(251, 291), new Point(452, 273), 1,
                MyColor.BLACK);
        drawArc(g, new Point(164, 349), new Point(161, 351), new Point(155, 358), new Point(156, 362), 1,
                MyColor.BLACK);
        drawArc(g, new Point(156, 362), new Point(156, 364), new Point(157, 368), new Point(164, 367), 1,
                MyColor.BLACK);
        drawArc(g, new Point(164, 367), new Point(158, 371), new Point(152, 379), new Point(166, 379), 1,
                MyColor.BLACK);
        drawArc(g, new Point(166, 379), new Point(242, 355), new Point(405, 311), new Point(458, 324), 1,
                MyColor.BLACK);
        drawArc(g, new Point(458, 324), new Point(460, 316), new Point(463, 297), new Point(450, 286), 1,
                MyColor.BLACK);
        drawArc(g, new Point(450, 286), new Point(454, 281), new Point(461, 272), new Point(452, 273), 1,
                MyColor.BLACK);
        g.drawLine(166, 379, 131, 396);
        g.drawLine(131, 396, 36, 427);
        drawArc(g, new Point(36, 427), new Point(27, 428), new Point(10, 432), new Point(10, 437), 1, MyColor.BLACK);
        drawArc(g, new Point(10, 437), new Point(8, 441), new Point(9, 449), new Point(19, 449), 1, MyColor.BLACK);
        g.drawLine(19, 449, 26, 445);
        g.drawLine(26, 445, 36, 445);
        g.drawLine(36, 445, 50, 444);
        g.drawLine(50, 444, 78, 435);
        drawArc(g, new Point(78, 435), new Point(121, 432), new Point(177, 412), new Point(229, 412), 1, MyColor.BLACK);
        drawArc(g, new Point(229, 412), new Point(322, 385), new Point(527, 342), new Point(591, 339), 1,
                MyColor.BLACK);
        drawArc(g, new Point(591, 339), new Point(591, 333), new Point(582, 321), new Point(550, 321), 1,
                MyColor.BLACK);
        g.drawLine(550, 321, 458, 324);
        drawArc(g, new Point(36, 445), new Point(48, 451), new Point(80, 451), new Point(100, 442), 1, MyColor.BLACK);
        drawArc(g, new Point(100, 442), new Point(111, 444), new Point(134, 447), new Point(139, 445), 1,
                MyColor.BLACK);
        drawArc(g, new Point(139, 445), new Point(150, 442), new Point(175, 437), new Point(179, 435), 1,
                MyColor.BLACK);
        drawArc(g, new Point(179, 435), new Point(180, 430), new Point(182, 420), new Point(181, 416), 1,
                MyColor.BLACK);
        drawArc(g, new Point(170, 281), new Point(170, 286), new Point(170, 299), new Point(172, 305), 1,
                MyColor.BLACK);
        drawArc(g, new Point(164, 295), new Point(163, 300), new Point(164, 311), new Point(166, 315), 1,
                MyColor.BLACK);
        g.drawLine(161, 319, 167, 346);
        drawArc(g, new Point(168, 326), new Point(168, 329), new Point(169, 335), new Point(171, 337), 1,
                MyColor.BLACK);
        drawArc(g, new Point(178, 317), new Point(178, 321), new Point(179, 329), new Point(182, 334), 1,
                MyColor.BLACK);
        drawArc(g, new Point(188, 317), new Point(187, 319), new Point(187, 325), new Point(191, 329), 1,
                MyColor.BLACK);
        g.drawLine(206, 346, 223, 338);
        drawArc(g, new Point(230, 334), new Point(259, 322), new Point(327, 299), new Point(361, 299), 1,
                MyColor.BLACK);
        drawArc(g, new Point(377, 303), new Point(388, 300), new Point(413, 295), new Point(431, 295), 1,
                MyColor.BLACK);
        drawArc(g, new Point(178, 366), new Point(189, 361), new Point(217, 349), new Point(245, 346), 1,
                MyColor.BLACK);
        drawArc(g, new Point(244, 342), new Point(274, 330), new Point(339, 306), new Point(361, 309), 1,
                MyColor.BLACK);
        drawArc(g, new Point(213, 373), new Point(228, 366), new Point(266, 353), new Point(288, 351), 1,
                MyColor.BLACK);
        drawArc(g, new Point(227, 375), new Point(251, 368), new Point(301, 354), new Point(310, 354), 1,
                MyColor.BLACK);
        drawArc(g, new Point(210, 387), new Point(226, 381), new Point(267, 376), new Point(278, 372), 1,
                MyColor.BLACK);
        drawArc(g, new Point(156, 407), new Point(166, 405), new Point(190, 396), new Point(200, 395), 1,
                MyColor.BLACK);
        g.drawLine(199, 401, 273, 381);
        drawArc(g, new Point(355, 344), new Point(362, 342), new Point(383, 337), new Point(407, 337), 1,
                MyColor.BLACK);
        drawArc(g, new Point(333, 352), new Point(346, 349), new Point(377, 343), new Point(391, 344), 1,
                MyColor.BLACK);

        buffer = floodFill(buffer, new Point(322, 230), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(319, 380), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(138, 435), MyColor.PLACEHOLDER, MyColor.STRAW_HAT_SHADOW);
        buffer = floodFill(buffer, new Point(321, 324), MyColor.PLACEHOLDER, MyColor.HEADDAND);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawStrawHatStyle5() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        drawArc(g, new Point(167, 300), new Point(192, 326), new Point(353, 349), new Point(449, 320), 1,
                MyColor.BLACK);
        drawArc(g, new Point(167, 300), new Point(134, 131), new Point(466, 62), new Point(449, 320), 1, MyColor.BLACK);
        drawArc(g, new Point(166, 294), new Point(162, 294), new Point(156, 299), new Point(162, 311), 1,
                MyColor.BLACK);
        drawArc(g, new Point(162, 311), new Point(158, 312), new Point(153, 319), new Point(166, 334), 1,
                MyColor.BLACK);
        drawArc(g, new Point(158, 323), new Point(155, 325), new Point(151, 331), new Point(152, 336), 1,
                MyColor.BLACK);
        drawArc(g, new Point(152, 336), new Point(164, 357), new Point(328, 409), new Point(458, 359), 1,
                MyColor.BLACK);
        drawArc(g, new Point(458, 359), new Point(458, 355), new Point(457, 346), new Point(453, 341), 1,
                MyColor.BLACK);
        drawArc(g, new Point(453, 341), new Point(453, 334), new Point(454, 320), new Point(449, 314), 1,
                MyColor.BLACK);
        drawArc(g, new Point(157, 320), new Point(110, 324), new Point(7, 343), new Point(19, 367), 1, MyColor.BLACK);
        drawArc(g, new Point(19, 367), new Point(35, 384), new Point(166, 413), new Point(200, 418), 1, MyColor.BLACK);
        drawArc(g, new Point(200, 418), new Point(205, 421), new Point(217, 426), new Point(225, 426), 1,
                MyColor.BLACK);
        drawArc(g, new Point(225, 426), new Point(300, 441), new Point(514, 421), new Point(574, 403), 1,
                MyColor.BLACK);
        drawArc(g, new Point(574, 403), new Point(576, 399), new Point(581, 392), new Point(586, 394), 1,
                MyColor.BLACK);
        drawArc(g, new Point(586, 394), new Point(590, 390), new Point(598, 382), new Point(598, 378), 1,
                MyColor.BLACK);
        drawArc(g, new Point(598, 378), new Point(596, 375), new Point(592, 368), new Point(586, 368), 1,
                MyColor.BLACK);
        g.drawLine(586, 368, 453, 333);
        g.drawLine(173, 305, 175, 282);
        g.drawLine(182, 283, 180, 309);
        g.drawLine(190, 277, 190, 313);
        g.drawLine(200, 290, 200, 316);
        drawArc(g, new Point(426, 273), new Point(426, 275), new Point(426, 280), new Point(426, 280), 1,
                MyColor.BLACK);
        drawArc(g, new Point(435, 276), new Point(435, 277), new Point(436, 280), new Point(435, 284), 1,
                MyColor.BLACK);
        g.drawLine(410, 310, 407, 328);
        drawArc(g, new Point(418, 300), new Point(419, 305), new Point(420, 316), new Point(418, 323), 1,
                MyColor.BLACK);
        drawArc(g, new Point(425, 292), new Point(426, 301), new Point(426, 320), new Point(424, 323), 1,
                MyColor.BLACK);
        drawArc(g, new Point(435, 295), new Point(436, 302), new Point(436, 317), new Point(434, 319), 1,
                MyColor.BLACK);
        drawArc(g, new Point(166, 319), new Point(171, 324), new Point(182, 336), new Point(189, 340), 1,
                MyColor.BLACK);
        drawArc(g, new Point(189, 328), new Point(201, 330), new Point(234, 349), new Point(240, 346), 1,
                MyColor.BLACK);
        drawArc(g, new Point(253, 349), new Point(262, 351), new Point(285, 356), new Point(306, 357), 1,
                MyColor.BLACK);
        g.drawLine(327, 360, 362, 360);
        drawArc(g, new Point(401, 352), new Point(407, 349), new Point(424, 344), new Point(435, 346), 1,
                MyColor.BLACK);
        drawArc(g, new Point(259, 358), new Point(268, 360), new Point(292, 364), new Point(314, 365), 1,
                MyColor.BLACK);
        drawArc(g, new Point(330, 368), new Point(356, 367), new Point(414, 361), new Point(433, 348), 1,
                MyColor.BLACK);
        g.drawLine(433, 354, 446, 346);
        g.drawLine(193, 366, 245, 377);
        g.drawLine(201, 374, 240, 382);
        g.drawLine(216, 377, 244, 379);
        g.drawLine(179, 379, 214, 386);
        drawArc(g, new Point(140, 376), new Point(152, 379), new Point(179, 385), new Point(191, 385), 1,
                MyColor.BLACK);
        drawArc(g, new Point(130, 381), new Point(143, 385), new Point(178, 393), new Point(207, 395), 1,
                MyColor.BLACK);
        g.drawLine(340, 387, 383, 386);
        g.drawLine(367, 389, 412, 386);
        drawArc(g, new Point(374, 394), new Point(386, 393), new Point(414, 392), new Point(426, 387), 1,
                MyColor.BLACK);
        g.drawLine(361, 400, 414, 394);
        g.drawLine(364, 405, 433, 405);
        g.drawLine(408, 411, 457, 404);

        buffer = floodFill(buffer, new Point(304, 263), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(298, 406), MyColor.PLACEHOLDER, MyColor.STRAW_HAT);
        buffer = floodFill(buffer, new Point(311, 342), MyColor.PLACEHOLDER, MyColor.HEADDAND);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawRogerFaceFront() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        // Face
        drawArc(g, new Point(113, 305), new Point(76, 72), new Point(501, 87), new Point(466, 289), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(113, 305), new Point(121, 337), new Point(139, 407), new Point(150, 427), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(150, 427), new Point(169, 450), new Point(212, 497), new Point(236, 498), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(236, 498, 333, 498);
        drawArc(g, new Point(333, 498), new Point(344, 496), new Point(379, 477), new Point(428, 427), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(428, 427), new Point(442, 400), new Point(463, 329), new Point(466, 289), 1,
                MyColor.EERIE_BLACK);

        // Left Ear
        drawArc(g, new Point(116, 316), new Point(109, 299), new Point(93, 265), new Point(86, 261), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(86, 261), new Point(78, 256), new Point(59, 295), new Point(69, 337), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(69, 337), new Point(79, 360), new Point(104, 382), new Point(133, 407), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(133, 407), new Point(135, 408), new Point(140, 408), new Point(141, 405), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(76, 311), new Point(76, 299), new Point(75, 293), new Point(82, 284), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(82, 284), new Point(84, 281), new Point(86, 280), new Point(88, 284), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(88, 284), new Point(90, 288), new Point(93, 292), new Point(95, 296), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(95, 296), new Point(93, 311), new Point(91, 311), new Point(80, 309), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(95, 296), new Point(102, 307), new Point(109, 317), new Point(108, 328), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(108, 328), new Point(106, 355), new Point(110, 353), new Point(130, 370), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(107, 321), new Point(105, 316), new Point(103, 314), new Point(95, 314), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(95, 314), new Point(87, 314), new Point(87, 318), new Point(85, 330), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(85, 330), new Point(82, 349), new Point(85, 345), new Point(126, 382), 1,
                MyColor.EERIE_BLACK);

        // Right Ear
        drawArc(g, new Point(461, 323), new Point(470, 300), new Point(493, 257), new Point(508, 271), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(508, 271), new Point(517, 285), new Point(514, 314), new Point(511, 328), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(511, 328), new Point(508, 343), new Point(487, 383), new Point(446, 407), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(446, 407), new Point(441, 410), new Point(438, 412), new Point(437, 407), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(509, 297, 504, 285);
        drawArc(g, new Point(504, 285), new Point(503, 283), new Point(502, 281), new Point(498, 284), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(498, 284), new Point(494, 287), new Point(489, 291), new Point(483, 297), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(483, 297), new Point(475, 305), new Point(468, 317), new Point(468, 329), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(468, 329), new Point(468, 341), new Point(475, 354), new Point(467, 362), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(467, 362, 450, 379);
        drawArc(g, new Point(506, 290), new Point(507, 296), new Point(509, 307), new Point(502, 311), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(483, 297), new Point(484, 305), new Point(490, 312), new Point(495, 312), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(468, 330), new Point(495, 304), new Point(495, 321), new Point(496, 334), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(496, 336), new Point(495, 350), new Point(479, 371), new Point(468, 378), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(452, 388, 441, 397);

        // Neck
        g.drawLine(167, 446, 175, 510);
        g.drawLine(175, 510, 397, 511);
        g.drawLine(397, 511, 404, 450);

        // Face Shadow
        drawArc(g, new Point(114, 304), new Point(125, 290), new Point(132, 289), new Point(152, 302), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(152, 302), new Point(193, 326), new Point(216, 322), new Point(279, 267), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(279, 267), new Point(284, 263), new Point(294, 263), new Point(300, 267), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(300, 267), new Point(345, 298), new Point(381, 349), new Point(458, 282), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(458, 282), new Point(460, 283), new Point(465, 286), new Point(466, 290), 1,
                MyColor.EERIE_BLACK);

        // Hair
        g.drawLine(122, 222, 55, 249);
        drawArc(g, new Point(55, 249), new Point(55, 293), new Point(34, 334), new Point(19, 343), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(19, 343), new Point(29, 343), new Point(56, 335), new Point(66, 315), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(66, 315), new Point(63, 326), new Point(62, 361), new Point(68, 393), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(68, 393), new Point(73, 382), new Point(76, 364), new Point(81, 362), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(81, 362), new Point(83, 374), new Point(86, 401), new Point(84, 415), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(84, 415), new Point(89, 410), new Point(99, 399), new Point(99, 393), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(99, 393), new Point(99, 399), new Point(99, 434), new Point(114, 460), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(114, 460), new Point(114, 452), new Point(116, 433), new Point(122, 425), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(122, 425), new Point(125, 435), new Point(131, 455), new Point(138, 465), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(138, 465), new Point(140, 459), new Point(145, 447), new Point(145, 442), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(145, 442), new Point(145, 437), new Point(150, 455), new Point(153, 466), 1,
                MyColor.EERIE_BLACK);

        drawArc(g, new Point(460, 222), new Point(520, 237), new Point(517, 253), new Point(517, 260), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(517, 260), new Point(517, 284), new Point(526, 332), new Point(543, 338), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(543, 338), new Point(535, 336), new Point(518, 330), new Point(514, 327), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(514, 327), new Point(513, 342), new Point(513, 378), new Point(518, 400), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(518, 400), new Point(512, 394), new Point(498, 379), new Point(496, 371), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(496, 371), new Point(494, 361), new Point(497, 415), new Point(512, 429), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(512, 429), new Point(499, 429), new Point(477, 417), new Point(470, 403), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(470, 403), new Point(470, 411), new Point(468, 429), new Point(454, 440), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(454, 440), new Point(454, 445), new Point(455, 457), new Point(463, 470), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(463, 470), new Point(455, 466), new Point(438, 455), new Point(433, 443), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(433, 443), new Point(432, 449), new Point(429, 461), new Point(433, 471), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(433, 471), new Point(430, 468), new Point(423, 458), new Point(421, 448), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(421, 448), new Point(421, 452), new Point(420, 460), new Point(418, 463), 1,
                MyColor.EERIE_BLACK);

        // Nose
        g.drawLine(298, 286, 298, 318);
        g.drawLine(281, 266, 281, 318);
        drawArc(g, new Point(281, 318), new Point(281, 323), new Point(281, 329), new Point(289, 328), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(282, 315, 289, 310);
        g.drawLine(282, 319, 290, 313);
        g.drawLine(285, 320, 293, 314);
        g.drawLine(292, 319, 295, 317);
        drawArc(g, new Point(272, 339), new Point(273, 338), new Point(277, 337), new Point(279, 338), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(300, 340), new Point(302, 338), new Point(306, 337), new Point(308, 337), 1,
                MyColor.EERIE_BLACK);

        // Mouth
        drawArc(g, new Point(145, 337), new Point(149, 344), new Point(196, 366), new Point(208, 365), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(208, 365, 365, 365);
        drawArc(g, new Point(365, 365), new Point(374, 366), new Point(424, 346), new Point(435, 335), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(430, 339), new Point(411, 404), new Point(374, 467), new Point(291, 463), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(291, 463), new Point(189, 461), new Point(158, 409), new Point(149, 341), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(168, 397), new Point(207, 409), new Point(369, 413), new Point(404, 397), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(290, 365, 290, 403);
        drawArc(g, new Point(245, 370), new Point(245, 381), new Point(247, 396), new Point(248, 403), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(203, 368), new Point(204, 378), new Point(206, 397), new Point(207, 401), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(165, 358), new Point(167, 370), new Point(171, 391), new Point(174, 394), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(336, 368), new Point(336, 377), new Point(334, 395), new Point(332, 404), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(376, 366), new Point(374, 377), new Point(368, 398), new Point(366, 402), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(412, 356, 398, 395);
        g.drawLine(290, 412, 290, 459);
        drawArc(g, new Point(248, 412), new Point(245, 425), new Point(243, 449), new Point(243, 457), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(209, 407), new Point(207, 407), new Point(205, 428), new Point(205, 438), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(175, 402, 174, 413);
        g.drawLine(334, 412, 335, 453);
        g.drawLine(368, 408, 368, 441);
        g.drawLine(397, 402, 398, 408);
        drawArc(g, new Point(145, 324), new Point(143, 325), new Point(136, 328), new Point(136, 332), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(431, 323), new Point(440, 326), new Point(445, 327), new Point(445, 332), 1,
                MyColor.EERIE_BLACK);

        // Coat
        drawArc(g, new Point(40, 436), new Point(65, 435), new Point(117, 458), new Point(170, 472), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(96, 449, 117, 510);
        g.drawLine(460, 450, 441, 511);
        g.drawLine(40, 436, 40, 510);
        g.drawLine(40, 510, 175, 510);
        drawArc(g, new Point(402, 468), new Point(446, 463), new Point(503, 424), new Point(546, 423), 1,
                MyColor.EERIE_BLACK);
        drawArc(g, new Point(546, 423), new Point(538, 437), new Point(527, 459), new Point(527, 486), 1,
                MyColor.EERIE_BLACK);
        g.drawLine(527, 486, 527, 510);
        g.drawLine(527, 510, 397, 511);

        buffer = floodFill(buffer, new Point(307, 226), MyColor.PLACEHOLDER, MyColor.OIL);

        buffer = floodFill(buffer, new Point(246, 328), MyColor.PLACEHOLDER, MyColor.TACAO);

        buffer = floodFill(buffer, new Point(487, 249), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(96, 253), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(113, 404), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);

        buffer = floodFill(buffer, new Point(76, 324), MyColor.PLACEHOLDER, MyColor.TACAO);
        buffer = floodFill(buffer, new Point(502, 325), MyColor.PLACEHOLDER, MyColor.TACAO);

        buffer = floodFill(buffer, new Point(185, 491), MyColor.PLACEHOLDER, MyColor.DARK_TACAO);

        buffer = floodFill(buffer, new Point(128, 483), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(428, 489), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(66, 481), MyColor.PLACEHOLDER, MyColor.EERIE);
        buffer = floodFill(buffer, new Point(490, 476), MyColor.PLACEHOLDER, MyColor.EERIE);

        buffer = floodFill(buffer, new Point(113, 455), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(430, 463), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(451, 457), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(458, 453), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(458, 465), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);
        buffer = floodFill(buffer, new Point(460, 467), MyColor.PLACEHOLDER, MyColor.EERIE_BLACK);

        buffer = floodFill(buffer, new Point(312, 385), MyColor.PLACEHOLDER, Color.WHITE);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawRogerFaceSide1() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        // Face
        drawArc(g, new Point(600, 514), new Point(598, 553), new Point(583, 552), new Point(515, 548), 1,
                MyColor.BLACK);
        drawArc(g, new Point(515, 548), new Point(479, 546), new Point(398, 504), new Point(390, 502), 1,
                MyColor.BLACK);
        drawArc(g, new Point(390, 502), new Point(360, 491), new Point(335, 496), new Point(306, 405), 1,
                MyColor.BLACK);
        drawArc(g, new Point(306, 405), new Point(295, 430), new Point(267, 413), new Point(255, 401), 1,
                MyColor.BLACK);
        drawArc(g, new Point(255, 401), new Point(237, 384), new Point(195, 371), new Point(184, 336), 1,
                MyColor.BLACK);
        drawArc(g, new Point(184, 336), new Point(160, 225), new Point(235, 173), new Point(302, 390), 1,
                MyColor.BLACK);
        drawArc(g, new Point(302, 390), new Point(290, 333), new Point(273, 228), new Point(295, 218), 1,
                MyColor.BLACK);
        drawArc(g, new Point(295, 218), new Point(361, 117), new Point(480, 96), new Point(600, 185), 1, MyColor.BLACK);

        return buffer;
    }

    private BufferedImage drawLuffyBoy() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        // Face
        drawArc(g, new Point(160, 282), new Point(173, 84), new Point(446, 87), new Point(446, 288), 1, MyColor.BLACK);
        g.drawLine(160, 282, 161, 342);
        drawArc(g, new Point(161, 342), new Point(166, 364), new Point(178, 410), new Point(186, 422), 1,
                MyColor.BLACK);
        drawArc(g, new Point(186, 422), new Point(197, 437), new Point(197, 439), new Point(222, 458), 1,
                MyColor.BLACK);
        drawArc(g, new Point(222, 458), new Point(248, 477), new Point(285, 499), new Point(311, 502), 1,
                MyColor.BLACK);
        drawArc(g, new Point(311, 502), new Point(334, 504), new Point(404, 447), new Point(408, 442), 1,
                MyColor.BLACK);
        drawArc(g, new Point(408, 442), new Point(414, 437), new Point(426, 423), new Point(432, 409), 1,
                MyColor.BLACK);
        drawArc(g, new Point(432, 409), new Point(437, 394), new Point(445, 368), new Point(449, 357), 1,
                MyColor.BLACK);
        g.drawLine(449, 357, 453, 338);
        drawArc(g, new Point(453, 338), new Point(454, 324), new Point(448, 294), new Point(446, 288), 1,
                MyColor.BLACK);
        g.drawLine(159, 274, 446, 278);

        // eyebrows
        g.drawLine(270, 317, 209, 285);
        g.drawLine(357, 310, 389, 301);
        g.drawLine(399, 297, 421, 286);

        // eyes
        drawArc(g, new Point(193, 330), new Point(192, 298), new Point(251, 289), new Point(264, 330), 1,
                MyColor.BLACK);
        drawArc(g, new Point(196, 354), new Point(199, 358), new Point(208, 367), new Point(221, 370), 1,
                MyColor.BLACK);
        drawArc(g, new Point(221, 370), new Point(229, 372), new Point(255, 371), new Point(263, 355), 1,
                MyColor.BLACK);
        drawArc(g, new Point(193, 330), new Point(192, 334), new Point(191, 345), new Point(196, 354), 1, Color.WHITE); // **
        drawArc(g, new Point(264, 330), new Point(265, 334), new Point(266, 345), new Point(263, 355), 1, Color.WHITE); // **
        drawArc(g, new Point(222, 326), new Point(223, 319), new Point(237, 313), new Point(242, 326), 1,
                MyColor.BLACK);
        drawArc(g, new Point(242, 326), new Point(244, 336), new Point(238, 339), new Point(235, 340), 1,
                MyColor.BLACK);
        drawArc(g, new Point(235, 340), new Point(233, 340), new Point(230, 340), new Point(229, 340), 1,
                MyColor.BLACK);
        drawArc(g, new Point(229, 340), new Point(225, 340), new Point(223, 335), new Point(222, 333), 1,
                MyColor.BLACK);
        drawArc(g, new Point(222, 333), new Point(221, 331), new Point(222, 328), new Point(222, 326), 1,
                MyColor.BLACK);
        drawArc(g, new Point(357, 331), new Point(359, 305), new Point(412, 282), new Point(428, 328), 1,
                MyColor.BLACK);
        drawArc(g, new Point(360, 358), new Point(361, 369), new Point(408, 383), new Point(425, 351), 1,
                MyColor.BLACK);
        drawArc(g, new Point(357, 331), new Point(356, 334), new Point(355, 346), new Point(360, 359), 1, Color.WHITE); // **
        drawArc(g, new Point(428, 328), new Point(428, 333), new Point(429, 345), new Point(425, 351), 1, Color.WHITE); // **
        drawArc(g, new Point(378, 328), new Point(379, 322), new Point(392, 314), new Point(399, 328), 1,
                MyColor.BLACK);
        drawArc(g, new Point(399, 328), new Point(401, 336), new Point(396, 341), new Point(393, 342), 1,
                MyColor.BLACK);
        drawArc(g, new Point(393, 342), new Point(387, 342), new Point(384, 341), new Point(384, 341), 1,
                MyColor.BLACK);
        drawArc(g, new Point(384, 341), new Point(380, 340), new Point(378, 336), new Point(378, 335), 1,
                MyColor.BLACK);
        drawArc(g, new Point(378, 335), new Point(377, 332), new Point(377, 329), new Point(378, 328), 1,
                MyColor.BLACK);

        // hair
        drawArc(g, new Point(163, 274), new Point(166, 295), new Point(174, 340), new Point(180, 347), 1,
                MyColor.BLACK);
        drawArc(g, new Point(180, 347), new Point(182, 322), new Point(186, 274), new Point(190, 274), 1,
                MyColor.BLACK);
        drawArc(g, new Point(226, 275), new Point(230, 285), new Point(241, 307), new Point(247, 315), 1,
                MyColor.BLACK);
        drawArc(g, new Point(247, 315), new Point(243, 304), new Point(237, 280), new Point(239, 275), 1,
                MyColor.BLACK);
        drawArc(g, new Point(306, 276), new Point(313, 291), new Point(332, 323), new Point(350, 332), 1,
                MyColor.BLACK);
        drawArc(g, new Point(350, 332), new Point(344, 319), new Point(332, 289), new Point(336, 276), 1,
                MyColor.BLACK);
        drawArc(g, new Point(376, 277), new Point(382, 288), new Point(396, 313), new Point(398, 321), 1,
                MyColor.BLACK);
        drawArc(g, new Point(398, 321), new Point(400, 329), new Point(398, 295), new Point(398, 277), 1,
                MyColor.BLACK);
        drawArc(g, new Point(419, 278), new Point(424, 291), new Point(435, 319), new Point(435, 328), 1,
                MyColor.BLACK);
        drawArc(g, new Point(435, 328), new Point(438, 324), new Point(443, 309), new Point(440, 278), 1,
                MyColor.BLACK);

        // ears
        drawArc(g, new Point(445, 368), new Point(462, 347), new Point(478, 305), new Point(484, 288), 1,
                MyColor.BLACK);
        g.drawLine(484, 288, 447, 288);
        drawArc(g, new Point(474, 316), new Point(480, 311), new Point(494, 305), new Point(497, 316), 1,
                MyColor.BLACK);
        drawArc(g, new Point(497, 316), new Point(500, 330), new Point(500, 347), new Point(494, 360), 1,
                MyColor.BLACK);
        drawArc(g, new Point(494, 360), new Point(490, 370), new Point(484, 377), new Point(481, 379), 1,
                MyColor.BLACK);
        drawArc(g, new Point(481, 379), new Point(473, 387), new Point(455, 402), new Point(445, 406), 1,
                MyColor.BLACK);
        drawArc(g, new Point(445, 406), new Point(434, 410), new Point(432, 408), new Point(432, 407), 1,
                MyColor.BLACK);
        drawArc(g, new Point(463, 341), new Point(467, 333), new Point(479, 318), new Point(487, 319), 1,
                MyColor.BLACK);
        drawArc(g, new Point(487, 319), new Point(488, 319), new Point(491, 321), new Point(490, 329), 1,
                MyColor.BLACK);
        drawArc(g, new Point(469, 333), new Point(470, 337), new Point(474, 344), new Point(484, 339), 1,
                MyColor.BLACK);
        g.drawLine(439, 388, 456, 372);
        g.drawLine(456, 372, 456, 362);
        g.drawLine(456, 362, 456, 355);
        drawArc(g, new Point(456, 355), new Point(460, 351), new Point(470, 346), new Point(474, 355), 1,
                MyColor.BLACK);
        drawArc(g, new Point(474, 355), new Point(475, 360), new Point(474, 371), new Point(465, 382), 1,
                MyColor.BLACK);
        g.drawLine(465, 382, 456, 390);
        g.drawLine(456, 390, 451, 391);
        g.drawLine(451, 391, 435, 399);
        g.drawLine(160, 281, 100, 280);
        drawArc(g, new Point(100, 280), new Point(101, 284), new Point(100, 298), new Point(86, 320), 1, MyColor.BLACK);
        drawArc(g, new Point(86, 320), new Point(92, 316), new Point(103, 306), new Point(107, 300), 1, MyColor.BLACK);
        drawArc(g, new Point(107, 300), new Point(108, 314), new Point(110, 344), new Point(106, 349), 1,
                MyColor.BLACK);
        g.drawLine(106, 349, 117, 338);
        drawArc(g, new Point(169, 376), new Point(163, 358), new Point(150, 320), new Point(146, 318), 1,
                MyColor.BLACK);
        drawArc(g, new Point(146, 318), new Point(142, 311), new Point(127, 303), new Point(119, 320), 1,
                MyColor.BLACK);
        drawArc(g, new Point(119, 320), new Point(117, 327), new Point(115, 343), new Point(119, 354), 1,
                MyColor.BLACK);
        drawArc(g, new Point(119, 354), new Point(124, 368), new Point(145, 400), new Point(172, 413), 1,
                MyColor.BLACK);
        drawArc(g, new Point(172, 413), new Point(174, 413), new Point(179, 413), new Point(180, 410), 1,
                MyColor.BLACK);
        drawArc(g, new Point(176, 399), new Point(170, 394), new Point(158, 383), new Point(156, 378), 1,
                MyColor.BLACK);
        drawArc(g, new Point(156, 378), new Point(154, 372), new Point(156, 360), new Point(158, 355), 1,
                MyColor.BLACK);
        drawArc(g, new Point(157, 354), new Point(153, 352), new Point(145, 349), new Point(140, 352), 1,
                MyColor.BLACK);
        g.drawLine(140, 352, 140, 370);
        drawArc(g, new Point(140, 370), new Point(141, 374), new Point(144, 382), new Point(152, 388), 1,
                MyColor.BLACK);
        drawArc(g, new Point(152, 388), new Point(161, 394), new Point(162, 394), new Point(167, 400), 1,
                MyColor.BLACK);
        g.drawLine(157, 354, 146, 334);
        drawArc(g, new Point(146, 334), new Point(132, 311), new Point(121, 326), new Point(124, 339), 1,
                MyColor.BLACK);
        g.drawLine(147, 336, 136, 345);
        g.drawLine(136, 345, 128, 338);
        drawArc(g, new Point(490, 329), new Point(489, 335), new Point(488, 336), new Point(484, 339), 1,
                MyColor.SKIN_LUFFY_SHADOW);
        g.drawLine(167, 400, 177, 400);

        // nose
        drawArc(g, new Point(289, 326), new Point(291, 327), new Point(294, 329), new Point(295, 333), 1,
                MyColor.BLACK);
        g.drawLine(295, 333, 297, 399);
        drawArc(g, new Point(297, 399), new Point(297, 400), new Point(298, 402), new Point(300, 403), 1,
                MyColor.BLACK);
        g.drawLine(316, 405, 320, 400);

        // mouth
        drawArc(g, new Point(229, 421), new Point(251, 428), new Point(286, 444), new Point(367, 433), 1,
                MyColor.BLACK);
        drawArc(g, new Point(367, 433), new Point(371, 432), new Point(382, 430), new Point(389, 426), 1,
                MyColor.BLACK);

        // scar
        drawArc(g, new Point(368, 387), new Point(379, 389), new Point(406, 389), new Point(419, 381), 1,
                MyColor.BLACK);
        drawArc(g, new Point(383, 385), new Point(383, 387), new Point(383, 390), new Point(385, 394), 1,
                MyColor.BLACK);
        drawArc(g, new Point(409, 379), new Point(409, 382), new Point(410, 387), new Point(412, 391), 1,
                MyColor.BLACK);

        // body
        drawArc(g, new Point(219, 456), new Point(227, 480), new Point(229, 522), new Point(226, 534), 1,
                MyColor.BLACK);
        drawArc(g, new Point(396, 453), new Point(394, 472), new Point(389, 516), new Point(391, 538), 1,
                MyColor.BLACK);
        drawArc(g, new Point(227, 518), new Point(234, 529), new Point(253, 555), new Point(276, 567), 1,
                MyColor.SKIN_LUFFY_SHADOW);
        drawArc(g, new Point(276, 567), new Point(280, 569), new Point(293, 579), new Point(310, 563), 1,
                MyColor.SKIN_LUFFY_SHADOW);
        drawArc(g, new Point(310, 563), new Point(334, 550), new Point(382, 521), new Point(391, 506), 1,
                MyColor.SKIN_LUFFY_SHADOW);
        drawArc(g, new Point(227, 519), new Point(220, 527), new Point(199, 544), new Point(180, 549), 1,
                MyColor.BLACK);
        drawArc(g, new Point(180, 549), new Point(174, 559), new Point(161, 583), new Point(158, 599), 1,
                MyColor.BLACK);
        g.drawLine(201, 540, 195, 537);
        drawArc(g, new Point(195, 537), new Point(190, 535), new Point(186, 538), new Point(184, 539), 1,
                MyColor.BLACK);
        drawArc(g, new Point(184, 539), new Point(179, 544), new Point(178, 551), new Point(177, 554), 1,
                MyColor.BLACK);
        drawArc(g, new Point(215, 531), new Point(207, 526), new Point(190, 517), new Point(188, 517), 1,
                MyColor.BLACK);
        drawArc(g, new Point(188, 517), new Point(185, 516), new Point(179, 515), new Point(175, 520), 1,
                MyColor.BLACK);
        drawArc(g, new Point(175, 520), new Point(171, 526), new Point(159, 539), new Point(157, 542), 1,
                MyColor.BLACK);
        drawArc(g, new Point(160, 538), new Point(141, 547), new Point(98, 567), new Point(88, 568), 1, MyColor.BLACK);
        g.drawLine(88, 568, 77, 568);
        drawArc(g, new Point(77, 568), new Point(75, 570), new Point(72, 580), new Point(70, 599), 1, MyColor.BLACK);
        drawArc(g, new Point(73, 580), new Point(58, 583), new Point(20, 590), new Point(13, 600), 1, MyColor.BLACK);
        g.drawLine(13, 600, 590, 600);
        drawArc(g, new Point(153, 557), new Point(149, 564), new Point(140, 581), new Point(137, 596), 1,
                MyColor.BLACK);
        g.drawLine(92, 593, 92, 599);
        g.drawLine(359, 573, 351, 599);
        drawArc(g, new Point(391, 519), new Point(404, 531), new Point(415, 541), new Point(426, 545), 1,
                MyColor.BLACK);
        g.drawLine(426, 545, 444, 553);
        drawArc(g, new Point(444, 553), new Point(446, 553), new Point(451, 556), new Point(453, 560), 1,
                MyColor.BLACK);
        drawArc(g, new Point(453, 560), new Point(459, 568), new Point(469, 584), new Point(473, 593), 1,
                MyColor.BLACK);
        g.drawLine(473, 593, 473, 599);
        drawArc(g, new Point(410, 535), new Point(416, 529), new Point(431, 518), new Point(439, 516), 1,
                MyColor.BLACK);
        drawArc(g, new Point(439, 516), new Point(441, 515), new Point(457, 526), new Point(464, 535), 1,
                MyColor.BLACK);
        drawArc(g, new Point(464, 535), new Point(478, 555), new Point(495, 588), new Point(498, 599), 1,
                MyColor.BLACK);
        g.drawLine(426, 545, 439, 538);
        g.drawLine(439, 538, 453, 560);
        drawArc(g, new Point(473, 548), new Point(483, 555), new Point(512, 571), new Point(544, 578), 1,
                MyColor.BLACK);
        drawArc(g, new Point(544, 578), new Point(551, 578), new Point(566, 578), new Point(567, 578), 1,
                MyColor.BLACK);
        drawArc(g, new Point(567, 578), new Point(569, 580), new Point(574, 588), new Point(577, 599), 1,
                MyColor.BLACK);
        drawArc(g, new Point(575, 594), new Point(579, 595), new Point(587, 598), new Point(590, 600), 1,
                MyColor.BLACK);
        g.drawLine(173, 413, 174, 434);
        g.drawLine(174, 434, 184, 419);
        g.drawLine(191, 429, 196, 465);
        drawArc(g, new Point(196, 465), new Point(198, 463), new Point(203, 456), new Point(207, 447), 1,
                MyColor.BLACK);
        drawArc(g, new Point(209, 448), new Point(211, 454), new Point(216, 469), new Point(224, 481), 1,
                MyColor.BLACK);
        g.drawLine(438, 409, 438, 437);
        drawArc(g, new Point(438, 437), new Point(435, 435), new Point(427, 427), new Point(426, 421), 1,
                MyColor.BLACK);
        g.drawLine(426, 421, 422, 449);
        drawArc(g, new Point(422, 449), new Point(420, 446), new Point(416, 439), new Point(416, 434), 1,
                MyColor.BLACK);
        g.drawLine(416, 434, 404, 463);
        drawArc(g, new Point(404, 463), new Point(403, 460), new Point(401, 454), new Point(401, 449), 1,
                MyColor.BLACK);

        buffer = floodFill(buffer, new Point(133, 296), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(174, 291), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(235, 286), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(241, 303), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(322, 290), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(389, 287), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(394, 307), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(432, 292), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(464, 304), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(178, 422), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(199, 451), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(218, 463), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(406, 453), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(421, 438), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(433, 423), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(229, 354), MyColor.PLACEHOLDER, Color.WHITE);
        buffer = floodFill(buffer, new Point(384, 350), MyColor.PLACEHOLDER, Color.WHITE);
        buffer = floodFill(buffer, new Point(230, 331), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(387, 333), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(257, 400), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(219, 561), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(410, 561), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(405, 285), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(486, 352), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(448, 371), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(127, 354), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(54, 593), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(580, 599), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_COLOR);
        buffer = floodFill(buffer, new Point(296, 530), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_SHADOW);
        buffer = floodFill(buffer, new Point(481, 330), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_SHADOW);
        buffer = floodFill(buffer, new Point(462, 369), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_SHADOW);
        buffer = floodFill(buffer, new Point(149, 367), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_SHADOW);
        buffer = floodFill(buffer, new Point(163, 554), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY);
        buffer = floodFill(buffer, new Point(469, 563), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY);
        buffer = floodFill(buffer, new Point(493, 567), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY);
        buffer = floodFill(buffer, new Point(190, 543), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_SHADOW);
        buffer = floodFill(buffer, new Point(438, 547), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_SHADOW);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawLuffyChild() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        g.setColor(MyColor.SKIN_LUFFY_CHILD_SHADOW);
        g.drawLine(176, 304, 228, 279);
        g.drawLine(271, 258, 306, 246);

        // eyes
        drawArc(g, new Point(172, 237), new Point(174, 239), new Point(179, 244), new Point(181, 249), 1,
                MyColor.BLACK);
        drawArc(g, new Point(168, 264), new Point(168, 252), new Point(175, 242), new Point(184, 263), 1,
                MyColor.BLACK);
        drawArc(g, new Point(173, 283), new Point(175, 285), new Point(179, 290), new Point(182, 288), 1,
                MyColor.BLACK);
        drawArc(g, new Point(176, 264), new Point(177, 261), new Point(180, 258), new Point(183, 265), 1,
                MyColor.BLACK);
        drawArc(g, new Point(183, 265), new Point(183, 266), new Point(184, 270), new Point(182, 273), 1,
                MyColor.BLACK);
        drawArc(g, new Point(182, 273), new Point(181, 274), new Point(180, 274), new Point(179, 274), 1,
                MyColor.BLACK);
        drawArc(g, new Point(179, 274), new Point(175, 271), new Point(176, 266), new Point(176, 264), 1,
                MyColor.BLACK);
        g.drawLine(220, 250, 222, 253);
        drawArc(g, new Point(231, 255), new Point(236, 250), new Point(248, 240), new Point(254, 236), 1,
                MyColor.BLACK);
        drawArc(g, new Point(254, 236), new Point(257, 233), new Point(263, 229), new Point(266, 228), 1,
                MyColor.BLACK);
        g.drawLine(218, 254, 222, 259);
        drawArc(g, new Point(228, 264), new Point(231, 253), new Point(250, 244), new Point(268, 254), 1,
                MyColor.BLACK);
        drawArc(g, new Point(233, 287), new Point(237, 291), new Point(257, 295), new Point(269, 283), 1,
                MyColor.BLACK);
        drawArc(g, new Point(269, 283), new Point(272, 281), new Point(277, 265), new Point(269, 256), 1, Color.WHITE); // **
        drawArc(g, new Point(233, 287), new Point(226, 280), new Point(227, 268), new Point(228, 264), 1, Color.WHITE); // **
        drawArc(g, new Point(241, 263), new Point(242, 261), new Point(245, 259), new Point(249, 263), 1,
                MyColor.BLACK);
        drawArc(g, new Point(249, 263), new Point(249, 264), new Point(250, 268), new Point(249, 271), 1,
                MyColor.BLACK);
        drawArc(g, new Point(249, 271), new Point(248, 273), new Point(246, 273), new Point(245, 274), 1,
                MyColor.BLACK);
        drawArc(g, new Point(245, 274), new Point(242, 274), new Point(241, 272), new Point(241, 271), 1,
                MyColor.BLACK);
        drawArc(g, new Point(241, 271), new Point(239, 268), new Point(240, 264), new Point(241, 263), 1,
                MyColor.BLACK);
        drawArc(g, new Point(235, 298), new Point(242, 301), new Point(258, 304), new Point(272, 291), 1,
                MyColor.BLACK);
        g.drawLine(246, 296, 246, 303);
        drawArc(g, new Point(263, 293), new Point(264, 294), new Point(266, 296), new Point(266, 299), 1,
                MyColor.BLACK);

        // nose
        drawArc(g, new Point(183, 255), new Point(188, 274), new Point(183, 289), new Point(178, 303), 1,
                MyColor.BLACK);
        drawArc(g, new Point(178, 303), new Point(178, 308), new Point(189, 310), new Point(197, 312), 1,
                MyColor.BLACK);
        g.drawLine(206, 307, 211, 307);

        // mouth
        drawArc(g, new Point(197, 328), new Point(213, 330), new Point(248, 324), new Point(264, 317), 1,
                MyColor.BLACK);
        drawArc(g, new Point(264, 317), new Point(270, 316), new Point(281, 317), new Point(280, 328), 1,
                MyColor.BLACK);
        drawArc(g, new Point(280, 328), new Point(279, 330), new Point(276, 335), new Point(272, 335), 1,
                MyColor.BLACK);
        g.drawLine(272, 335, 254, 337);
        drawArc(g, new Point(254, 337), new Point(224, 338), new Point(205, 331), new Point(197, 328), 1,
                MyColor.BLACK);
        drawArc(g, new Point(249, 331), new Point(255, 330), new Point(263, 329), new Point(267, 326), 1,
                MyColor.BLACK);
        drawArc(g, new Point(267, 326), new Point(269, 325), new Point(270, 323), new Point(271, 322), 1,
                MyColor.BLACK);
        g.drawLine(267, 326, 273, 326);
        drawArc(g, new Point(276, 323), new Point(275, 324), new Point(273, 325), new Point(273, 326), 1,
                MyColor.BLACK);
        drawArc(g, new Point(273, 326), new Point(273, 327), new Point(273, 328), new Point(275, 329), 1,
                MyColor.BLACK);

        // hair
        drawArc(g, new Point(119, 222), new Point(182, 232), new Point(386, 217), new Point(414, 212), 1,
                MyColor.BLACK);
        drawArc(g, new Point(119, 222), new Point(119, 228), new Point(115, 244), new Point(99, 255), 1, MyColor.BLACK);
        drawArc(g, new Point(99, 255), new Point(105, 252), new Point(120, 243), new Point(131, 230), 1, MyColor.BLACK);
        drawArc(g, new Point(131, 230), new Point(132, 245), new Point(132, 277), new Point(120, 289), 1,
                MyColor.BLACK);
        drawArc(g, new Point(120, 289), new Point(126, 284), new Point(139, 270), new Point(145, 256), 1,
                MyColor.BLACK);
        drawArc(g, new Point(145, 256), new Point(147, 265), new Point(151, 283), new Point(157, 287), 1,
                MyColor.BLACK);
        drawArc(g, new Point(157, 287), new Point(157, 285), new Point(156, 279), new Point(158, 275), 1,
                MyColor.BLACK);
        drawArc(g, new Point(158, 275), new Point(160, 280), new Point(166, 290), new Point(174, 295), 1,
                MyColor.BLACK);
        drawArc(g, new Point(325, 285), new Point(325, 273), new Point(325, 245), new Point(319, 231), 1,
                MyColor.BLACK);
        drawArc(g, new Point(319, 231), new Point(317, 241), new Point(312, 264), new Point(307, 269), 1,
                MyColor.BLACK);
        drawArc(g, new Point(307, 269), new Point(307, 259), new Point(307, 235), new Point(299, 222), 1,
                MyColor.BLACK);
        drawArc(g, new Point(280, 223), new Point(279, 230), new Point(274, 248), new Point(268, 258), 1,
                MyColor.BLACK);
        drawArc(g, new Point(268, 258), new Point(268, 249), new Point(268, 230), new Point(266, 224), 1,
                MyColor.BLACK);
        drawArc(g, new Point(242, 225), new Point(238, 237), new Point(227, 264), new Point(221, 269), 1,
                MyColor.BLACK);
        drawArc(g, new Point(221, 269), new Point(222, 260), new Point(224, 238), new Point(221, 225), 1,
                MyColor.BLACK);
        drawArc(g, new Point(214, 225), new Point(206, 233), new Point(199, 260), new Point(197, 272), 1,
                MyColor.BLACK);
        drawArc(g, new Point(197, 272), new Point(191, 261), new Point(190, 236), new Point(190, 225), 1,
                MyColor.BLACK);
        drawArc(g, new Point(342, 306), new Point(343, 317), new Point(342, 328), new Point(341, 332), 1,
                MyColor.BLACK);
        drawArc(g, new Point(341, 332), new Point(344, 327), new Point(350, 315), new Point(352, 310), 1,
                MyColor.BLACK);
        g.drawLine(352, 310, 360, 324);
        drawArc(g, new Point(360, 324), new Point(355, 315), new Point(383, 289), new Point(391, 271), 1,
                MyColor.BLACK);
        g.drawLine(391, 271, 393, 283);
        g.drawLine(393, 283, 403, 252);
        drawArc(g, new Point(403, 252), new Point(406, 259), new Point(407, 272), new Point(407, 278), 1,
                MyColor.BLACK);
        drawArc(g, new Point(407, 278), new Point(411, 268), new Point(411, 245), new Point(411, 235), 1,
                MyColor.BLACK);
        g.drawLine(411, 235, 422, 242);
        drawArc(g, new Point(422, 242), new Point(416, 236), new Point(414, 219), new Point(414, 212), 1,
                MyColor.BLACK);

        // face
        drawArc(g, new Point(179, 226), new Point(176, 229), new Point(168, 239), new Point(164, 252), 1,
                MyColor.BLACK);
        drawArc(g, new Point(164, 252), new Point(167, 260), new Point(173, 281), new Point(175, 295), 1,
                MyColor.BLACK);
        drawArc(g, new Point(175, 295), new Point(176, 309), new Point(176, 335), new Point(218, 355), 1,
                MyColor.BLACK);
        drawArc(g, new Point(218, 355), new Point(227, 358), new Point(255, 360), new Point(291, 346), 1,
                MyColor.BLACK);
        drawArc(g, new Point(291, 346), new Point(302, 341), new Point(326, 327), new Point(332, 305), 1,
                MyColor.BLACK);

        // ears
        drawArc(g, new Point(332, 305), new Point(332, 308), new Point(334, 311), new Point(342, 305), 1,
                MyColor.BLACK);
        drawArc(g, new Point(342, 305), new Point(353, 300), new Point(374, 282), new Point(368, 257), 1,
                MyColor.BLACK);
        drawArc(g, new Point(368, 257), new Point(366, 250), new Point(361, 237), new Point(352, 243), 1,
                MyColor.BLACK);
        drawArc(g, new Point(352, 243), new Point(340, 245), new Point(336, 263), new Point(325, 285), 1,
                MyColor.BLACK);
        drawArc(g, new Point(340, 274), new Point(354, 265), new Point(356, 276), new Point(354, 282), 1,
                MyColor.BLACK);
        g.drawLine(354, 282, 347, 290);
        g.drawLine(347, 290, 340, 298);
        drawArc(g, new Point(340, 298), new Point(335, 300), new Point(333, 297), new Point(333, 295), 1,
                MyColor.SKIN_LUFFY_SHADOW);
        g.drawLine(333, 295, 340, 285);
        drawArc(g, new Point(340, 285), new Point(341, 281), new Point(335, 275), new Point(337, 269), 1,
                MyColor.BLACK);
        drawArc(g, new Point(337, 269), new Point(338, 262), new Point(345, 255), new Point(348, 252), 1,
                MyColor.BLACK);
        drawArc(g, new Point(348, 252), new Point(356, 245), new Point(359, 252), new Point(360, 256), 1,
                MyColor.BLACK);
        drawArc(g, new Point(360, 256), new Point(359, 259), new Point(357, 261), new Point(357, 261), 1,
                MyColor.SKIN_LUFFY_SHADOW);
        drawArc(g, new Point(357, 261), new Point(350, 267), new Point(344, 261), new Point(343, 258), 1,
                MyColor.BLACK);
        drawArc(g, new Point(347, 263), new Point(345, 265), new Point(345, 269), new Point(346, 271), 1,
                MyColor.SKIN_LUFFY_SHADOW);

        // // body
        drawArc(g, new Point(270, 353), new Point(279, 365), new Point(271, 383), new Point(273, 387), 1,
                MyColor.BLACK);
        drawArc(g, new Point(266, 394), new Point(287, 397), new Point(335, 332), new Point(341, 331), 1,
                MyColor.SKIN_LUFFY_CHILD_SHADOW);
        drawArc(g, new Point(274, 373), new Point(271, 373), new Point(266, 383), new Point(264, 388), 1,
                MyColor.BLACK);
        drawArc(g, new Point(264, 388), new Point(263, 395), new Point(270, 396), new Point(273, 395), 1,
                MyColor.BLACK);
        drawArc(g, new Point(273, 395), new Point(294, 393), new Point(333, 370), new Point(349, 350), 1,
                MyColor.BLACK);
        drawArc(g, new Point(349, 350), new Point(353, 346), new Point(352, 338), new Point(351, 335), 1,
                MyColor.BLACK);
        drawArc(g, new Point(351, 335), new Point(349, 332), new Point(348, 323), new Point(348, 319), 1,
                MyColor.BLACK);
        drawArc(g, new Point(274, 363), new Point(265, 372), new Point(251, 389), new Point(251, 393), 1,
                MyColor.BLACK);
        drawArc(g, new Point(251, 393), new Point(250, 396), new Point(249, 402), new Point(251, 408), 1,
                MyColor.BLACK);
        drawArc(g, new Point(251, 408), new Point(252, 411), new Point(256, 413), new Point(264, 413), 1,
                MyColor.BLACK);
        drawArc(g, new Point(264, 413), new Point(269, 413), new Point(281, 413), new Point(297, 405), 1,
                MyColor.BLACK);
        drawArc(g, new Point(297, 405), new Point(312, 398), new Point(350, 383), new Point(365, 349), 1,
                MyColor.BLACK);
        drawArc(g, new Point(365, 349), new Point(365, 345), new Point(361, 337), new Point(351, 335), 1,
                MyColor.BLACK);
        drawArc(g, new Point(266, 371), new Point(253, 381), new Point(223, 392), new Point(221, 413), 1,
                MyColor.BLACK);
        drawArc(g, new Point(221, 413), new Point(218, 425), new Point(211, 454), new Point(213, 469), 1,
                MyColor.BLACK);
        drawArc(g, new Point(213, 469), new Point(213, 470), new Point(215, 473), new Point(217, 472), 1,
                MyColor.BLACK);
        drawArc(g, new Point(225, 421), new Point(225, 432), new Point(221, 459), new Point(217, 472), 1,
                MyColor.BLACK);
        drawArc(g, new Point(217, 472), new Point(210, 506), new Point(197, 580), new Point(201, 600), 1,
                MyColor.BLACK);
        drawArc(g, new Point(209, 482), new Point(204, 516), new Point(195, 586), new Point(197, 600), 1,
                MyColor.BLACK);
        g.drawLine(197, 600, 427, 600);
        drawArc(g, new Point(212, 463), new Point(211, 465), new Point(208, 468), new Point(208, 470), 1,
                MyColor.BLACK);
        drawArc(g, new Point(208, 470), new Point(208, 474), new Point(208, 482), new Point(210, 483), 1,
                MyColor.BLACK);
        drawArc(g, new Point(210, 483), new Point(211, 483), new Point(213, 484), new Point(215, 483), 1,
                MyColor.BLACK);
        drawArc(g, new Point(225, 421), new Point(226, 437), new Point(229, 471), new Point(239, 479), 1,
                MyColor.SHIRT_LUFFY_CHILD_SHADOW);
        g.drawLine(242, 495, 230, 524);
        g.drawLine(226, 536, 221, 553);
        drawArc(g, new Point(221, 553), new Point(216, 577), new Point(218, 616), new Point(268, 583), 1,
                MyColor.SHIRT_LUFFY_CHILD_SHADOW);
        g.drawLine(268, 583, 258, 600);
        drawArc(g, new Point(285, 563), new Point(278, 527), new Point(261, 593), new Point(251, 600), 1,
                MyColor.BLACK);
        drawArc(g, new Point(334, 430), new Point(332, 443), new Point(333, 473), new Point(348, 490), 1,
                MyColor.BLACK);
        drawArc(g, new Point(338, 473), new Point(339, 484), new Point(348, 500), new Point(352, 507), 1,
                MyColor.SHIRT_LUFFY_CHILD_SHADOW); // **
        drawArc(g, new Point(334, 450), new Point(336, 458), new Point(343, 477), new Point(351, 484), 1,
                MyColor.SHIRT_LUFFY_CHILD_SHADOW); // **
        drawArc(g, new Point(374, 400), new Point(349, 425), new Point(349, 479), new Point(353, 503), 1,
                MyColor.BLACK);
        drawArc(g, new Point(353, 503), new Point(371, 483), new Point(413, 480), new Point(431, 481), 1,
                MyColor.BLACK);
        drawArc(g, new Point(431, 481), new Point(432, 468), new Point(427, 434), new Point(424, 419), 1,
                MyColor.BLACK);
        drawArc(g, new Point(424, 419), new Point(415, 380), new Point(375, 362), new Point(365, 350), 1,
                MyColor.BLACK);
        drawArc(g, new Point(353, 503), new Point(353, 504), new Point(353, 505), new Point(353, 507), 1,
                MyColor.BLACK);
        drawArc(g, new Point(353, 507), new Point(352, 512), new Point(353, 518), new Point(355, 518), 1,
                MyColor.BLACK);
        g.drawLine(355, 518, 367, 518);
        g.drawLine(367, 518, 367, 509);
        drawArc(g, new Point(367, 509), new Point(373, 505), new Point(393, 497), new Point(422, 496), 1,
                MyColor.BLACK);
        g.drawLine(422, 496, 422, 501);
        drawArc(g, new Point(422, 501), new Point(423, 502), new Point(428, 500), new Point(431, 499), 1,
                MyColor.BLACK);
        drawArc(g, new Point(431, 499), new Point(434, 496), new Point(432, 485), new Point(431, 481), 1,
                MyColor.BLACK);
        drawArc(g, new Point(431, 481), new Point(432, 468), new Point(427, 434), new Point(424, 419), 1,
                MyColor.BLACK);
        drawArc(g, new Point(424, 419), new Point(415, 380), new Point(375, 362), new Point(365, 350), 1,
                MyColor.BLACK);
        drawArc(g, new Point(367, 518), new Point(368, 537), new Point(372, 575), new Point(378, 583), 1,
                MyColor.BLACK);
        g.drawLine(378, 583, 375, 600);
        drawArc(g, new Point(427, 600), new Point(434, 574), new Point(422, 539), new Point(422, 501), 1,
                MyColor.BLACK);
        drawArc(g, new Point(368, 528), new Point(380, 517), new Point(409, 508), new Point(422, 505), 1,
                MyColor.SKIN_LUFFY_SHADOW);

        // character
        g.drawLine(258, 458, 262, 459);
        g.drawLine(262, 459, 262, 465);
        g.drawLine(262, 465, 238, 461);
        g.drawLine(238, 461, 238, 456);
        g.drawLine(238, 456, 245, 456);

        // N
        drawArc(g, new Point(221, 492), new Point(219, 492), new Point(215, 491), new Point(215, 484), 1,
                MyColor.BLUE_SHADOW);
        g.drawLine(217, 475, 218, 476);
        g.drawLine(218, 476, 220, 469);
        g.drawLine(220, 469, 223, 469);
        g.drawLine(223, 469, 221, 492);

        // C
        g.drawLine(236, 478, 231, 478);
        drawArc(g, new Point(231, 478), new Point(228, 472), new Point(228, 480), new Point(228, 485), 1,
                MyColor.BLUE_SHADOW);
        drawArc(g, new Point(228, 485), new Point(228, 488), new Point(229, 489), new Point(230, 488), 1,
                MyColor.BLUE_SHADOW);
        g.drawLine(230, 488, 230, 484);
        g.drawLine(230, 484, 235, 484);
        drawArc(g, new Point(235, 484), new Point(235, 488), new Point(233, 491), new Point(232, 492), 1,
                MyColor.BLUE_SHADOW);
        drawArc(g, new Point(232, 492), new Point(227, 495), new Point(224, 490), new Point(224, 488), 1,
                MyColor.BLUE_SHADOW);
        drawArc(g, new Point(224, 488), new Point(222, 478), new Point(225, 472), new Point(227, 470), 1,
                MyColor.BLUE_SHADOW);
        drawArc(g, new Point(227, 470), new Point(234, 467), new Point(236, 474), new Point(236, 478), 1,
                MyColor.BLUE_SHADOW);

        // H
        g.setColor(MyColor.BLUE_COLOR);
        g.drawLine(240, 472, 246, 472);
        g.drawLine(246, 472, 245, 481);
        g.drawLine(245, 481, 249, 481);
        g.drawLine(249, 481, 250, 473);
        g.drawLine(250, 473, 256, 473);
        g.drawLine(256, 473, 253, 496);
        g.drawLine(253, 496, 248, 496);
        g.drawLine(248, 496, 247, 496);
        g.drawLine(247, 496, 247, 486);
        g.drawLine(247, 486, 244, 486);
        g.drawLine(244, 486, 243, 495);
        g.drawLine(243, 495, 238, 495);
        g.drawLine(238, 495, 240, 472);

        // O
        drawArc(g, new Point(263, 482), new Point(264, 479), new Point(267, 475), new Point(268, 482), 1,
                MyColor.BLUE_COLOR);
        drawArc(g, new Point(268, 482), new Point(269, 487), new Point(268, 489), new Point(267, 490), 1,
                MyColor.BLUE_COLOR);
        drawArc(g, new Point(267, 490), new Point(263, 491), new Point(263, 485), new Point(263, 482), 1,
                MyColor.BLUE_COLOR);
        drawArc(g, new Point(258, 480), new Point(260, 474), new Point(269, 468), new Point(273, 478), 1,
                MyColor.BLUE_COLOR);
        g.drawLine(273, 478, 273, 487);
        drawArc(g, new Point(273, 487), new Point(271, 494), new Point(268, 497), new Point(262, 495), 1,
                MyColor.BLUE_COLOR);
        drawArc(g, new Point(262, 495), new Point(256, 494), new Point(257, 485), new Point(258, 480), 1,
                MyColor.BLUE_COLOR);

        // R
        drawArc(g, new Point(275, 469), new Point(280, 467), new Point(291, 464), new Point(293, 472), 1,
                MyColor.BLUE_COLOR);
        drawArc(g, new Point(293, 472), new Point(294, 473), new Point(293, 478), new Point(285, 482), 1,
                MyColor.BLUE_COLOR);
        g.drawLine(285, 482, 292, 491);
        g.drawLine(292, 491, 287, 495);
        g.drawLine(287, 495, 281, 485);
        g.drawLine(281, 485, 281, 497);
        g.drawLine(281, 497, 274, 497);
        g.drawLine(274, 497, 275, 469);
        drawArc(g, new Point(281, 472), new Point(282, 472), new Point(285, 472), new Point(285, 475), 1,
                MyColor.BLUE_COLOR);
        drawArc(g, new Point(285, 475), new Point(285, 478), new Point(282, 478), new Point(281, 477), 1,
                MyColor.BLUE_COLOR);
        g.drawLine(281, 477, 281, 472);

        // anchor
        drawArc(g, new Point(247, 441), new Point(248, 438), new Point(255, 434), new Point(258, 441), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(258, 441), new Point(259, 443), new Point(259, 448), new Point(255, 450), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(255, 450), new Point(253, 451), new Point(251, 450), new Point(250, 450), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(250, 450), new Point(246, 447), new Point(246, 443), new Point(247, 441), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(245, 456), new Point(240, 451), new Point(238, 438), new Point(246, 432), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(246, 432), new Point(250, 429), new Point(259, 426), new Point(264, 435), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(264, 435), new Point(266, 439), new Point(268, 449), new Point(258, 458), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(227, 498), new Point(229, 499), new Point(239, 503), new Point(251, 501), 1,
                MyColor.RED_COLOR);
        g.drawLine(251, 501, 251, 506);
        g.drawLine(251, 506, 243, 508);
        g.drawLine(243, 508, 241, 537);
        drawArc(g, new Point(241, 537), new Point(245, 539), new Point(259, 537), new Point(275, 519), 1,
                MyColor.RED_COLOR);
        g.drawLine(275, 519, 271, 512);
        g.drawLine(271, 512, 296, 501);
        g.drawLine(296, 501, 288, 528);
        g.drawLine(288, 528, 282, 524);
        drawArc(g, new Point(282, 524), new Point(276, 535), new Point(257, 550), new Point(243, 554), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(243, 554), new Point(238, 555), new Point(225, 556), new Point(217, 550), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(217, 550), new Point(214, 547), new Point(208, 540), new Point(206, 532), 1,
                MyColor.RED_COLOR);
        drawArc(g, new Point(227, 498), new Point(227, 499), new Point(226, 500), new Point(227, 503), 1,
                MyColor.RED_COLOR);
        g.drawLine(227, 503, 232, 504);
        g.drawLine(232, 504, 228, 537);
        drawArc(g, new Point(228, 537), new Point(223, 535), new Point(213, 529), new Point(209, 519), 1,
                MyColor.RED_COLOR);

        buffer = floodFill(buffer, new Point(149, 247), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(196, 236), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(226, 235), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(270, 234), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);
        buffer = floodFill(buffer, new Point(380, 227), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);

        buffer = floodFill(buffer, new Point(296, 292), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(332, 353), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(208, 497), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(404, 554), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);

        buffer = floodFill(buffer, new Point(202, 282), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(285, 361), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(347, 277), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(353, 255), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(248, 231), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(259, 241), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(292, 242), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(177, 293), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(375, 516), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);

        buffer = floodFill(buffer, new Point(176, 279), MyColor.PLACEHOLDER, Color.WHITE);
        buffer = floodFill(buffer, new Point(255, 281), MyColor.PLACEHOLDER, Color.WHITE);
        buffer = floodFill(buffer, new Point(254, 260), MyColor.PLACEHOLDER, Color.WHITE);
        buffer = floodFill(buffer, new Point(242, 330), MyColor.PLACEHOLDER, Color.WHITE);
        buffer = floodFill(buffer, new Point(180, 268), MyColor.PLACEHOLDER, Color.BLACK);
        buffer = floodFill(buffer, new Point(244, 267), MyColor.PLACEHOLDER, Color.BLACK);

        buffer = floodFill(buffer, new Point(284, 401), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);
        buffer = floodFill(buffer, new Point(372, 499), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);
        buffer = floodFill(buffer, new Point(209, 474), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);

        buffer = floodFill(buffer, new Point(303, 442), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD);

        buffer = floodFill(buffer, new Point(224, 460), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(216, 511), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(209, 554), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(348, 495), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(259, 596), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD_SHADOW);

        buffer = floodFill(buffer, new Point(251, 457), MyColor.PLACEHOLDER, MyColor.RED_COLOR);
        buffer = floodFill(buffer, new Point(237, 526), MyColor.PLACEHOLDER, MyColor.RED_COLOR);
        buffer = floodFill(buffer, new Point(215, 537), MyColor.PLACEHOLDER, MyColor.RED_SHADOW);
        buffer = floodFill(buffer, new Point(234, 506), MyColor.PLACEHOLDER, MyColor.RED_SHADOW);
        buffer = floodFill(buffer, new Point(234, 506), MyColor.PLACEHOLDER, MyColor.RED_SHADOW);
        buffer = floodFill(buffer, new Point(234, 506), MyColor.PLACEHOLDER, MyColor.RED_SHADOW);

        buffer = floodFill(buffer, new Point(218, 484), MyColor.PLACEHOLDER, MyColor.BLUE_SHADOW);
        buffer = floodFill(buffer, new Point(226, 485), MyColor.PLACEHOLDER, MyColor.BLUE_SHADOW);
        buffer = floodFill(buffer, new Point(250, 489), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);
        buffer = floodFill(buffer, new Point(261, 493), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);
        buffer = floodFill(buffer, new Point(278, 490), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawLuffyChildStandWithBack() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        // hair
        drawArc(g, new Point(230, 153), new Point(231, 157), new Point(230, 168), new Point(221, 182), 1,
                MyColor.BLACK);
        drawArc(g, new Point(221, 182), new Point(224, 182), new Point(230, 180), new Point(233, 176), 1,
                MyColor.BLACK);
        drawArc(g, new Point(233, 176), new Point(234, 180), new Point(234, 190), new Point(229, 201), 1,
                MyColor.BLACK);
        drawArc(g, new Point(229, 201), new Point(230, 201), new Point(235, 200), new Point(242, 193), 1,
                MyColor.BLACK);
        drawArc(g, new Point(242, 193), new Point(244, 199), new Point(245, 213), new Point(242, 228), 1,
                MyColor.BLACK);
        drawArc(g, new Point(242, 228), new Point(245, 227), new Point(252, 223), new Point(256, 218), 1,
                MyColor.BLACK);
        drawArc(g, new Point(256, 218), new Point(260, 219), new Point(271, 218), new Point(277, 214), 1,
                MyColor.BLACK);
        g.drawLine(277, 214, 277, 221);
        g.drawLine(277, 221, 295, 207);
        g.drawLine(295, 207, 296, 211);
        g.drawLine(296, 211, 301, 201);
        g.drawLine(301, 201, 309, 211);
        drawArc(g, new Point(309, 211), new Point(310, 207), new Point(313, 197), new Point(323, 188), 1,
                MyColor.BLACK);
        g.drawLine(323, 188, 335, 171);
        g.drawLine(335, 171, 230, 153);

        // ear
        g.drawLine(321, 182, 307, 201);
        drawArc(g, new Point(307, 201), new Point(300, 186), new Point(313, 183), new Point(321, 182), 1,
                MyColor.BLACK);

        // face
        drawArc(g, new Point(324, 186), new Point(320, 204), new Point(328, 233), new Point(290, 224), 1,
                MyColor.BLACK);
        g.drawLine(293, 216, 287, 231);
        g.drawLine(256, 218, 254, 229);

        // body
        drawArc(g, new Point(254, 229), new Point(257, 231), new Point(269, 235), new Point(287, 231), 1,
                MyColor.BLACK);
        drawArc(g, new Point(254, 229), new Point(240, 229), new Point(222, 232), new Point(225, 259), 1,
                MyColor.BLACK);
        g.drawLine(228, 260, 225, 343);
        drawArc(g, new Point(225, 343), new Point(237, 345), new Point(268, 348), new Point(295, 344), 1,
                MyColor.BLACK);
        g.drawLine(295, 344, 286, 330);
        g.drawLine(286, 330, 286, 282);
        drawArc(g, new Point(287, 231), new Point(292, 231), new Point(303, 234), new Point(307, 242), 1,
                MyColor.BLACK);
        g.drawLine(307, 242, 308, 276);
        g.drawLine(308, 276, 280, 276);
        drawArc(g, new Point(280, 276), new Point(278, 275), new Point(273, 270), new Point(271, 262), 1,
                MyColor.BLACK);
        drawArc(g, new Point(280, 276), new Point(279, 280), new Point(281, 282), new Point(282, 282), 1,
                MyColor.BLACK);
        g.drawLine(282, 282, 308, 282);
        drawArc(g, new Point(308, 282), new Point(310, 279), new Point(309, 277), new Point(308, 276), 1,
                MyColor.BLACK);
        g.drawLine(295, 345, 297, 348);
        drawArc(g, new Point(297, 348), new Point(296, 373), new Point(296, 383), new Point(299, 388), 1,
                MyColor.BLACK);
        drawArc(g, new Point(299, 388), new Point(303, 388), new Point(307, 388), new Point(310, 387), 1,
                MyColor.BLACK);
        drawArc(g, new Point(310, 387), new Point(315, 386), new Point(318, 383), new Point(319, 382), 1,
                MyColor.BLACK);
        drawArc(g, new Point(319, 382), new Point(326, 372), new Point(316, 361), new Point(310, 356), 1,
                MyColor.BLACK);
        drawArc(g, new Point(310, 356), new Point(307, 351), new Point(305, 337), new Point(304, 331), 1,
                MyColor.BLACK);
        g.drawLine(304, 331, 302, 282);
        drawArc(g, new Point(230, 333), new Point(239, 335), new Point(263, 339), new Point(286, 338), 1,
                MyColor.BLACK);
        g.drawLine(225, 259, 221, 356);
        drawArc(g, new Point(221, 356), new Point(222, 358), new Point(223, 360), new Point(225, 356), 1,
                MyColor.BLACK);
        g.drawLine(225, 259, 228, 260);
        g.drawLine(225, 343, 225, 418);
        g.drawLine(222, 418, 259, 418);
        g.drawLine(259, 418, 259, 431);
        g.drawLine(259, 431, 222, 431);
        g.drawLine(222, 431, 222, 418);
        g.drawLine(310, 387, 310, 418);
        g.drawLine(310, 418, 314, 418);
        g.drawLine(314, 418, 314, 431);
        g.drawLine(314, 431, 262, 431);
        g.drawLine(262, 431, 262, 418);
        g.drawLine(262, 418, 314, 418);
        g.drawLine(259, 420, 262, 420);
        g.drawLine(259, 430, 262, 430);
        g.drawLine(262, 418, 262, 411);
        g.drawLine(262, 411, 262, 404);
        g.drawLine(262, 404, 258, 389);
        drawArc(g, new Point(258, 389), new Point(254, 386), new Point(246, 379), new Point(243, 376), 1,
                MyColor.BLACK);
        g.drawLine(243, 376, 263, 377);
        g.drawLine(263, 377, 263, 388);
        drawArc(g, new Point(263, 388), new Point(263, 390), new Point(263, 396), new Point(266, 401), 1,
                MyColor.BLACK);
        g.drawLine(266, 401, 266, 418);
        g.drawLine(239, 432, 239, 495);
        drawArc(g, new Point(239, 495), new Point(237, 497), new Point(235, 502), new Point(235, 504), 1,
                MyColor.BLACK);
        g.drawLine(235, 504, 235, 511);
        g.drawLine(235, 511, 257, 511);
        drawArc(g, new Point(257, 511), new Point(258, 509), new Point(257, 502), new Point(251, 499), 1,
                MyColor.BLACK);
        drawArc(g, new Point(251, 499), new Point(251, 488), new Point(251, 460), new Point(254, 432), 1,
                MyColor.BLACK);
        g.drawLine(229, 511, 266, 511);
        g.drawLine(229, 519, 266, 519);
        drawArc(g, new Point(229, 511), new Point(228, 512), new Point(226, 514), new Point(229, 519), 1,
                MyColor.BLACK);
        drawArc(g, new Point(266, 511), new Point(269, 514), new Point(267, 517), new Point(266, 519), 1,
                MyColor.BLACK);
        drawArc(g, new Point(266, 511), new Point(264, 509), new Point(260, 505), new Point(256, 504), 1,
                MyColor.BLACK);
        g.drawLine(283, 431, 284, 508);
        g.drawLine(284, 508, 307, 508);
        drawArc(g, new Point(307, 508), new Point(308, 507), new Point(310, 505), new Point(307, 502), 1,
                MyColor.BLACK);
        drawArc(g, new Point(307, 502), new Point(303, 498), new Point(296, 496), new Point(295, 490), 1,
                MyColor.BLACK);
        g.drawLine(295, 490, 296, 431);
        g.drawLine(278, 508, 330, 508);
        g.drawLine(279, 518, 330, 518);
        drawArc(g, new Point(278, 508), new Point(278, 510), new Point(275, 514), new Point(279, 518), 1,
                MyColor.BLACK);
        drawArc(g, new Point(330, 518), new Point(332, 514), new Point(331, 510), new Point(330, 508), 1,
                MyColor.BLACK);
        drawArc(g, new Point(330, 508), new Point(326, 506), new Point(316, 501), new Point(307, 502), 1,
                MyColor.BLACK);
        drawArc(g, new Point(330, 508), new Point(375, 496), new Point(654, 487), new Point(330, 518), 1,
                MyColor.BLACK);
        drawArc(g, new Point(264, 509), new Point(266, 509), new Point(272, 508), new Point(278, 509), 1,
                MyColor.BLACK);
        drawArc(g, new Point(264, 519), new Point(267, 520), new Point(274, 520), new Point(279, 519), 1,
                MyColor.BLACK);
        drawArc(g, new Point(227, 512), new Point(216, 512), new Point(201, 520), new Point(228, 520), 1,
                MyColor.BLACK);
        g.setColor(MyColor.SKIN_LUFFY_CHILD_SHADOW);
        g.drawLine(239, 449, 253, 449);
        g.drawLine(283, 450, 296, 450);
        drawArc(g, new Point(241, 230), new Point(252, 242), new Point(285, 239), new Point(299, 235), 1,
                MyColor.BLACK);

        buffer = floodFill(buffer, new Point(265, 186), MyColor.PLACEHOLDER, MyColor.HAIR_LUFFY);

        buffer = floodFill(buffer, new Point(244, 440), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);
        buffer = floodFill(buffer, new Point(290, 440), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD_SHADOW);

        buffer = floodFill(buffer, new Point(314, 211), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(311, 190), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(295, 299), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(224, 307), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(245, 503), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(292, 502), MyColor.PLACEHOLDER, MyColor.SKIN_LUFFY_CHILD);

        buffer = floodFill(buffer, new Point(266, 237), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);
        buffer = floodFill(buffer, new Point(294, 280), MyColor.PLACEHOLDER, MyColor.BLUE_COLOR);

        buffer = floodFill(buffer, new Point(247, 282), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD);

        buffer = floodFill(buffer, new Point(259, 363), MyColor.PLACEHOLDER, MyColor.TROUSERS_LUFFY);

        buffer = floodFill(buffer, new Point(256, 383), MyColor.PLACEHOLDER, MyColor.TROUSERS_LUFFY_SHADOW);

        buffer = floodFill(buffer, new Point(244, 427), MyColor.PLACEHOLDER, MyColor.PANTSEDGE_LUFFY);
        buffer = floodFill(buffer, new Point(261, 427), MyColor.PLACEHOLDER, MyColor.PANTSEDGE_LUFFY);
        buffer = floodFill(buffer, new Point(272, 426), MyColor.PLACEHOLDER, MyColor.PANTSEDGE_LUFFY);

        buffer = floodFill(buffer, new Point(254, 516), MyColor.PLACEHOLDER, MyColor.SHOE);
        buffer = floodFill(buffer, new Point(292, 516), MyColor.PLACEHOLDER, MyColor.SHOE);

        buffer = floodFill(buffer, new Point(260, 508), MyColor.PLACEHOLDER, MyColor.SHOEEDGE);
        buffer = floodFill(buffer, new Point(313, 507), MyColor.PLACEHOLDER, MyColor.SHOEEDGE);

        buffer = floodFill(buffer, new Point(221, 518), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(271, 517), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD);
        buffer = floodFill(buffer, new Point(346, 513), MyColor.PLACEHOLDER, MyColor.SHIRT_LUFFY_CHILD);

        buffer = toTransparent(buffer);

        return buffer;
    }

    private BufferedImage drawLogoOnePiece() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(MyColor.PLACEHOLDER);
        g.fillRect(0, 0, 601, 601);

        g.setColor(Color.BLACK);
        g.drawLine(81, 252, 68, 239);
        drawArc(g, new Point(68, 239), new Point(64, 241), new Point(55, 243), new Point(54, 233), 1, Color.BLACK);
        drawArc(g, new Point(54, 233), new Point(54, 230), new Point(57, 224), new Point(63, 226), 1, Color.BLACK);
        drawArc(g, new Point(63, 226), new Point(63, 222), new Point(63, 215), new Point(73, 217), 1, Color.BLACK);
        drawArc(g, new Point(73, 217), new Point(78, 218), new Point(81, 224), new Point(76, 231), 1, Color.BLACK);
        g.drawLine(76, 231, 90, 245);
        g.drawLine(90, 245, 81, 252);
        g.drawLine(169, 245, 184, 231);
        drawArc(g, new Point(184, 231), new Point(182, 228), new Point(180, 222), new Point(185, 219), 1, Color.BLACK);
        drawArc(g, new Point(185, 219), new Point(188, 217), new Point(197, 217), new Point(197, 226), 1, Color.BLACK);
        drawArc(g, new Point(197, 226), new Point(201, 226), new Point(209, 228), new Point(204, 238), 1, Color.BLACK);
        drawArc(g, new Point(204, 238), new Point(203, 240), new Point(198, 244), new Point(192, 240), 1, Color.BLACK);
        g.drawLine(192, 240, 178, 254);
        g.drawLine(178, 254, 169, 245);
        g.drawLine(80, 334, 68, 346);
        drawArc(g, new Point(68, 346), new Point(65, 345), new Point(60, 343), new Point(56, 346), 1, Color.BLACK);
        drawArc(g, new Point(56, 346), new Point(53, 349), new Point(52, 359), new Point(62, 359), 1, Color.BLACK);
        drawArc(g, new Point(62, 359), new Point(63, 363), new Point(66, 371), new Point(74, 367), 1, Color.BLACK);
        drawArc(g, new Point(74, 367), new Point(76, 365), new Point(80, 361), new Point(76, 355), 1, Color.BLACK);
        g.drawLine(76, 355, 88, 343);
        g.drawLine(88, 343, 80, 334);
        g.drawLine(87, 296, 60, 292);
        drawArc(g, new Point(60, 292), new Point(65, 289), new Point(76, 285), new Point(90, 283), 1, Color.BLACK);
        g.drawLine(172, 296, 192, 293);
        drawArc(g, new Point(90, 283), new Point(110, 279), new Point(140, 278), new Point(169, 283), 1, Color.BLACK);
        drawArc(g, new Point(90, 283), new Point(90, 259), new Point(147, 229), new Point(169, 283), 1, Color.BLACK);
        drawArc(g, new Point(169, 283), new Point(179, 285), new Point(190, 288), new Point(200, 292), 1, Color.BLACK);
        g.drawLine(200, 292, 192, 293);
        drawArc(g, new Point(192, 293), new Point(193, 304), new Point(189, 330), new Point(162, 348), 1, Color.BLACK);
        drawArc(g, new Point(91, 275), new Point(105, 273), new Point(138, 269), new Point(166, 275), 1, Color.BLACK);
        drawArc(g, new Point(94, 288), new Point(110, 287), new Point(146, 285), new Point(165, 288), 1, Color.BLACK);
        drawArc(g, new Point(165, 288), new Point(168, 288), new Point(174, 291), new Point(171, 301), 1, Color.BLACK);
        drawArc(g, new Point(171, 301), new Point(167, 328), new Point(143, 336), new Point(131, 337), 1, Color.BLACK);
        drawArc(g, new Point(131, 337), new Point(101, 337), new Point(90, 314), new Point(88, 303), 1, Color.BLACK);
        drawArc(g, new Point(88, 303), new Point(84, 293), new Point(91, 289), new Point(94, 288), 1, Color.BLACK);
        g.drawLine(114, 334, 111, 338);
        g.drawLine(111, 338, 95, 351);
        g.drawLine(95, 351, 81, 363);
        g.drawLine(81, 363, 77, 363);
        drawArc(g, new Point(111, 338), new Point(115, 341), new Point(125, 344), new Point(138, 342), 1, Color.BLACK);
        g.drawLine(149, 339, 145, 333);
        drawArc(g, new Point(149, 339), new Point(159, 348), new Point(188, 367), new Point(212, 364), 1, Color.BLACK);
        drawArc(g, new Point(212, 364), new Point(212, 367), new Point(209, 370), new Point(208, 371), 1, Color.BLACK);
        drawArc(g, new Point(208, 371), new Point(200, 383), new Point(152, 354), new Point(138, 342), 1, Color.BLACK);
        drawArc(g, new Point(149, 339), new Point(145, 340), new Point(141, 341), new Point(138, 342), 1, Color.BLACK);
        g.drawLine(115, 336, 118, 337);
        g.drawLine(118, 337, 117, 340);
        g.drawLine(117, 340, 113, 338);
        g.drawLine(113, 338, 115, 336);
        g.drawLine(120, 337, 125, 338);
        g.drawLine(125, 338, 125, 341);
        g.drawLine(125, 341, 119, 340);
        g.drawLine(119, 340, 120, 337);
        g.drawLine(127, 338, 132, 338);
        g.drawLine(132, 338, 133, 341);
        g.drawLine(133, 341, 127, 341);
        g.drawLine(127, 341, 127, 338);
        g.drawLine(134, 338, 139, 337);
        g.drawLine(139, 337, 140, 340);
        g.drawLine(140, 340, 135, 341);
        g.drawLine(135, 341, 134, 338);
        g.drawLine(140, 337, 142, 340);
        g.drawLine(142, 340, 147, 339);
        g.drawLine(147, 339, 145, 335);
        g.drawLine(145, 335, 140, 337);
        g.drawLine(88, 359, 82, 364);
        g.drawLine(82, 364, 87, 370);
        g.drawLine(87, 370, 89, 368);
        drawArc(g, new Point(89, 368), new Point(89, 365), new Point(89, 359), new Point(88, 359), 1, Color.BLACK);
        drawArc(g, new Point(89, 357), new Point(90, 359), new Point(91, 363), new Point(91, 366), 1, Color.BLACK);
        g.drawLine(91, 366, 96, 361);
        g.drawLine(96, 361, 94, 354);
        g.drawLine(94, 354, 89, 357);
        g.drawLine(95, 352, 98, 360);
        g.drawLine(98, 360, 102, 356);
        drawArc(g, new Point(102, 356), new Point(102, 354), new Point(101, 351), new Point(100, 349), 1, Color.BLACK);
        g.drawLine(100, 349, 95, 352);
        g.drawLine(102, 347, 105, 345);
        drawArc(g, new Point(105, 345), new Point(106, 346), new Point(108, 348), new Point(108, 352), 1, Color.BLACK);
        g.drawLine(108, 352, 104, 355);
        g.drawLine(104, 355, 102, 347);
        g.drawLine(72, 368, 67, 372);
        g.drawLine(67, 372, 72, 373);
        g.drawLine(72, 373, 72, 379);
        g.drawLine(72, 379, 75, 377);
        g.drawLine(75, 377, 76, 380);
        g.drawLine(76, 380, 79, 379);
        g.drawLine(79, 379, 80, 381);
        g.drawLine(80, 381, 82, 380);
        g.drawLine(82, 380, 84, 382);
        g.drawLine(84, 382, 86, 380);
        g.drawLine(86, 380, 89, 381);
        g.drawLine(89, 381, 88, 373);
        drawArc(g, new Point(88, 373), new Point(94, 366), new Point(111, 350), new Point(125, 343), 1, Color.BLACK);
        g.drawLine(80, 365, 78, 367);
        drawArc(g, new Point(78, 367), new Point(78, 368), new Point(79, 372), new Point(79, 376), 1, Color.BLACK);
        g.drawLine(79, 376, 81, 377);
        g.drawLine(81, 377, 82, 377);
        g.drawLine(82, 377, 84, 378);
        g.drawLine(84, 378, 85, 377);
        g.drawLine(85, 377, 86, 377);
        drawArc(g, new Point(86, 377), new Point(86, 373), new Point(82, 367), new Point(80, 365), 1, Color.BLACK);
        drawArc(g, new Point(76, 368), new Point(77, 370), new Point(78, 374), new Point(77, 377), 1, Color.BLACK);
        g.drawLine(77, 377, 76, 373);
        g.drawLine(76, 373, 75, 374);
        g.drawLine(75, 374, 75, 371);
        g.drawLine(75, 371, 74, 370);
        g.drawLine(74, 370, 76, 368);
        g.drawLine(106, 344, 111, 340);
        drawArc(g, new Point(111, 340), new Point(112, 341), new Point(114, 344), new Point(114, 348), 1, Color.BLACK);
        g.drawLine(114, 348, 110, 351);
        drawArc(g, new Point(110, 351), new Point(109, 349), new Point(108, 345), new Point(106, 344), 1, Color.BLACK);
        g.drawLine(114, 341, 116, 347);
        g.drawLine(116, 347, 120, 344);
        g.drawLine(120, 344, 114, 341);
        drawArc(g, new Point(115, 349), new Point(118, 351), new Point(138, 353), new Point(146, 349), 1, Color.BLACK);
        drawArc(g, new Point(111, 352), new Point(114, 363), new Point(138, 371), new Point(150, 351), 1, Color.BLACK);
        g.drawLine(124, 344, 118, 348);
        g.drawLine(118, 348, 123, 349);
        g.drawLine(123, 349, 124, 344);
        g.drawLine(126, 344, 125, 349);
        g.drawLine(125, 349, 135, 349);
        g.drawLine(135, 349, 134, 344);
        g.drawLine(134, 344, 126, 344);
        g.drawLine(136, 343, 137, 349);
        g.drawLine(137, 349, 143, 348);
        g.drawLine(143, 348, 136, 343);
        g.drawLine(141, 343, 149, 341);
        g.drawLine(149, 341, 150, 343);
        g.drawLine(150, 343, 144, 345);
        g.drawLine(144, 345, 141, 343);
        g.drawLine(152, 344, 146, 346);
        g.drawLine(146, 346, 148, 348);
        g.drawLine(148, 348, 154, 345);
        g.drawLine(154, 345, 152, 344);
        g.drawLine(156, 346, 150, 349);
        g.drawLine(150, 349, 155, 352);
        g.drawLine(155, 352, 160, 349);
        g.drawLine(160, 349, 156, 346);
        g.drawLine(162, 350, 157, 353);
        g.drawLine(157, 353, 161, 356);
        g.drawLine(161, 356, 165, 353);
        g.drawLine(165, 353, 162, 350);
        g.drawLine(167, 353, 163, 357);
        g.drawLine(163, 357, 168, 360);
        g.drawLine(168, 360, 173, 357);
        g.drawLine(173, 357, 167, 353);
        g.drawLine(175, 358, 170, 361);
        g.drawLine(170, 361, 174, 363);
        g.drawLine(174, 363, 179, 360);
        g.drawLine(179, 360, 175, 358);
        g.drawLine(181, 360, 179, 362);
        g.drawLine(179, 362, 176, 364);
        g.drawLine(176, 364, 184, 368);
        g.drawLine(184, 368, 188, 363);
        g.drawLine(188, 363, 181, 360);
        g.drawLine(190, 363, 186, 369);
        g.drawLine(186, 369, 191, 371);
        drawArc(g, new Point(191, 371), new Point(192, 369), new Point(194, 367), new Point(195, 365), 1, Color.BLACK);
        g.drawLine(195, 365, 190, 363);
        drawArc(g, new Point(197, 365), new Point(196, 366), new Point(195, 369), new Point(193, 371), 1, Color.BLACK);
        g.drawLine(193, 371, 199, 373);
        g.drawLine(199, 373, 204, 365);
        g.drawLine(204, 365, 197, 365);
        g.drawLine(206, 365, 201, 373);
        drawArc(g, new Point(201, 373), new Point(203, 372), new Point(208, 371), new Point(210, 367), 1, Color.BLACK);
        g.drawLine(210, 367, 206, 365);
        drawArc(g, new Point(97, 348), new Point(88, 344), new Point(65, 323), new Point(67, 294), 1, Color.BLACK);
        drawArc(g, new Point(67, 289), new Point(68, 271), new Point(81, 234), new Point(129, 232), 1, Color.BLACK);
        drawArc(g, new Point(129, 232), new Point(149, 232), new Point(186, 244), new Point(191, 289), 1, Color.BLACK);
        g.drawLine(171, 342, 184, 355);
        g.drawLine(184, 355, 181, 358);
        g.drawLine(179, 333, 193, 345);
        drawArc(g, new Point(204, 350), new Point(205, 353), new Point(206, 360), new Point(197, 360), 1, Color.BLACK);
        g.drawLine(197, 360, 197, 363);
        g.drawLine(359, 243, 349, 245);
        g.drawLine(349, 245, 359, 247);
        g.drawLine(378, 243, 387, 245);
        g.drawLine(387, 245, 378, 248);
        drawArc(g, new Point(364, 255), new Point(365, 256), new Point(366, 257), new Point(363, 259), 1, Color.BLACK);
        drawArc(g, new Point(363, 259), new Point(341, 268), new Point(347, 290), new Point(355, 294), 1, Color.BLACK);
        g.drawLine(355, 294, 351, 300);
        g.drawLine(351, 300, 355, 300);
        g.drawLine(355, 300, 351, 306);
        g.drawLine(351, 306, 357, 306);
        g.drawLine(357, 306, 354, 320);
        g.drawLine(354, 320, 360, 320);
        g.drawLine(360, 320, 362, 343);
        drawArc(g, new Point(361, 273), new Point(355, 276), new Point(355, 286), new Point(361, 289), 1, Color.BLACK);
        g.drawLine(361, 289, 361, 273);
        g.drawLine(362, 342, 351, 346);
        drawArc(g, new Point(351, 346), new Point(348, 347), new Point(349, 352), new Point(352, 351), 1, Color.BLACK);
        g.drawLine(352, 351, 369, 345);
        g.drawLine(369, 345, 378, 351);
        drawArc(g, new Point(378, 351), new Point(381, 353), new Point(386, 351), new Point(384, 349), 1, Color.BLACK);
        g.drawLine(384, 349, 376, 342);
        drawArc(g, new Point(300, 236), new Point(300, 236), new Point(303, 238), new Point(306, 243), 1, Color.BLACK);
        g.drawLine(306, 243, 306, 342);
        drawArc(g, new Point(306, 342), new Point(305, 343), new Point(303, 347), new Point(300, 349), 1, Color.BLACK);
        g.drawLine(300, 349, 325, 349);
        drawArc(g, new Point(325, 349), new Point(322, 347), new Point(318, 343), new Point(319, 339), 1, Color.BLACK);
        g.drawLine(319, 339, 319, 318);
        drawArc(g, new Point(319, 318), new Point(326, 319), new Point(342, 318), new Point(345, 302), 1, Color.BLACK);
        g.drawLine(345, 302, 345, 251);
        drawArc(g, new Point(345, 251), new Point(343, 246), new Point(337, 236), new Point(326, 236), 1, Color.BLACK);
        g.drawLine(326, 236, 300, 236);
        g.drawLine(319, 248, 319, 307);
        drawArc(g, new Point(319, 307), new Point(323, 307), new Point(332, 306), new Point(333, 299), 1, Color.BLACK);
        g.drawLine(333, 299, 333, 255);
        drawArc(g, new Point(333, 255), new Point(331, 253), new Point(327, 248), new Point(319, 248), 1, Color.BLACK);
        g.drawLine(196, 241, 196, 290);
        drawArc(g, new Point(196, 293), new Point(196, 307), new Point(195, 339), new Point(193, 346), 1, Color.BLACK);
        drawArc(g, new Point(193, 346), new Point(193, 346), new Point(191, 348), new Point(188, 350), 1, Color.BLACK);
        g.drawLine(188, 350, 215, 350);
        drawArc(g, new Point(215, 350), new Point(213, 346), new Point(211, 334), new Point(214, 324), 1, Color.BLACK);
        g.drawLine(214, 324, 222, 350);
        g.drawLine(222, 350, 241, 350);
        g.drawLine(241, 350, 288, 350);
        g.drawLine(288, 350, 288, 327);
        drawArc(g, new Point(288, 327), new Point(284, 335), new Point(276, 336), new Point(273, 336), 1, Color.BLACK);
        drawArc(g, new Point(273, 336), new Point(265, 335), new Point(260, 327), new Point(259, 324), 1, Color.BLACK);
        g.drawLine(259, 324, 259, 295);
        drawArc(g, new Point(259, 295), new Point(268, 293), new Point(276, 301), new Point(279, 305), 1, Color.BLACK);
        g.drawLine(279, 305, 279, 278);
        drawArc(g, new Point(279, 278), new Point(273, 287), new Point(263, 288), new Point(259, 288), 1, Color.BLACK);
        g.drawLine(259, 288, 259, 262);
        g.drawLine(259, 262, 262, 255);
        drawArc(g, new Point(262, 255), new Point(270, 243), new Point(282, 250), new Point(288, 255), 1, Color.BLACK);
        g.drawLine(288, 255, 287, 237);
        g.drawLine(287, 237, 241, 237);
        drawArc(g, new Point(241, 350), new Point(240, 349), new Point(240, 349), new Point(239, 348), 1, Color.BLACK);
        drawArc(g, new Point(241, 237), new Point(235, 241), new Point(236, 306), new Point(237, 338), 1, Color.BLACK);
        drawArc(g, new Point(237, 338), new Point(237, 342), new Point(238, 345), new Point(239, 348), 1, Color.BLACK);
        g.drawLine(241, 237, 219, 237);
        drawArc(g, new Point(219, 237), new Point(220, 243), new Point(220, 256), new Point(218, 260), 1, Color.BLACK);
        drawArc(g, new Point(218, 260), new Point(216, 255), new Point(214, 243), new Point(214, 237), 1, Color.BLACK);
        g.drawLine(214, 237, 205, 237);
        drawArc(g, new Point(241, 237), new Point(242, 238), new Point(244, 241), new Point(245, 245), 1, Color.BLACK);
        g.drawLine(245, 245, 245, 340);
        drawArc(g, new Point(245, 340), new Point(245, 341), new Point(244, 344), new Point(239, 348), 1, Color.BLACK);
        g.drawLine(383, 236, 434, 235);
        g.drawLine(434, 235, 434, 256);
        drawArc(g, new Point(434, 256), new Point(419, 240), new Point(408, 253), new Point(405, 260), 1, Color.BLACK);
        g.drawLine(405, 260, 405, 288);
        drawArc(g, new Point(405, 288), new Point(413, 290), new Point(421, 284), new Point(424, 281), 1, Color.BLACK);
        g.drawLine(424, 281, 424, 304);
        drawArc(g, new Point(424, 304), new Point(419, 296), new Point(410, 294), new Point(405, 294), 1, Color.BLACK);
        g.drawLine(405, 294, 405, 321);
        drawArc(g, new Point(405, 321), new Point(405, 333), new Point(415, 336), new Point(420, 336), 1, Color.BLACK);
        drawArc(g, new Point(420, 336), new Point(427, 335), new Point(431, 332), new Point(433, 330), 1, Color.BLACK);
        g.drawLine(433, 330, 433, 349);
        g.drawLine(433, 349, 385, 349);
        drawArc(g, new Point(385, 349), new Point(390, 345), new Point(391, 342), new Point(391, 340), 1, Color.BLACK);
        g.drawLine(391, 340, 391, 249);
        drawArc(g, new Point(391, 249), new Point(391, 243), new Point(386, 237), new Point(383, 236), 1, Color.BLACK);
        g.drawLine(464, 280, 482, 280);
        g.drawLine(482, 280, 482, 257);
        drawArc(g, new Point(482, 257), new Point(482, 250), new Point(476, 237), new Point(459, 237), 1, Color.BLACK);
        drawArc(g, new Point(459, 237), new Point(453, 236), new Point(440, 241), new Point(436, 257), 1, Color.BLACK);
        g.drawLine(436, 257, 436, 330);
        drawArc(g, new Point(436, 330), new Point(437, 336), new Point(443, 349), new Point(458, 350), 1, Color.BLACK);
        drawArc(g, new Point(458, 350), new Point(465, 351), new Point(479, 347), new Point(482, 331), 1, Color.BLACK);
        g.drawLine(482, 331, 482, 304);
        g.drawLine(482, 304, 463, 304);
        drawArc(g, new Point(463, 304), new Point(464, 304), new Point(466, 307), new Point(466, 312), 1, Color.BLACK);
        drawArc(g, new Point(466, 312), new Point(467, 317), new Point(465, 330), new Point(459, 329), 1, Color.BLACK);
        drawArc(g, new Point(459, 329), new Point(455, 329), new Point(452, 327), new Point(453, 318), 1, Color.BLACK);
        g.drawLine(453, 318, 453, 264);
        drawArc(g, new Point(453, 264), new Point(454, 261), new Point(455, 255), new Point(459, 256), 1, Color.BLACK);
        drawArc(g, new Point(459, 256), new Point(461, 256), new Point(465, 257), new Point(466, 263), 1, Color.BLACK);
        drawArc(g, new Point(466, 263), new Point(466, 266), new Point(466, 274), new Point(464, 280), 1, Color.BLACK);
        drawArc(g, new Point(513, 341), new Point(490, 330), new Point(484, 305), new Point(483, 294), 1, Color.BLACK);
        drawArc(g, new Point(483, 294), new Point(481, 258), new Point(512, 242), new Point(529, 238), 1, Color.BLACK);
        g.drawLine(529, 238, 527, 233);
        g.drawLine(527, 233, 541, 237);
        g.drawLine(541, 237, 532, 247);
        g.drawLine(532, 247, 530, 242);
        drawArc(g, new Point(530, 242), new Point(517, 247), new Point(496, 271), new Point(497, 288), 1, Color.BLACK);
        g.drawLine(497, 288, 510, 288);
        g.drawLine(510, 288, 510, 280);
        g.drawLine(510, 280, 516, 280);
        g.drawLine(516, 280, 516, 288);
        g.drawLine(516, 288, 520, 288);
        drawArc(g, new Point(520, 288), new Point(521, 285), new Point(524, 280), new Point(529, 280), 1, Color.BLACK);
        drawArc(g, new Point(529, 280), new Point(531, 279), new Point(537, 281), new Point(538, 290), 1, Color.BLACK);
        drawArc(g, new Point(538, 290), new Point(539, 294), new Point(538, 302), new Point(530, 303), 1, Color.BLACK);
        drawArc(g, new Point(530, 303), new Point(532, 298), new Point(536, 289), new Point(530, 287), 1, Color.BLACK);
        drawArc(g, new Point(530, 287), new Point(527, 286), new Point(522, 288), new Point(520, 296), 1, Color.BLACK);
        drawArc(g, new Point(520, 296), new Point(518, 301), new Point(514, 314), new Point(522, 320), 1, Color.BLACK);
        drawArc(g, new Point(522, 320), new Point(519, 330), new Point(510, 351), new Point(503, 355), 1, Color.BLACK);
        drawArc(g, new Point(503, 355), new Point(503, 355), new Point(460, 355), new Point(433, 355), 1, Color.BLACK);
        drawArc(g, new Point(503, 355), new Point(499, 359), new Point(496, 362), new Point(494, 363), 1, Color.BLACK);
        g.drawLine(433, 355, 426, 349);
        g.drawLine(433, 355, 445, 363);
        g.drawLine(494, 363, 445, 363);
        drawArc(g, new Point(494, 363), new Point(490, 368), new Point(477, 370), new Point(472, 371), 1, Color.BLACK);
        drawArc(g, new Point(472, 371), new Point(456, 370), new Point(447, 365), new Point(445, 363), 1, Color.BLACK);
        g.drawLine(520, 295, 517, 295);
        g.drawLine(517, 295, 517, 303);
        g.drawLine(517, 303, 511, 303);
        g.drawLine(511, 303, 511, 295);
        g.drawLine(511, 295, 497, 295);
        drawArc(g, new Point(497, 295), new Point(499, 308), new Point(506, 328), new Point(517, 334), 1, Color.BLACK);
        g.drawLine(526, 342, 530, 344);
        g.drawLine(530, 344, 531, 341);
        g.drawLine(531, 341, 540, 349);
        g.drawLine(540, 349, 528, 352);
        g.drawLine(528, 352, 529, 349);
        g.drawLine(529, 349, 524, 347);
        drawArc(g, new Point(524, 347), new Point(516, 360), new Point(496, 385), new Point(472, 384), 1, Color.BLACK);
        drawArc(g, new Point(472, 384), new Point(464, 384), new Point(445, 381), new Point(426, 365), 1, Color.BLACK);
        drawArc(g, new Point(426, 365), new Point(421, 362), new Point(411, 355), new Point(405, 355), 1, Color.BLACK);
        g.drawLine(544, 363, 511, 363);
        drawArc(g, new Point(532, 302), new Point(535, 305), new Point(541, 312), new Point(541, 318), 1, Color.BLACK);
        g.drawLine(541, 318, 547, 324);
        g.drawLine(547, 324, 541, 324);
        g.drawLine(541, 324, 540, 328);
        g.drawLine(540, 328, 538, 326);
        g.drawLine(538, 326, 535, 329);
        g.drawLine(535, 329, 533, 325);
        drawArc(g, new Point(533, 325), new Point(533, 328), new Point(531, 335), new Point(527, 342), 1, Color.BLACK);
        g.drawLine(73, 217, 544, 217);
        drawArc(g, new Point(544, 217), new Point(546, 218), new Point(549, 221), new Point(544, 225), 1, Color.BLACK);
        g.drawLine(544, 225, 197, 225);
        g.drawLine(77, 356, 88, 356);
        g.drawLine(105, 356, 113, 356);
        g.drawLine(97, 363, 173, 364);
        g.drawLine(175, 356, 183, 356);
        g.drawLine(147, 356, 158, 356);
        g.drawLine(197, 363, 260, 363);
        g.drawLine(406, 355, 352, 355);
        g.drawLine(424, 363, 366, 363);
        drawArc(g, new Point(366, 363), new Point(362, 362), new Point(357, 360), new Point(352, 355), 1, Color.BLACK);
        drawArc(g, new Point(381, 363), new Point(380, 367), new Point(377, 374), new Point(369, 375), 1, Color.BLACK);
        drawArc(g, new Point(369, 375), new Point(366, 375), new Point(358, 373), new Point(349, 366), 1, Color.BLACK);
        g.drawLine(349, 366, 333, 356);
        g.drawLine(333, 356, 326, 356);
        drawArc(g, new Point(326, 356), new Point(326, 353), new Point(328, 348), new Point(332, 348), 1, Color.BLACK);
        drawArc(g, new Point(332, 348), new Point(334, 347), new Point(341, 346), new Point(352, 354), 1, Color.BLACK);
        g.drawLine(344, 363, 301, 363);
        drawArc(g, new Point(270, 355), new Point(271, 359), new Point(278, 362), new Point(282, 363), 1, Color.BLACK);
        g.drawLine(282, 363, 301, 363);
        drawArc(g, new Point(301, 363), new Point(299, 367), new Point(295, 375), new Point(287, 375), 1, Color.BLACK);
        drawArc(g, new Point(287, 375), new Point(282, 376), new Point(259, 363), new Point(248, 356), 1, Color.BLACK);
        g.drawLine(248, 356, 205, 356);
        g.drawLine(253, 351, 248, 355);
        g.drawLine(248, 355, 243, 355);
        drawArc(g, new Point(243, 355), new Point(245, 353), new Point(250, 349), new Point(253, 351), 1, Color.BLACK);
        drawArc(g, new Point(410, 349), new Point(409, 350), new Point(406, 353), new Point(406, 355), 1, Color.BLACK);
        g.drawLine(326, 356, 270, 356);
        g.drawLine(270, 356, 260, 350);
        drawArc(g, new Point(244, 350), new Point(243, 351), new Point(240, 354), new Point(240, 356), 1, Color.BLACK);
        g.drawLine(256, 351, 261, 353);
        g.drawLine(261, 353, 254, 358);
        g.drawLine(254, 358, 250, 356);
        g.drawLine(250, 356, 256, 351);
        g.drawLine(262, 353, 267, 357);
        g.drawLine(267, 357, 260, 361);
        g.drawLine(260, 361, 256, 359);
        g.drawLine(256, 359, 262, 353);
        g.drawLine(268, 358, 274, 361);
        g.drawLine(274, 361, 267, 365);
        g.drawLine(267, 365, 262, 362);
        g.drawLine(262, 362, 268, 358);
        g.drawLine(276, 362, 279, 363);
        g.drawLine(279, 363, 272, 368);
        g.drawLine(272, 368, 269, 366);
        g.drawLine(269, 366, 276, 362);
        g.drawLine(280, 364, 288, 365);
        g.drawLine(288, 365, 282, 373);
        g.drawLine(282, 373, 274, 369);
        g.drawLine(274, 369, 280, 364);
        g.drawLine(290, 365, 298, 365);
        drawArc(g, new Point(298, 365), new Point(296, 369), new Point(291, 375), new Point(284, 373), 1, Color.BLACK);
        g.drawLine(284, 373, 290, 365);
        g.drawLine(336, 349, 333, 354);
        g.drawLine(333, 354, 328, 354);
        drawArc(g, new Point(328, 354), new Point(329, 352), new Point(332, 349), new Point(336, 349), 1, Color.BLACK);
        g.drawLine(338, 350, 335, 355);
        g.drawLine(335, 355, 339, 357);
        g.drawLine(339, 357, 342, 352);
        g.drawLine(342, 352, 338, 350);
        g.drawLine(344, 353, 340, 359);
        g.drawLine(340, 359, 344, 361);
        g.drawLine(344, 361, 348, 355);
        g.drawLine(348, 355, 344, 353);
        g.drawLine(349, 356, 345, 362);
        g.drawLine(345, 362, 350, 365);
        g.drawLine(350, 365, 353, 359);
        g.drawLine(353, 359, 349, 356);
        g.drawLine(355, 360, 351, 365);
        g.drawLine(351, 365, 354, 367);
        g.drawLine(354, 367, 358, 362);
        g.drawLine(358, 362, 355, 360);
        g.drawLine(360, 363, 356, 368);
        g.drawLine(356, 368, 360, 371);
        g.drawLine(360, 371, 365, 364);
        g.drawLine(365, 364, 360, 363);
        drawArc(g, new Point(367, 365), new Point(365, 367), new Point(363, 372), new Point(362, 372), 1, Color.BLACK);
        g.drawLine(362, 372, 367, 374);
        g.drawLine(367, 374, 372, 365);
        g.drawLine(372, 365, 367, 365);
        g.drawLine(374, 365, 369, 374);
        drawArc(g, new Point(369, 374), new Point(371, 373), new Point(377, 371), new Point(379, 365), 1, Color.BLACK);
        g.drawLine(379, 365, 374, 365);
        g.drawLine(541, 320, 534, 322);
        g.drawLine(534, 322, 536, 324);
        g.drawLine(536, 324, 537, 323);
        g.drawLine(537, 323, 539, 324);
        g.drawLine(539, 324, 540, 322);
        g.drawLine(540, 322, 542, 322);
        g.drawLine(542, 322, 541, 320);
        g.drawLine(533, 318, 534, 321);
        g.drawLine(534, 321, 540, 319);
        g.drawLine(540, 319, 539, 316);
        drawArc(g, new Point(539, 316), new Point(537, 317), new Point(533, 319), new Point(533, 318), 1, Color.BLACK);
        g.drawLine(531, 313, 532, 317);
        g.drawLine(532, 317, 539, 314);
        g.drawLine(539, 314, 536, 310);
        g.drawLine(536, 310, 531, 313);
        drawArc(g, new Point(528, 310), new Point(528, 310), new Point(528, 312), new Point(530, 312), 1, Color.BLACK);
        g.drawLine(530, 312, 535, 309);
        g.drawLine(535, 309, 533, 306);
        g.drawLine(533, 306, 528, 310);
        g.drawLine(532, 305, 529, 308);
        g.drawLine(529, 308, 529, 305);
        g.drawLine(529, 305, 532, 305);
        drawArc(g, new Point(531, 292), new Point(530, 290), new Point(528, 287), new Point(523, 294), 1, Color.BLACK);
        g.drawLine(523, 294, 531, 292);
        g.drawLine(531, 294, 522, 297);
        g.drawLine(522, 297, 521, 302);
        g.drawLine(521, 302, 528, 300);
        g.drawLine(528, 300, 531, 294);
        g.drawLine(527, 302, 520, 304);
        g.drawLine(520, 304, 520, 309);
        g.drawLine(520, 309, 525, 307);
        drawArc(g, new Point(525, 307), new Point(525, 305), new Point(526, 303), new Point(527, 302), 1, Color.BLACK);
        drawArc(g, new Point(526, 308), new Point(525, 309), new Point(525, 312), new Point(527, 312), 1, Color.BLACK);
        g.drawLine(527, 312, 524, 318);
        drawArc(g, new Point(524, 318), new Point(521, 318), new Point(520, 313), new Point(520, 311), 1, Color.BLACK);
        g.drawLine(520, 311, 526, 308);
        g.drawLine(528, 314, 531, 319);
        drawArc(g, new Point(531, 319), new Point(529, 320), new Point(526, 320), new Point(525, 319), 1, Color.BLACK);
        g.drawLine(525, 319, 528, 314);
        g.drawLine(524, 322, 531, 322);
        g.drawLine(531, 322, 529, 331);
        g.drawLine(529, 331, 523, 325);
        g.drawLine(523, 325, 524, 322);
        g.drawLine(522, 327, 528, 333);
        g.drawLine(528, 333, 525, 339);
        g.drawLine(525, 339, 520, 333);
        g.drawLine(520, 333, 522, 327);
        g.drawLine(519, 334, 525, 341);
        g.drawLine(525, 341, 520, 348);
        g.drawLine(520, 348, 516, 342);
        g.drawLine(516, 342, 519, 334);
        g.drawLine(515, 343, 519, 350);
        g.drawLine(519, 350, 513, 358);
        g.drawLine(513, 358, 511, 350);
        g.drawLine(511, 350, 515, 343);
        g.drawLine(509, 352, 512, 360);
        g.drawLine(512, 360, 506, 365);
        drawArc(g, new Point(506, 365), new Point(506, 363), new Point(506, 358), new Point(505, 357), 1, Color.BLACK);
        g.drawLine(505, 357, 509, 352);
        drawArc(g, new Point(504, 358), new Point(504, 360), new Point(505, 365), new Point(504, 368), 1, Color.BLACK);
        g.drawLine(504, 368, 497, 372);
        g.drawLine(497, 372, 497, 364);
        g.drawLine(497, 364, 504, 358);
        g.drawLine(496, 365, 496, 374);
        g.drawLine(496, 374, 486, 379);
        g.drawLine(486, 379, 487, 370);
        g.drawLine(487, 370, 496, 365);
        g.drawLine(485, 371, 484, 379);
        g.drawLine(484, 379, 476, 380);
        g.drawLine(476, 380, 478, 372);
        g.drawLine(478, 372, 485, 371);
        g.drawLine(477, 373, 474, 381);
        g.drawLine(474, 381, 464, 381);
        drawArc(g, new Point(464, 381), new Point(465, 379), new Point(468, 375), new Point(468, 373), 1, Color.BLACK);
        g.drawLine(468, 373, 477, 373);
        drawArc(g, new Point(466, 373), new Point(465, 374), new Point(464, 378), new Point(461, 380), 1, Color.BLACK);
        g.drawLine(461, 380, 453, 377);
        g.drawLine(453, 377, 459, 372);
        g.drawLine(459, 372, 466, 373);
        g.drawLine(457, 371, 451, 376);
        g.drawLine(451, 376, 443, 373);
        g.drawLine(443, 373, 449, 368);
        g.drawLine(449, 368, 457, 371);
        g.drawLine(447, 367, 441, 371);
        g.drawLine(441, 371, 436, 368);
        g.drawLine(436, 368, 443, 364);
        g.drawLine(443, 364, 447, 367);
        g.drawLine(441, 363, 434, 367);
        g.drawLine(434, 367, 430, 364);
        g.drawLine(430, 364, 436, 359);
        g.drawLine(436, 359, 441, 363);
        g.drawLine(434, 358, 428, 363);
        g.drawLine(428, 363, 424, 360);
        g.drawLine(424, 360, 429, 355);
        g.drawLine(429, 355, 434, 358);
        g.drawLine(428, 353, 423, 359);
        g.drawLine(423, 359, 418, 356);
        g.drawLine(418, 356, 421, 350);
        g.drawLine(421, 350, 428, 353);
        g.drawLine(419, 350, 416, 355);
        g.drawLine(416, 355, 408, 354);
        drawArc(g, new Point(408, 354), new Point(410, 352), new Point(415, 349), new Point(419, 350), 1, Color.BLACK);
        g.drawLine(182, 225, 79, 225);
        drawArc(g, new Point(373, 256), new Point(373, 256), new Point(372, 258), new Point(374, 259), 1, Color.BLACK);
        drawArc(g, new Point(374, 259), new Point(380, 261), new Point(391, 268), new Point(389, 280), 1, Color.BLACK);
        drawArc(g, new Point(389, 280), new Point(389, 283), new Point(388, 290), new Point(383, 294), 1, Color.BLACK);
        g.drawLine(383, 294, 386, 300);
        g.drawLine(386, 300, 383, 300);
        g.drawLine(383, 300, 386, 306);
        g.drawLine(386, 306, 380, 306);
        g.drawLine(380, 306, 383, 320);
        g.drawLine(383, 320, 378, 320);
        g.drawLine(378, 320, 376, 342);
        drawArc(g, new Point(377, 274), new Point(380, 275), new Point(385, 281), new Point(377, 289), 1, Color.BLACK);
        g.drawLine(377, 289, 377, 274);
        g.drawLine(518, 355, 543, 355);
        drawArc(g, new Point(543, 355), new Point(546, 357), new Point(550, 360), new Point(543, 363), 1, Color.BLACK);
        midpointEllipse(g, 111, 308, 14, 14);
        midpointEllipse(g, 148, 308, 14, 14);
        midpointEllipse(g, 129, 324, 6, 6);
        midpointEllipse(g, 369, 247, 10, 10);

        buffer = floodFill(buffer, new Point(250, 223), MyColor.PLACEHOLDER, MyColor.LINE_ON_LOGO);
        buffer = floodFill(buffer, new Point(117, 222), MyColor.PLACEHOLDER, MyColor.LINE_ON_LOGO);

        buffer = floodFill(buffer, new Point(228, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);
        buffer = floodFill(buffer, new Point(81, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);
        buffer = floodFill(buffer, new Point(107, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);
        buffer = floodFill(buffer, new Point(154, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);
        buffer = floodFill(buffer, new Point(304, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);
        buffer = floodFill(buffer, new Point(397, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);
        buffer = floodFill(buffer, new Point(462, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);
        buffer = floodFill(buffer, new Point(532, 361), MyColor.PLACEHOLDER, MyColor.UNDERSCORE_LOGO);

        buffer = floodFill(buffer, new Point(216, 293), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(251, 293), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(311, 293), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(397, 293), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(444, 293), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(490, 293), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(532,349), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(163, 255), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(179, 312), MyColor.PLACEHOLDER, MyColor.LETTER);
        buffer = floodFill(buffer, new Point(85,320), MyColor.PLACEHOLDER, MyColor.LETTER);
        
        buffer = floodFill(buffer, new Point(191, 232), MyColor.PLACEHOLDER, MyColor.WHITE);
        buffer = floodFill(buffer, new Point(127, 357), MyColor.PLACEHOLDER, MyColor.WHITE);
        buffer = floodFill(buffer, new Point(69, 354), MyColor.PLACEHOLDER, MyColor.WHITE);
        buffer = floodFill(buffer, new Point(181, 345), MyColor.PLACEHOLDER, MyColor.WHITE);
        buffer = floodFill(buffer, new Point(129,297), MyColor.PLACEHOLDER, MyColor.WHITE);
        buffer = floodFill(buffer, new Point(70,234), MyColor.PLACEHOLDER, MyColor.WHITE);

        buffer = floodFill(buffer, new Point(110,308), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(151,307), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(129,325), MyColor.PLACEHOLDER, MyColor.BLACK);

        buffer = floodFill(buffer, new Point(129,259), MyColor.PLACEHOLDER, MyColor.BANANA);
        buffer = floodFill(buffer, new Point(79,292), MyColor.PLACEHOLDER, MyColor.BANANA);

        buffer = floodFill(buffer, new Point(131,278), MyColor.PLACEHOLDER, MyColor.SUN);

        buffer = floodFill(buffer, new Point(370,250), MyColor.PLACEHOLDER, MyColor.CRIMSON_RED);
        buffer = floodFill(buffer, new Point(369,309), MyColor.PLACEHOLDER, MyColor.CRIMSON_RED);
        buffer = floodFill(buffer, new Point(382,245), MyColor.PLACEHOLDER, MyColor.CRIMSON_RED);
        buffer = floodFill(buffer, new Point(357,245), MyColor.PLACEHOLDER, MyColor.CRIMSON_RED);

        buffer = floodFill(buffer, new Point(451,377), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(73,374), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(347,353), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(526,307), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(518,308), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(527,287), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(519,351), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(519,348), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(543,323), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(536,309), MyColor.PLACEHOLDER, MyColor.BLACK);

        // Moddaeng
        buffer = floodFill(buffer, new Point(89, 365), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(96, 358), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(102, 353), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(108, 350), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(114, 345), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(126, 337), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(130, 350), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(133, 337), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(125, 344), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(124, 347), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(140, 336), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(134, 340), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(142, 349), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(138, 343), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(141, 342), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(144, 334), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(147, 337), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(114, 335), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(119, 336), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(120, 342), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(118, 339), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(119, 349), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(113, 340), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(116, 341), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(115, 342), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(117, 348), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(120, 346), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(123, 341), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(122, 341), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(140, 338), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(146, 336), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(141, 346), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(139, 344), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(140, 345), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(143, 347), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(144, 348), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(151, 342), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(150, 344), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(147, 345), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(145, 346), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(146, 347), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(149, 346), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(152, 345), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(154, 344), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(152, 347), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(156, 353), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(159, 351), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(161, 349), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(163, 356), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(166, 352), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(169, 360), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(171, 359), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(173, 358), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(194, 363), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(189, 362), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(210, 366), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(204, 367), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(188, 364), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(186, 368), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(192, 370), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(175, 363), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(178, 362), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(179, 361), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(180, 359), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(204, 372), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(206, 371), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(201, 371), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(202, 370), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(203, 368), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(205, 365), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(194, 367), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(186, 361), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(183, 360), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(177, 358), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(180, 357), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(167, 360), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(165, 359), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(171, 355), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(170, 354), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(168, 353), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(165, 354), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(160, 356), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(157, 354), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(163, 350), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(164, 351), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(152, 351), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(154, 352), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(157, 346), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(160, 348), MyColor.PLACEHOLDER, MyColor.BLACK);
        buffer = floodFill(buffer, new Point(156, 348), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(161, 353), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(167, 356), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(175, 360), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(182, 363), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(190, 366), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(198, 367), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(207, 367), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(148, 342), MyColor.PLACEHOLDER, MyColor.OAKUM);
        buffer = floodFill(buffer, new Point(150, 341), MyColor.PLACEHOLDER, MyColor.OAKUM);

        g.setColor(Color.RED);
        plot(g, currentPoint.x, currentPoint.y, 1);

        buffer = toTransparent(buffer);

        return buffer;

    }

    private BufferedImage toTransparent(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelColor = image.getRGB(x, y);
                int alpha = (pixelColor >> 24) & 0xff;
                int red = (pixelColor >> 16) & 0xff;
                int green = (pixelColor >> 8) & 0xff;
                int blue = pixelColor & 0xff;

                // If the pixel is white, set its alpha to 0 (transparent)
                if (red == 245 && green == 245 && blue == 245) {
                    alpha = 0;
                }

                int newPixelColor = (alpha << 24) | (red << 16) | (green << 8) | blue;
                newImage.setRGB(x, y, newPixelColor);
            }
        }

        return newImage;
    }

}