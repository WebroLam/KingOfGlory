package com.jerry.mapObjects.heroes;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class temp extends JPanel {

    JLabel label;

    public temp(String s) {
        super();
        JPanel p = new JPanel();
        label = new JLabel("Key Listener!");
        p.add(label);
        add(p);
        addKeyListener(new KeyLis());
        setSize(200, 100);
        setVisible(true);

    }
    class KeyLis implements KeyListener {
        public void keyTyped(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                System.out.println("Right key typed");
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                System.out.println("Left key typed");
            }

        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                System.out.println("Right key pressed");
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                System.out.println("Left key pressed");
            }

        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                System.out.println("Right key Released");
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                System.out.println("Left key Released");
            }
        }
    }


    public static void main(String[] args) {
        new temp("Key Listener Tester");
    }
}