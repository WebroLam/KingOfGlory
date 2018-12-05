package com.jerry.glory;
import com.jerry.mapObjects.Location;
import com.jerry.mapObjects.MapBoundaries;
import com.jerry.mapObjects.MapObject;
import com.jerry.mapObjects.heroes.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;
import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Vector;
import static com.jerry.glory.ViewChooseHero.PlayerChosen;

import static com.jerry.jsonHandle.readJSONStringFromFile;

public class GUI extends JPanel {
    static final int imageSizeX = 24;
    static final int imageSizeY = 24;// Use this to drawImage.
    public JTextArea text = new JTextArea();
    public File ioFile;
    public BufferedImage background;

    private JFrame infoFrame = new JFrame("info");
    JFrame frame = new JFrame("Game");

    public Vector<Hero> heroes = null;
    Location[] SpawnPointTeamRed = new Location[10];
    Location[] SpawnPointTeamBlue = new Location[10];
    boolean MapMatrix[][];
    public static int Frame_Width = 25 * imageSizeX;
    public static int Frame_Height = 15 * imageSizeY;
    public static int ScreenHeight = 15;
    public static int ScreenWidth = 25;
    public Hero player;
    public HashMap<MapObject, Location> mapObjectLocation = new HashMap<MapObject, Location>();
    public void launchFrame() {
        setSize(Frame_Width, Frame_Height);
        setLocation(1000, 100);
        setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(Frame_Width, Frame_Height + 100);
        frame.setVisible(true);

        JScrollPane infoScroll = new JScrollPane(text);
        infoFrame.add(infoScroll);
        infoFrame.setSize(200,800);
        infoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        infoFrame.setVisible(true);
    }
    public GUI(Hero hero) {// test only
        super();
        mapObjectLocation = new HashMap<MapObject, Location>();
    }

    public GUI() {
        super();
        ioFile = new File("commands.txt");
        try {
            FileWriter writer = new FileWriter(ioFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        MouseLis mouseListener = new MouseLis();
        this.addMouseListener(mouseListener);
        initSpawnLocation();

        Thread ViewThread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try{
                        Thread.sleep(25); // FPS = 1000 / 25 = 40
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    repaint();
                }
            }
            });
        ViewThread.setPriority(Thread.MAX_PRIORITY);
        ViewThread.start();
        initHero();
        addObjects();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(MapObject obj : mapObjectLocation.keySet()) {
            Location baseLoc = mapObjectLocation.get(obj);
            Location converted = new Location(baseLoc.xLoc * imageSizeX,baseLoc.yLoc * imageSizeY);
            obj.draw(g,converted);
        }
    }



    /**
     * Add bounds to the game, such as trees.
     */
    private void addObjects() {
        for(int j =0;j < ScreenHeight;j++) {
            mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(0,j));
            mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(ScreenWidth - 1,j));
        }
        for(int i = 0;i < ScreenWidth;i++) {
            mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(i,0));
            mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(i,ScreenHeight - 1));
        } // Adding objects for the constraint of the map.

        for(int i = 7,j = 11, count = 0;count <= 8;count++) {
            mapObjectLocation.put(new MapBoundaries(), new Location(i++,j--));
        }
//		for(int j =0;j < ScreenHeight;j++) {
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(j)),new Location(0 + 1,j));
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(j)),new Location(ScreenWidth - 2,j));
//		}
//		for(int i = 0;i < ScreenWidth;i++) {
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(i)),new Location(i,1));
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(i)),new Location(i,ScreenHeight - 2));
//		}

        //TODO: Find the object locations.
        final int objLocations[] = {};

    }
    // TODO: Main Thread will be jamed when a hero is pending to be respawned.
    // Doing something with the heroes.
    private void initHero() {
        Hero chosen = PlayerChosen(frame,text);
        heroes = new Vector<Hero>();
        final int heroSize = 10;
        try {
            JSONObject jsonObject = new JSONObject(readJSONStringFromFile("Heroes.json"));
            JSONArray heroJSON = jsonObject.getJSONArray("Heroes");
            int index = 0;
            for(;index < 5;index++) {
                heroes.insertElementAt(new Hero(heroJSON.getJSONObject(index),this),heroes.size());
            }

            for(;index < 10;index++) {
                heroes.insertElementAt(new autoProtecter(heroJSON.getJSONObject(index),this),heroes.size());
            }
        } catch (JSONException e) {
            System.out.println(e.toString());
        }
        for (Hero hero: heroes) {
            SpawnHero(hero);
        }
        for(Hero hero : heroes) {
            if(hero.getId() == chosen.getId()) {
                player = hero;
            } else {
                hero.startAutoPerform();
            }
        }
    }
    private void initSpawnLocation() {
        int indexForBlue = 0;
        for(int i = 1;i <= 2;i++) {
            for(int j = 1;j<=5;j++) {
                SpawnPointTeamBlue[indexForBlue++] = new Location(i,j);
            }
        }
        int indexForRed = 0;
        for(int i = 22;i <= 23;i++) {
            for(int j = 9;j<=13;j++)
                SpawnPointTeamRed[indexForRed++] = new Location(i,j);
        }
    }
    public void SpawnHero(@NotNull Hero hero) {
        Location[] SpawnPoints;
        if(hero.Team.equals("Red")) {
            SpawnPoints = SpawnPointTeamRed;
        }
        else {
            SpawnPoints = SpawnPointTeamBlue;
        }
        boolean foundSame = false;
        for(int i = 0;i < SpawnPoints.length;i++) {
            foundSame = false;
            for (Location loc : mapObjectLocation.values()) {
                if(loc.equals(SpawnPoints[i])) {
                    foundSame = true;
                    break;
                }
            }
            if(foundSame)
                continue;
            Location foundLoc = new Location(SpawnPoints[i]);
            mapObjectLocation.put(hero,foundLoc);
        }
    }



    public static void main(String [] args) {
        GUI game = new GUI();
        game.launchFrame();

    }
    //TODO: Implement all this stuff.
    // Mouse event button 1 for main, 3 for sub
    class MouseLis implements MouseListener {
        public void mouseClicked(MouseEvent event) {
            Location target = new Location(event.getX() / imageSizeX , event.getY()/imageSizeY);
            for(Hero hero : heroes) {
                if(mapObjectLocation.get(hero).distanceTo(target) == 0) {
                    player.attack(hero);
                    text.append(hero.name + " got attacked. Current health: " + hero.getCurrentHealth() + "\n");
                    return;
                }
            }

            player.moveTo(new Location(event.getX()/imageSizeX,event.getY()/imageSizeY));
            text.append("Got :" + event.getX()/imageSizeX+" and " + event.getY()/imageSizeY + "\n");
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



}
