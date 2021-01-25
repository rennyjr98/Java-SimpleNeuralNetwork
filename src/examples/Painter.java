package examples;

import models.ArrayMap;
import models.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Painter extends JFrame {
    private JCanvas canvas;
    private Button clear;
    private Button isCat;
    private Button train;
    private Button predict;
    private Button save;
    private Button triesind;
    private NeuralNetwork nn;

    private boolean isDrawCat = false;

    public Painter() {
        this.setTitle("Painter");
        this.setResizable(false);
        this.setSize(new Dimension(1200, 800));
        this.setLocationRelativeTo(null);

        this.canvas = new JCanvas();
        this.canvas.setLayout(null);
        this.addMouseListener(this.canvas);
        this.addMouseMotionListener(this.canvas);
        this.getContentPane().add(this.canvas);

        this.onInit();

        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void onInit() {
        this.clear = new Button();
        this.isCat = new Button();
        this.train = new Button();
        this.predict = new Button();
        this.save = new Button();
        this.triesind = new Button();
        String[] names = new String[]{"Clear", "Cat", "Train", "Predict", "Save", "Tries"};
        Button[] btns = new Button[]{this.clear, this.isCat, this.train, this.predict, this.save, this.triesind};

        for(int i = 0; i < btns.length; i++) {
            btns[i].setLabel(names[i]);
            btns[i].setFont(new Font("Agency FB", Font.BOLD, 18));
            btns[i].setBackground(Color.WHITE);
            btns[i].setSize(new Dimension(90, 30));
            btns[i].setLocation(1200-95*(i+1), 800-80);
        }

        this.clear.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                canvas.clear();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.isCat.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isDrawCat = !isDrawCat;
                isCat.setBackground((isDrawCat) ? Color.cyan : Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.train.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double[] inputs = canvas.getImg();
                boolean correctAnswer = false;
                int tries = 0;
                if(nn == null) {
                    try {
                        nn = NeuralNetwork.deserialize();
                    } catch (Exception err) {
                        System.out.println(err.getMessage());
                    }
                }

                while(!correctAnswer) {
                    nn.train(inputs, new double[]{(isDrawCat) ? 1 : 0});
                    if(nn.predict(inputs)[0] >= .5 && isDrawCat) {
                        correctAnswer = true;
                    } else if(nn.predict(inputs)[0] < .5 && !isDrawCat) {
                        correctAnswer = true;
                    }
                    tries++;
                    if(tries%100 == 0)
                        nn.mutate(0.5);
                    if(tries > 100000)
                        break;
                    triesind.setLabel(tries+"");
                }
                JOptionPane.showMessageDialog(null,
                        "Train successfully : " + tries + " tries");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.predict.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double[] inputs = canvas.getImg();
                if(nn == null) {
                    try {
                        nn = NeuralNetwork.deserialize();
                    } catch (Exception err) {
                        System.out.println(err.getMessage());
                    }
                }
                double[] result = nn.predict(inputs);
                JOptionPane.showMessageDialog(null,
                        (result[0] < 0.5f) ? "Is not a cat" : "It's a cat");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if(nn == null) {
                        JOptionPane.showMessageDialog(null, "First train it");
                    } else {
                        nn.serialize();
                        JOptionPane.showMessageDialog(null, "Saved successfully!");
                    }
                } catch(IOException err) {
                    System.out.println(err.getMessage());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.canvas.setFocusable(true);
        this.canvas.add(this.clear);
        this.canvas.add(this.isCat);
        this.canvas.add(this.train);
        this.canvas.add(this.predict);
        this.canvas.add(this.save);
        this.canvas.add(this.triesind);
    }
}

class JCanvas extends JPanel implements Runnable, MouseListener, MouseMotionListener {
    private boolean keepThreading = true;
    private boolean canPaint = false;

    private Thread thread;
    private Graphics2D g2d;
    private BufferedImage img = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
    private ArrayList<Point> pathDraw = new ArrayList<Point>();

    public JCanvas() {
        this.setPreferredSize(new Dimension(1200, 800));
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        this.g2d = this.img.createGraphics();
        this.g2d.setColor(Color.WHITE);
        g2d.setColor(Color.WHITE);
        this.g2d.fillRect(0,0, 1200, 800);
        g2d.fillRect(0,0, 1200, 800);

        this.g2d.setColor(Color.DARK_GRAY);
        g2d.setColor(Color.DARK_GRAY);
        for(Point p : this.pathDraw) {
            this.g2d.fillRect(p.x-10, p.y-40, 8, 8);
            g2d.fillRect(p.x-10, p.y-40, 8, 8);
        }
    }

    public void clear() {
        this.pathDraw.clear();
        this.g2d.clearRect(0, 0, 1200, 800);
    }

    public double[] getImg() {
        Image tmp = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);

        int rows = dimg.getWidth();
        int cols = dimg.getHeight();
        double[][] pixels = new double[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                pixels[i][j] = getIntGrayScalePixel(new Color(dimg.getRGB(i, j)));
                //System.out.print(pixels[i][j] + ", ");
            }
            //System.out.println();
        }
        return new ArrayMap(pixels).toArray();
    }

    private static double getIntGrayScalePixel(Color c){
        double r = c.getRed();
        double g = c.getGreen();
        double b = c.getBlue();
        double contrast = ((r+g+b)/3)/255;

        if(contrast > 0.5 && contrast < 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void run() {
        while(this.keepThreading) {
            try {
                repaint();
                Thread.sleep(20);
            } catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.pathDraw.add(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.pathDraw.add(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}