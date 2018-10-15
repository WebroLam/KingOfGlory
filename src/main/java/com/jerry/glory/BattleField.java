package com.jerry.glory;
import java.util.*;
/**
 * The battle field of the game.
 * The VIEW of the whole program
 * @author Jerry
 */

public class BattleField {
	int ScreenWidth;
	int ScreenHeight;
	String [][] Map;
	Vector<Hero> heroes;
	BattleField(int width, int height) {
		ScreenWidth = width;
		ScreenHeight = height;
		heroes = new java.util.Vector<Hero>();
		Map = new String[height][width];
		for(int i = 0;i < height;i++)
			for(int j = 0;j < width;j++)
				Map[i][j] = "-";

		heroes.insertElementAt(new Hero(2,3),0);
	}
	public void paintBattleField() {
		for(int i = 0;i < ScreenHeight;i++)
			for(int j = 0;j < ScreenWidth;j++)
				Map[i][j] = "-";
		for(int i = 0;i < heroes.size();i++) {
			printAppearanceOnMap(heroes.elementAt(i).appearance,2,3);
		}
	}
	public void printBattleField() {
		paintBattleField();
		for(int i = 0;i < ScreenHeight;i++) {
			for(int j = 0;j < ScreenWidth;j++)
				System.out.print(Map[i][j]);
			System.out.println();
		}
	}
	private void printAppearanceOnMap(String appearance, int xOffSet,int yOffSet) {
		Map[xOffSet][yOffSet] = appearance;
	}
	public static void main(String [] args) {
		BattleField battleField = new BattleField(50,30);
		battleField.printBattleField();
	}

}
