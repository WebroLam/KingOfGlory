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
import java.util.Scanner;
import java.util.Vector;
import static com.jerry.glory.Server.SERVER_PORT;
import static com.jerry.glory.ViewChooseHero.PlayerChosen;
import static com.jerry.jsonHandle.readJSONStringFromFile;
public class Client extends JPanel {
    static final int imageSizeX = 24;
    static final int imageSizeY = 24;// Use this to drawImage.
    private int HeroNum;
    public JTextArea text = new JTextArea();
    public File ioFile;
    private ServerSocket server;
    private JFrame infoFrame = new JFrame("info");
    private int active_user;
    JFrame frame = new JFrame("Game");
    public Vector<Hero> heroes = null;
    public static int Frame_Width = 25 * imageSizeX;
    public static int Frame_Height = 15 * imageSizeY;
    public Socket socket;
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
    public Client(Hero hero) {// test only
        super();
        mapObjectLocation = new HashMap<MapObject, Location>();
    }

    public void addHero(int HeroNum, int x,int y) {

    }

    public Client(int HeroNum) {
        super();
        this.HeroNum = HeroNum;
        MouseLis mouseListener = new MouseLis();
        this.addMouseListener(mouseListener);
        try {
            socket = new Socket("127.0.0.1",SERVER_PORT);
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(HeroNum);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }


        Thread ViewThread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try{
                        Thread.sleep(25); // FPS = 1000 / 25 = 4// 0
                        socket = new Socket("127.0.0.1",SERVER_PORT);
                        BufferedReader buf =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        int size = Integer.parseInt(buf.readLine());
                        for(int i = 0;i < size;i++) {
                            int HeroNum = Integer.parseInt(buf.readLine());
                            int x = Integer.parseInt(buf.readLine());
                            int y = Integer.parseInt(buf.readLine());
                            Location loc = null;
                            for(Hero hero : heroes) {
                                if(hero.id == HeroNum) {
                                    loc = mapObjectLocation.get(hero);
                                    break;
                                }
                            }
                            loc.xLoc = x;
                            loc.yLoc = y;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                    repaint();
                }
            }
            });
        ViewThread.setPriority(Thread.MAX_PRIORITY);
        ViewThread.start();
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





    public static void main(String [] args) {
        Client game = new Client(1);
        game.launchFrame();

    }
    //TODO: Implement all this stuff.
    // Mouse event button 1 for main, 3 for sub
    class MouseLis implements MouseListener {
        public void mouseClicked(MouseEvent event) {
            Location target = new Location(event.getX() / imageSizeX , event.getY()/imageSizeY);
            try {
                socket = new Socket("127.0.0.1",SERVER_PORT);
                PrintStream out = new PrintStream(socket.getOutputStream());
                out.println(HeroNum);
                out.println(target.xLoc);
                out.println(target.yLoc);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
