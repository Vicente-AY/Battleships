package player;

import battle.NavalBattle;
import battle.Ship;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {

    String name = null;
    NavalBattle navalBattle = null;
    ArrayList<Point> shoted = new ArrayList<Point>();
    Ship[] ships = new Ship[]{new Ship(1), new Ship(2), new Ship(3), new Ship(4), new Ship(5)};

    public Player(String name){

        this.name = name;
    }

    public static void createPlayer(){

        Scanner input = new Scanner(System.in);
        String name = input.nextLine();

        Player newPlayer = new Player(name);
    }

    //Getters y Setters

    public Ship[] getShips(){
        return ships;
    }
    public NavalBattle getNavalBattle(){
        return navalBattle;
    }
    public String getName(){
        return name;
    }
}
