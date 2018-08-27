package MP.gfx;

import MP.Mail;
import javafx.scene.text.FontWeight;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Maprender extends JPanel implements Runnable{

    String addresses[];
    boolean running;
    int coordinates[][];
    JFrame frame;
    int speed = 5;
    int diff = 0;

    ArrayList<Mail> maillistahan;
    int fac = 300;
    int lin = 0;


    public static final int WIDTH = 160 * 4;
    public static final int HEIGHT = 160*4;
    public static final String NAME = "Delivery Simulation";

    public Maprender(String addresses[]){
        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setPreferredSize(dimension);
        Random rand = new Random();
        this.addresses = addresses;
        this.coordinates = new int[2][addresses.length];
        int center = HEIGHT/2-10;
        coordinates[0][0] = center;
        coordinates[1][0] = center;
        coordinates[0][1] = center;
        coordinates[1][1] = center;

        for(int i = 2; i < addresses.length-1; i++){
            coordinates[1][i] = rand.nextInt(HEIGHT); // this so facking bad im sorry
            if(coordinates[1][i] > HEIGHT - 50){
                coordinates[1][i] = HEIGHT - 55;
            }
            coordinates[0][i] = rand.nextInt(WIDTH);

            if(coordinates[0][i] > WIDTH - 50){
                coordinates[0][i] = WIDTH - 55;
            }
        }


    }

    @Override
    public void run() {
        frame = new JFrame();

        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setResizable(false);
        running = true;
        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        frame.setMinimumSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setPreferredSize(dimension);
        frame.add(this);



        double timePerTick = 1000000000 / 60;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if(delta >= 1){
                render();
                ticks++;
                delta--;
            }

            if(ticks > 40){
                if(lin < addresses.length - 1){
                    lin++;
                }

                else {
                    stop();
                }
                ticks = 0;
            }


        }

    }

    public void render(){
        if(fac > 0)
            fac--;
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D gra = (Graphics2D) g;
        gra.setFont( new Font( "Philosopher-BoldItalic", Font.ITALIC | Font.BOLD, 10 ) );

        gra.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gra.setStroke (new BasicStroke ( 5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{ 10f, 10f }, diff ) );
        gra.setPaint (Color.BLACK);

        for(int i = 1; i < addresses.length - 1; i++){
            g.setColor(Color.PINK);
            g.fillOval(coordinates[0][i],coordinates[1][i],10,10);
            g.drawString(addresses[i].substring(0,15) + "...", coordinates[0][i],coordinates[1][i] + 20);
        }

        for(int i = 1; i < lin; i++){
            g.setColor(Color.RED);
            g.drawLine(coordinates[0][i-1] + 5, coordinates[1][i-1] + 5,coordinates[0][i] + 5,coordinates[1][i] + 5);
        }

    }

    public void stop(){
        running = false;
        frame.dispose();
    }

    public boolean isRunning() {
        return running;

    }
}
