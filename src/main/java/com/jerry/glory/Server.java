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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import static com.jerry.glory.ViewChooseHero.PlayerChosen;

import static com.jerry.jsonHandle.readJSONStringFromFile;

public class Server {
    // SERVER
    public static final int SERVER_PORT = 13;
    public static final int MAX_PLAYER =  2;
    private ServerSocket server;
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
    private int active_user;
    boolean MapMatrix[][];
    public static int Frame_Width = 25 * imageSizeX;
    public static int Frame_Height = 15 * imageSizeY;
    public static int ScreenHeight = 15;
    public static int ScreenWidth = 25;
    public Hero player;
    public HashMap<MapObject, Location> mapObjectLocation = new HashMap<MapObject, Location>();
    private void initSocket() {
        try {
            ServerSocket server = new ServerSocket(SERVER_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addHero(int HeroNum) {
        if(heroes == null)
            heroes = new Vector<Hero>();
        try {
            JSONObject jsonObject = new JSONObject(readJSONStringFromFile("Heroes.json"));
            JSONArray heroJSON = jsonObject.getJSONArray("Heroes");
            int index = HeroNum;
            heroes.insertElementAt(new Hero(heroJSON.getJSONObject(index)),0);
            active_user++;
        } catch (JSONException e) {
            System.out.println(e.toString());
        }
        for (Hero hero: heroes) {
            SpawnHero(hero);
        }
    }
    private void HandleRequest(Socket client) {
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String temp = buf.readLine();
            if(temp.equals("request")) {
                PrintStream out = new PrintStream(client.getOutputStream());
                out.println(heroes.size());
                for (Hero hero : heroes) {
                    out.println(hero.id);
                    out.println(mapObjectLocation.get(hero).xLoc);
                    out.println(mapObjectLocation.get(hero).yLoc);
                }
            } else {
                int HeroNum = Integer.parseInt(temp);
                int xLoc = Integer.parseInt(buf.readLine());
                int yLoc = Integer.parseInt(buf.readLine());
                Location clickLoc = new Location(xLoc,yLoc);
                ProcessClick(HeroNum,clickLoc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void ProcessClick(int HeroNum,Location loc) {
        Hero player = null;
        for(Hero hero : heroes) {
            if(hero.id == HeroNum)
                player = hero;
        }
        for(Hero hero : heroes) {
            if(mapObjectLocation.get(hero).distanceTo(loc) == 0) {
                player.attack(hero);
                text.append(hero.name + " got attacked. Current health: " + hero.getCurrentHealth() + "\n");
                return;
            }
        }

        player.moveTo(new Location(loc));

    }

    private void HandleLogin(Socket client) {
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String temp = buf.readLine();
            int HeroNum = Integer.parseInt(temp);
            addHero(HeroNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void OnLogin() {
        while(active_user < MAX_PLAYER) {
            Socket client = null;
            try {
                client = server.accept();
                HandleLogin(client);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Server() {
        initSpawnLocation();
        initSocket();
        addObjects();
        OnLogin();
        new Thread(new Runnable() {
            public void run() {
                Socket client = null;
                try {
                    synchronized (mapObjectLocation) {
                        Thread.sleep(10);
                      client = server.accept();
                    HandleRequest(client);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
        Server server = new Server();
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
