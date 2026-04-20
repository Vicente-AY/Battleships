package player;

import battle.NavalBattle;
import battle.Ship;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase que simula un jugador
 */
public class Player {

    String name = null;
    NavalBattle navalBattle = new NavalBattle();
    ArrayList<Point> shoted = new ArrayList<Point>();
    Ship[] ships = new Ship[]{new Ship("Carrier", 5), new Ship("Battleship", 4), new Ship("Cruiser", 3), new Ship("Submarine", 2), new Ship("Patrol Boat", 1)};
    boolean lost = false;

    /**
     * Consturctor de la clase
     * @param name identificador del jugador
     */
    public Player(String name){

        this.name = name;
    }

    /**
     * Deprecated
     * Metodo que simula un disparo por parte del jugador
     * @param cpu jugador controlado por el programa
     */
    public void shot(IA cpu){

        Scanner input = new Scanner(System.in);
        boolean canShot = false;
        NavalBattle navalB = cpu.getNavalBattle();


        System.out.println("Enter X Coordinates");
        int x = input.nextInt();
        input.nextLine();
        System.out.println("Enter Y Coordinates");
        int y = input.nextInt();
        input.nextLine();

        Point aim = new Point(x, y);

        if (!shoted.contains(aim)) {
            canShot = true;
            navalB.shot(x, y);
            shoted.add(aim);
        }

        cpu.checkDefeated();
    }

    /**
     * Metodo que determina, comprobando cuantos barcos quedan a flote, si un jugador ha perdido
     */
    public void checkDefeated(){

        boolean defeated = true;

        for(Ship ship : this.ships){
            if(!ship.getSunk()){
                defeated = false;
                break;
            }
        }
        this.lost = defeated;
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
    public boolean getLost(){
        return lost;
    }
}
