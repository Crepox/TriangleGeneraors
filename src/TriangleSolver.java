import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Joe
 */
public class TriangleSolver {

    /**
     * @param args the command line arguments
     */
    boolean force = false;
    boolean stopped = true;
    boolean reset = false;
    boolean lines = true;
    float accuracy = 5f;
    JPanel menu2 = new JPanel();
    JPanel menu3 = new JPanel();
    JLabel l = new JLabel("", JLabel.CENTER);

    JButton add = new JButton("+");
    JButton minus = new JButton("-");
    JButton line = new JButton("Lines");

    JPanel p = new JPanel();

    Thread t = new Thread() {
        boolean done = true;

        public void run() {
            solveDistances();
        }

        public void solveDistances() {
            while (true) {
                if (!stopped) {
                    for (Square s : squares) {
                        float ab = (float) Math.sqrt(squareDist(s.a, s.b));
                        float sb = (float) Math.sqrt(squareDist(s, s.b));
                        float sa = (float) Math.sqrt(squareDist(s, s.a));
                        if ((ab - sb) * (ab - sb) < speed * speed
                                & (ab - sa) * (ab - sa) < speed * speed) {
                        } else {
                            calculateSquare(s);
                            done = false;
                        }
                    }
                    float totalMovement = 0;
                    for (Square s : squares) {
                        if (s.x + s.xm - 10 < 0 || s.x + s.xm + 10 > p.getWidth()) {

                        } else {
                            s.x += s.xm;
                            totalMovement += s.xm * s.xm;
                        }

                        if (s.y + s.ym - 10 < 0 || s.y + s.ym + 10 > p.getHeight()) {

                        } else {
                            s.y += s.ym;
                            totalMovement += s.ym * s.ym;
                        }

                        s.setMove(0, 0);
                    }
                    if (done || totalMovement < 0.1f || force) {
                        stopped = true;
                        System.out.println("Done");
                    }
                    done = true;
                    w(1);
                }
                draw();
                if (reset) {
                    squares.clear();
                    force = true;
                    addSquares();
                    addPairs();
                    reset = !reset;
                }
            }
        }

        public void calculateSquare(Square s) {
            float x = (s.a.x + s.b.x) / 2;
            float y = (s.a.y + s.b.y) / 2;

            float x2 = -(y - s.a.y);
            float y2 = (x - s.a.x);

            float d = (float) Math.sqrt(x2 * x2 + y2 * y2);

            if (d != 0) {
                x2 = x2 / d;
                y2 = y2 / d;
            }

            d = d * (float) Math.sqrt(3);

            float x3 = x + (x2 * d);
            float y3 = y + (y2 * d);

            float x4 = x + (x2 * -d);
            float y4 = y + (y2 * -d);

            float dist1 = (float) Math.sqrt((x3 - s.x) * (x3 - s.x) + (y3 - s.y) * (y3 - s.y));
            float dist2 = (float) Math.sqrt((x4 - s.x) * (x4 - s.x) + (y4 - s.y) * (y4 - s.y));

            boolean p1 = inSquare(x3, y3);
            boolean p2 = inSquare(x4, y4);

            if ((dist1 + dist2) / 2 > accuracy) {
                if (p1 && p2) {
                    if (dist1 <= dist2) {
                        moveSquare(x3, y3, s, dist1);
                    } else {
                        moveSquare(x4, y4, s, dist2);
                    }
                } else if (p1) {
                    moveSquare(x3, y3, s, dist1);
                } else {
                    moveSquare(x4, y4, s, dist2);
                }
            }

        }

        public void moveSquare(float a, float b, Square s, float dist) {
            if (dist <= speed) {
                s.setMove((a - s.x) * ((float) Math.sqrt(2) * 0.1f) / 2f,
                        (b - s.y) * ((float) Math.sqrt(2) * 0.1f) / 2f);
            } else {
                float x = a - s.x;
                float y = b - s.y;
                float d = (float) Math.sqrt(x * x + y * y);
                x = x / d;
                y = y / d;

                s.setMove(x * speed, y * speed);

                if (vibrating(s) && s.hasP) {
                    s.setMove(0, 0);
                } else {
                    s.px = s.x;
                    s.py = s.y;
                    s.hasP = true;
                }

            }
        }

        public float squareDist(Square a, Square b) {
            return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
        }

        public boolean inSquare(float a, float b) {
            boolean isTrue = a > j.getWidth() || b > j.getHeight()
                    || a < 0 || b < 0;
            return !isTrue;
        }

        public boolean vibrating(Square s) {
            float xd = (s.px - (s.x + s.xm));
            float yd = (s.py - (s.y + s.ym));
            float xd2 = (s.px - s.x);
            float yd2 = (s.py - s.y);

            return (xd * xd + yd * yd) < (xd2 * xd2 + yd2 * yd2);
        }

    };
    ArrayList<Square> squares = new ArrayList<Square>();
    int number = 7;
    JFrame j = new JFrame("JoeFrame");
    JPanel menu = new JPanel();
    JButton b = new JButton("Reset");
    JButton b2 = new JButton("Solve");
    JButton b3 = new JButton("Stop");
    float speed = 1;

    public static void main(String[] args) {
        TriangleSolver t = new TriangleSolver();
        t.init();

    }

    public void draw() {

        Graphics g = p.getGraphics();

        BufferedImage bf = new BufferedImage(p.getWidth(), p.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics gr = bf.getGraphics();
        for (Square s : squares) {
            gr.drawRect((int) Math.floor(s.x + 0.5) - 10,
                    (int) Math.floor(s.y + 0.5) - 10, 20, 20);
            if (lines) {
                gr.drawLine(
                        (int) Math.floor(s.x + 0.5),
                        (int) Math.floor(s.y + 0.5),
                        (int) Math.floor(s.a.x + 0.5),
                        (int) Math.floor(s.a.y + 0.5));
                gr.drawLine(
                        (int) Math.floor(s.x + 0.5),
                        (int) Math.floor(s.y + 0.5),
                        (int) Math.floor(s.b.x + 0.5),
                        (int) Math.floor(s.b.y + 0.5));
                gr.drawLine(
                        (int) Math.floor(s.a.x + 0.5),
                        (int) Math.floor(s.a.y + 0.5),
                        (int) Math.floor(s.b.x + 0.5),
                        (int) Math.floor(s.b.y + 0.5));
            }
        }

        g.drawImage(bf, 0, 0, null);
    }

    public void init() {
        if (number < 3) {
            number = 3;
        }
        j.setBounds(0, 0, 800, 600);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset = true;
            }

        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                force = true;
            }

        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopped = false;
                force = false;
            }

        });

        GridLayout gl = new GridLayout(5, 1);
        GridLayout gl2 = new GridLayout(2, 1);
        GridLayout gl3 = new GridLayout(1, 2);
        menu.setLayout(gl);

        menu2.setLayout(gl2);
        l.setText(number + "");
        menu2.add(l);
        menu2.add(menu3);

        menu3.setLayout(gl3);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                number++;
                l.setText(number + "");
            }

        });

        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                number--;
                l.setText(number + "");
            }

        });

        line.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lines = !lines;
            }

        });

        menu3.add(add);
        menu3.add(minus);

        menu.add(b);
        menu.add(b2);
        menu.add(b3);
        menu.add(menu2);
        menu.add(line);

        j.add(menu, BorderLayout.LINE_START);
        j.add(p, BorderLayout.CENTER);
        j.revalidate();
        addSquares();
        addPairs();
        j.update(j.getGraphics());
        j.repaint();
        t.start();

    }

    public void addPairs() {
        for (Square s : squares) {
            ArrayList<Square> squares2 = ((ArrayList<Square>) squares.clone());
            squares2.remove(s);
            s.a = squares2.get((int) Math.floor((Math.random() * (squares2.size() - 1)) + 0.5));
            squares2.remove(s.a);
            s.b = squares2.get((int) Math.floor((Math.random() * (squares2.size() - 1)) + 0.5));
        }
    }

    public void addSquares() {
        for (int n = 0; n < Math.max(number, 3); n++) {
            addRandomSquare();
        }
    }

    public void addRandomSquare() {
        squares.add(new Square(
                (int) ((Math.random() * (p.getWidth() - 40)) + 20),
                (int) ((Math.random() * (p.getHeight() - 40)) + 20)));
    }

    public void w(long l) {
        try {
            Thread.sleep(l);
        } catch (Exception e) {

        }
    }

}

class Square {

    float x, y, xm, ym, px, py;
    boolean hasP;
    Square a, b;

    public void addPairs(Square a, Square b) {
        this.a = a;
        this.b = b;
    }

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setMove(float a, float b) {
        xm = a;
        ym = b;
    }

    public int round(float a) {
        return (int) Math.floor(a + 0.5);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
