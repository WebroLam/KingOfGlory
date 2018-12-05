package com.jerry.glory;

import com.jerry.mapObjects.Location;
import com.jerry.mapObjects.MapObject;
import com.jerry.mapObjects.heroes.Hero;
import com.jerry.mapObjects.heroes.autoProtecter;
import com.jerry.mapObjects.heroes.automatic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import static com.jerry.glory.GUI.Frame_Height;
import static com.jerry.glory.GUI.Frame_Width;
import static com.jerry.glory.GUI.imageSizeY;
import static com.jerry.jsonHandle.readJSONStringFromFile;

public class ViewChooseHero extends JComponent{
    public final static int start = 5;
    class ChooseIcon implements MapObject{
        public BufferedImage image;
        public void drawOnMap(String[][] Map, Location loc){

        }
        public void draw(Graphics g,Location loc) {
            g.drawImage(image,loc.xLoc,loc.yLoc,null);

        }
        public ChooseIcon() {
            String  imagePath = ImageFilePath + "choose.png";
            try {
                File imageFile = new File(imagePath);
                image = ImageIO.read(imageFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    ChooseIcon icon = new ChooseIcon();
    Hero player;
    public final static int ImageX = 24;
    public final static int ImageY = 24;
    public JTextArea text = new JTextArea();
    private JFrame infoFrame = new JFrame("info");

    public HashMap<MapObject, Location> mapObjectLocation = new HashMap<MapObject, Location>();
    Vector<Hero> heroes = new Vector<Hero>();
    public ViewChooseHero() {
        final int y = 7;
        final int heroSize = 10;
        try {
            JSONObject jsonObject = new JSONObject(readJSONStringFromFile("Heroes.json"));
            JSONArray heroJSON = jsonObject.getJSONArray("Heroes");
            int index = 0;
            for(;index < 5;index++) {
                heroes.insertElementAt(new Hero(heroJSON.getJSONObject(index)),0);
            }

            for(;index < 10;index++) {
                heroes.insertElementAt(new automatic(heroJSON.getJSONObject(index)),heroes.size());
            }
            int x = start;
            mapObjectLocation.put(icon,new Location(x,y + 1));
            for(Hero hero:heroes) {
                mapObjectLocation.put(hero,new Location(x,y));
                x++;
            }
            setSize(Frame_Width, Frame_Height);
            setLocation(1000, 100);
            setVisible(true);
        } catch (JSONException e) {
            System.out.println(e.toString());
        }
        this.addMouseListener(new MouseLis());
        this.addKeyListener(new KeyLis());

//        JScrollPane infoScroll = new JScrollPane(text);
//        //Init info frame, delete later.
//        infoFrame.add(infoScroll);
//        infoFrame.setSize(200,800);
//        infoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        infoFrame.setVisible(true);
//        // END
//
//        text.append("Run here");

    }

    public static Hero PlayerChosen (JFrame frame,JTextArea text) {
        ViewChooseHero chooseView = new ViewChooseHero();
        frame.add(chooseView);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(Frame_Width, Frame_Height + 100);
        chooseView.setVisible(true);
        frame.setVisible(true);

        frame.add(chooseView);
        chooseView.repaint();
        while(chooseView.player == null) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        text.append("quq\n");
        frame.remove(chooseView);
        return chooseView.player;
    }


    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for(MapObject hero: mapObjectLocation.keySet()) {
            Location baseLoc = mapObjectLocation.get(hero);
            Location converted = new Location(baseLoc.xLoc * ImageX, baseLoc.yLoc * ImageY);
            hero.draw(graphics,converted);
        }
    }
    public static void main(String [] args) {
//        JFrame frame = new JFrame("test");
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        ViewChooseHero view = new ViewChooseHero();
//        view.setVisible(true);
//        frame.add(view);
//        frame.setSize(Frame_Width, Frame_Height + 100);
//        frame.setVisible(true);
        PlayerChosen(new JFrame("Test"),null);

    }
    class MouseLis implements MouseListener {
        public void mouseClicked(MouseEvent event) {
            Location target = new Location(event.getX() / ImageX , event.getY()/ImageY);
            for(Hero hero:heroes) {
                if(mapObjectLocation.get(hero).distanceTo(target) == 0) {
                    player = hero;
                }
            }
        }
        public void mousePressed(MouseEvent event) {

        }
        public void mouseReleased(MouseEvent event) {

        }
        public void mouseEntered(MouseEvent event) {

        }
        public void mouseExited(MouseEvent event) {

        }
    }

    class KeyLis implements KeyListener {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_J) {
                if(mapObjectLocation.get(icon).xLoc > start) {
                    mapObjectLocation.get(icon).xLoc--;
                }
            }
            else if(key == KeyEvent.VK_K) {
                if(mapObjectLocation.get(icon).yLoc < start + 10) {
                    mapObjectLocation.get(icon).xLoc++;
                }
            }
            repaint();
            if(key == KeyEvent.VK_ENTER) {
                Hero find;
                for(Hero hero : heroes) {
                    if(mapObjectLocation.get(hero).xLoc == mapObjectLocation.get(icon).xLoc) {
                        player = hero;
                        break;
                    }
                }
            }
        }

        public void keyTyped(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {

        }
    }


}
