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
        mainBuffer = drawRogerFaceFront();

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

    private void drawStrawHatStyle2 (Graphics g){
        drawArc(g, new Point(187,361), new Point(165,179), new Point(496,175), new Point(452,389), 1, MyColor.BLACK);
        drawArc(g, new Point(180,358), new Point(235,377), new Point(368,412), new Point(452,389), 1, MyColor.BLACK);
        drawArc(g, new Point(180,358), new Point(177,359), new Point(173,364), new Point(177,372), 1, MyColor.BLACK);
        drawArc(g, new Point(177,372), new Point(175,374), new Point(171,378), new Point(171,385), 1, MyColor.BLACK);
        drawArc(g, new Point(171,385), new Point(168,387), new Point(162,391), new Point(165,398), 1, MyColor.BLACK);
        drawArc(g, new Point(165,398), new Point(219,426), new Point(353,474), new Point(455,442), 1, MyColor.BLACK);
        drawArc(g, new Point(455,442), new Point(452,437), new Point(449,428), new Point(455,425), 1, MyColor.BLACK);
        drawArc(g, new Point(455,425), new Point(453,421), new Point(449,412), new Point(455,406), 1, MyColor.BLACK);
        drawArc(g, new Point(455,406), new Point(456,401), new Point(457,392), new Point(452,389), 1, MyColor.BLACK);
        g.drawLine(166,390, 43,398);
        drawArc(g, new Point(43,398), new Point(38,395), new Point(25,389), new Point(17,388), 1, MyColor.BLACK);
        drawArc(g, new Point(17,388), new Point(14,390), new Point(8,400), new Point(13,409), 1, MyColor.BLACK);
        drawArc(g, new Point(13,409), new Point(17,411), new Point(27,416), new Point(31,425), 1, MyColor.BLACK);
        drawArc(g, new Point(31,425), new Point(73,449), new Point(342,524), new Point(492,494), 1, MyColor.BLACK);
        g.drawLine(492,494, 577,494);
        drawArc(g, new Point(577,494), new Point(581,492), new Point(590,487), new Point(592,481), 1, MyColor.BLACK);
        drawArc(g, new Point(592,481), new Point(588,478), new Point(581,469), new Point(578,461), 1, MyColor.BLACK);
        g.drawLine(578,461, 542,458);
        g.drawLine(542,458, 456,425);
        drawArc(g, new Point(231,271), new Point(227,276), new Point(219,288), new Point(219,299), 1, MyColor.BLACK);
        drawArc(g, new Point(238,286), new Point(236,290), new Point(231,301), new Point(232,310), 1, MyColor.BLACK);
        drawArc(g, new Point(200,333), new Point(197,339), new Point(191,353), new Point(194,361), 1, MyColor.BLACK);
        drawArc(g, new Point(215,316), new Point(211,327), new Point(206,352), new Point(210,362), 1, MyColor.BLACK);
        drawArc(g, new Point(229,329), new Point(226,336), new Point(220,353), new Point(224,367), 1, MyColor.BLACK);
        drawArc(g, new Point(253,358), new Point(251,360), new Point(247,367), new Point(248,373), 1, MyColor.BLACK);
        drawArc(g, new Point(435,309), new Point(436,311), new Point(439,319), new Point(437,328), 1, MyColor.BLACK);
        g.drawLine(450,322, 450,331);
        drawArc(g, new Point(426,360), new Point(427,366), new Point(426,378), new Point(425,386), 1, MyColor.BLACK);
        drawArc(g, new Point(437,341), new Point(437,353), new Point(436,380), new Point(433,386), 1, MyColor.BLACK);
        g.drawLine(447,370, 447,390);
        drawArc(g, new Point(171,385), new Point(198,396), new Point(257,422), new Point(274,437), 1, MyColor.BLACK);
        drawArc(g, new Point(264,408), new Point(311,417), new Point(412,424), new Point(439,412), 1, MyColor.BLACK);
        drawArc(g, new Point(264,408), new Point(288,418), new Point(317,443), new Point(439,422), 1, MyColor.BLACK);
        drawArc(g, new Point(439,422), new Point(440,420), new Point(441,415), new Point(439,412), 1, MyColor.BLACK);
        g.drawLine(283,446, 324,453);
        drawArc(g, new Point(231,448), new Point(247,448), new Point(286,454), new Point(304,459), 1, MyColor.BLACK);
        drawArc(g, new Point(224,454), new Point(249,457), new Point(305,464), new Point(326,473), 1, MyColor.BLACK);
        drawArc(g, new Point(188,448), new Point(194,451), new Point(211,459), new Point(228,461), 1, MyColor.BLACK);
        drawArc(g, new Point(228,461), new Point(242,460), new Point(273,461), new Point(288,471), 1, MyColor.BLACK);
        drawArc(g, new Point(443,451), new Point(449,451), new Point(464,451), new Point(468,446), 1, MyColor.BLACK);
        drawArc(g, new Point(455,456), new Point(461,457), new Point(476,456), new Point(481,452), 1, MyColor.BLACK);
        drawArc(g, new Point(472,464), new Point(479,464), new Point(494,463), new Point(502,459), 1, MyColor.BLACK);
        drawArc(g, new Point(472,464), new Point(479,464), new Point(494,463), new Point(502,459), 1, MyColor.BLACK);
        drawArc(g, new Point(472,471), new Point(485,470), new Point(514,467), new Point(526,458), 1, MyColor.BLACK);
    }

    private BufferedImage drawRogerFaceFront() {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 601, 601);

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
        drawArc(g, new Point(76, 311), new Point(76, 299), new Point(75, 293), new Point(82, 284), 1, MyColor.BLACK);
        drawArc(g, new Point(82, 284), new Point(84, 281), new Point(86, 280), new Point(88, 284), 1, MyColor.BLACK);
        drawArc(g, new Point(88, 284), new Point(90, 288), new Point(93, 292), new Point(95, 296), 1, MyColor.BLACK);
        drawArc(g, new Point(95, 296), new Point(93, 311), new Point(91, 311), new Point(80, 309), 1, MyColor.BLACK);
        drawArc(g, new Point(95, 296), new Point(102, 307), new Point(109, 317), new Point(108, 328), 1, MyColor.BLACK);
        drawArc(g, new Point(108, 328), new Point(106, 355), new Point(110, 353), new Point(130, 370), 1, MyColor.BLACK);
        drawArc(g, new Point(107, 321), new Point(105, 316), new Point(103, 314), new Point(95, 314), 1, MyColor.BLACK);
        drawArc(g, new Point(95, 314), new Point(87, 314), new Point(87, 318), new Point(85, 330), 1, MyColor.BLACK);
        drawArc(g, new Point(85, 330), new Point(82, 349), new Point(85, 345), new Point(126, 382), 1, MyColor.BLACK);


        // Right Ear
        drawArc(g, new Point(461, 323), new Point(470, 300), new Point(493, 257), new Point(508, 271), 1, MyColor.BLACK);
        drawArc(g, new Point(508, 271), new Point(517, 285), new Point(514, 314), new Point(511, 328), 1, MyColor.BLACK);
        drawArc(g, new Point(511, 328), new Point(508, 343), new Point(487, 383), new Point(446, 407), 1, MyColor.BLACK);
        drawArc(g, new Point(446, 407), new Point(441, 410), new Point(438, 412), new Point(437, 407), 1, MyColor.BLACK);
        g.drawLine(509, 297, 504, 285);
        drawArc(g, new Point(504, 285), new Point(503, 283), new Point(502, 281), new Point(498, 284), 1, MyColor.BLACK);
        drawArc(g, new Point(498, 284), new Point(494, 287), new Point(489, 291), new Point(483, 297), 1, MyColor.BLACK);
        drawArc(g, new Point(483, 297), new Point(475, 305), new Point(468, 317), new Point(468, 329), 1, MyColor.BLACK);
        drawArc(g, new Point(468, 329), new Point(468, 341), new Point(475, 354), new Point(467, 362), 1, MyColor.BLACK);
        g.drawLine(467, 362, 450, 379);
        drawArc(g, new Point(506, 290), new Point(507, 296), new Point(509, 307), new Point(502, 311), 1, MyColor.BLACK);
        drawArc(g, new Point(483, 297), new Point(484, 305), new Point(490, 312), new Point(495, 312), 1, MyColor.BLACK);
        drawArc(g, new Point(468, 330), new Point(495, 304), new Point(495, 321), new Point(496, 334), 1, MyColor.BLACK);
        drawArc(g, new Point(496, 336), new Point(495, 350), new Point(479, 371), new Point(468, 378), 1, MyColor.BLACK);
        g.drawLine(452, 388, 441, 397);

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

        // Nose
        g.drawLine(298, 286, 298, 318);
        g.drawLine(281, 266, 281, 318);
        drawArc(g, new Point(281, 318), new Point(281, 323), new Point(281, 329), new Point(289, 328), 1, MyColor.BLACK);
        g.drawLine(282, 315, 289, 310);
        g.drawLine(282, 319, 290, 313);
        g.drawLine(285, 320, 293, 314);
        g.drawLine(292, 319, 295, 317);
        drawArc(g, new Point(272, 339), new Point(273, 338), new Point(277, 337), new Point(279, 338), 1, MyColor.BLACK);
        drawArc(g, new Point(300, 340), new Point(302, 338), new Point(306, 337), new Point(308, 337), 1, MyColor.BLACK);

        // Mouth
        drawArc(g, new Point(145, 337), new Point(149, 344), new Point(196, 366), new Point(208, 365), 1, MyColor.BLACK);
        g.drawLine(208, 365, 365, 365);
        drawArc(g, new Point(365, 365), new Point(374, 366), new Point(424, 346), new Point(435, 335), 1, MyColor.BLACK);
        drawArc(g, new Point(430, 339), new Point(411, 404), new Point(374, 467), new Point(291, 463), 1, MyColor.BLACK);
        drawArc(g, new Point(291, 463), new Point(189, 461), new Point(158, 409), new Point(149, 341), 1, MyColor.BLACK);
        drawArc(g, new Point(168, 397), new Point(207, 409), new Point(369, 413), new Point(404, 397), 1, MyColor.BLACK);
        g.drawLine(290, 365, 290, 403);
        drawArc(g, new Point(245, 370), new Point(245, 381), new Point(247, 396), new Point(248, 403), 1, MyColor.BLACK);
        drawArc(g, new Point(203, 368), new Point(204, 378), new Point(206, 397), new Point(207, 401), 1, MyColor.BLACK);
        drawArc(g, new Point(165, 358), new Point(167, 370), new Point(171, 391), new Point(174, 394), 1, MyColor.BLACK);
        drawArc(g, new Point(336, 368), new Point(336, 377), new Point(334, 395), new Point(332, 404), 1, MyColor.BLACK);
        drawArc(g, new Point(376, 366), new Point(374, 377), new Point(368, 398), new Point(366, 402), 1, MyColor.BLACK);
        g.drawLine(412, 356, 398, 395);
        g.drawLine(290, 412, 290, 459);
        drawArc(g, new Point(248, 412), new Point(245, 425), new Point(243, 449), new Point(243, 457), 1, MyColor.BLACK);
        drawArc(g, new Point(209, 407), new Point(207, 407), new Point(205, 428), new Point(205, 438), 1, MyColor.BLACK);
        g.drawLine(175, 402, 174, 413);
        g.drawLine(334, 412, 335, 453);
        g.drawLine(368, 408, 368, 441);
        g.drawLine(397, 402, 398, 408);
        drawArc(g, new Point(145, 324), new Point(143, 325), new Point(136, 328), new Point(136, 332), 1, MyColor.BLACK);
        drawArc(g, new Point(431, 323), new Point(440, 326), new Point(445, 327), new Point(445, 332), 1, MyColor.BLACK);
        
        buffer = floodFill(buffer, new Point(307, 226), Color.WHITE, MyColor.OIL);
        buffer = floodFill(buffer, new Point(246, 328), Color.WHITE, MyColor.TACAO);
        // buffer = floodFill(buffer, new Point(487, 249), Color.WHITE, MyColor.EERIE_BLACK);
        // buffer = floodFill(buffer, new Point(96, 253), Color.WHITE, MyColor.EERIE_BLACK);

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
                if (red == 255 && green == 255 && blue == 255) {
                    alpha = 0;
                }

                int newPixelColor = (alpha << 24) | (red << 16) | (green << 8) | blue;
                newImage.setRGB(x, y, newPixelColor);
            }
        }

        return newImage;
    }
    
}