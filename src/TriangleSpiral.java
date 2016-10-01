
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Joe
 */
public class TriangleSpiral {

    /**
     * @param args the command line arguments
     */
    boolean force = false;
    boolean stopped = false;
    boolean reset = false;
    boolean lines = true;
    int size = 200;
    JPanel menu2 = new JPanel();
    JPanel menu3 = new JPanel();
    JLabel l = new JLabel("", JLabel.CENTER);

    Vector screen = new Vector(800, 600);

    JButton add = new JButton("+");
    JButton minus = new JButton("-");
    JButton line = new JButton("Lines");

    JPanel p = new JPanel();

    ArrayList<Triangle> array = new ArrayList<Triangle>();

    Thread t = new Thread() {
        boolean done = true;

        public void run() {
            solveDistances();
        }

        public void solveDistances() {
            while (true) {
                screen = new Vector(j.getSize());

                if (!stopped) {
                    Triangle t = array.get(0);

                    Vector f = t.list.get(t.list.size() - 1);
                    Vector a = t.list.get(t.list.size() - 3);
                    Vector b = t.list.get(t.list.size() - 2);

                    Vector a2b = b.subi(a);

                    Vector next = a.addScaledi(a2b, 0.05f);
                    Vector n2f = next.subi(f);

                    if(n2f.magSqr() < 20 * 20){
                        stopped = true;
                    }
                    
                    
                    if (t.last != null) {
                        t.last.addScaled(new Vector(n2f.normalizei()), speed);
                        if (next.subi(t.last).dot(n2f.negatei()) > 0) {
                            t.list.add(next);
                            t.last = null;
                        }
                    } else {
                        t.last = f.addScaledi(new Vector(n2f.normalizei()), speed);
                    }

                }
                draw();
            }
        }
    };

    JFrame j = new JFrame("JoeFrame");
    JPanel menu = new JPanel();
    JButton b = new JButton("Reset");
    JButton b2 = new JButton("Solve");
    JButton b3 = new JButton("Stop");
    float speed = 1f;

    public static void main(String[] args) {
        TriangleSpiral t = new TriangleSpiral();
        t.init();

    }

    public void draw() {

        Graphics g = p.getGraphics();

        BufferedImage bf = new BufferedImage(p.getWidth(), p.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics gr = bf.getGraphics();
        for (Triangle tri : array) {
            Vector a = tri.list.get(0);
            for (int i = 1; i < tri.list.size(); i++) {
                Vector b = tri.list.get(i);
                drawLine(gr, a, b);
                a = b;
            }
            if (tri.last != null) {
                drawLine(gr, a, tri.last);
            }
        }

        g.drawImage(bf, 0, 0, null);
    }

    public void drawLine(Graphics g, Vector a, Vector b) {
        g.drawLine(
                (int) Math.floor(a.getX() + 0.5),
                (int) Math.floor(a.getY() + 0.5),
                (int) Math.floor(b.getX() + 0.5),
                (int) Math.floor(b.getY() + 0.5));
    }

    public void resetTraingle() {
        array.clear();
        int xc = (int) Math.floor(200 + (screen.getXi() - 400) * Math.random() + 0.5);
        int yc = (int) Math.floor(150 + (screen.getYi() - 300) * Math.random() + 0.5);
        array.add(generateTriangle(new Vector(xc, yc), size));
    }

    public Triangle generateTriangle(Vector v, float s) {
        float angle = (2 * (float) Math.PI) / 3f;

        float a1 = (float) (angle / 2f * Math.random());
        float a2 = angle + (float) (angle / 2f * Math.random());
        float a3 = 2 * angle + (float) (angle / 2f * Math.random());

        Triangle tri = new Triangle();
        tri.list.add(v.addScaledi(new Vector(Math.sin(a1), Math.cos(a1)), s));
        tri.list.add(v.addScaledi(new Vector(Math.sin(a2), Math.cos(a2)), s));
        tri.list.add(v.addScaledi(new Vector(Math.sin(a3), Math.cos(a3)), s));
        tri.list.add(v.addScaledi(new Vector(Math.sin(a1), Math.cos(a1)), s));

        return tri;
    }

    public void init() {
        resetTraingle();

        j.setBounds(0, 0, screen.getXi(), screen.getYi());
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset = true;
                resetTraingle();
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
        l.setText(size + "");
        menu2.add(l);
        menu2.add(menu3);

        menu3.setLayout(gl3);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                size++;
                l.setText(size + "");
            }

        });

        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                size--;
                l.setText(size + "");
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
        j.update(j.getGraphics());
        j.repaint();
        t.start();

    }

    public void w(long l) {
        try {
            Thread.sleep(l);
        } catch (Exception e) {

        }
    }

}

class Triangle {

    ArrayList<Vector> list = new ArrayList<Vector>();
    Vector move = null;
    Vector last = null;
    Vector next = null;
}
