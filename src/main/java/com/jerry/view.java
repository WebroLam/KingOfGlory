package com.jerry;
import com.jerry.ability.*;
import com.jerry.mapObjects.*;
import com.jerry.glory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

public class view {
    private static final Logger logger = LogManager.getLogger(view.class);
    public static void main(String [] args) {
        MyMouseHandle mouseHandle = new MyMouseHandle();

    }
}



class MyMouseHandle extends JFrame implements MouseListener {

    private JTextArea text = new JTextArea();
    private JButton button = new JButton();

    private JTextPane textPane = new JTextPane();
    private JTextField textField = new javax.swing.JTextField();
    BattleFieldControl battleFieldControl;

    public MyMouseHandle() {
        super.add(text);
        super.add(button);
        battleFieldControl = new BattleFieldControl(30,30);
        super.setTitle("Crystal");// 设置标题条
        super.add(textField);
        textField.addMouseListener(this);
        text.append(battleFieldControl.GenerateMapString());
        super.setSize(310, 210);
        super.setVisible(true);
        super.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                System.exit(1);
            }
        });
    }

    public void mouseClicked(MouseEvent e)// 鼠标单击事件
    {
        int c = e.getButton();// 得到按下的鼠标键
        String mouseInfo = null;// 接收信息
        if (c == MouseEvent.BUTTON1)// 判断是鼠标左键按下
        {
            mouseInfo = "左键";
        } else if (c == MouseEvent.BUTTON3) {// 判断是鼠标右键按下
            mouseInfo = "右键";
        } else {
            mouseInfo = "滚轴";
        }
        text.append("鼠标单击：" + mouseInfo + ".\n");
        text.append("Location:" + e.getX() + " " + e.getY());
    }

    public void mouseEntered(MouseEvent e)// 鼠标进入组件
    {
        text.append("鼠标进入组件.\n");
    }

    public void mouseExited(MouseEvent e)// 鼠标退出组件
    {
        text.append("鼠标退出组件.\n");
    }

    public void mousePressed(MouseEvent e)// 鼠标按下
    {
        text.append("鼠标按下.\n");
    }

    public void mouseReleased(MouseEvent e)// 鼠标松开
    {
        text.append("鼠标松开.\n");
    }
}
