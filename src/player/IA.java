package player;


import battle.NavalBattle;
import battle.Ship;
import utils.Impact;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class IA {

    String name = "A.D.M.I.R.A.L";
    NavalBattle navalBattle = new  NavalBattle();
    ArrayList<Point> shoted = new ArrayList<Point>();
    Ship[] ships = new Ship[]{new Ship("Carrier", 5), new Ship("Battleship", 4), new Ship("Cruiser", 3), new Ship("Submarine", 2), new Ship("Patrol Boat", 1)};
    boolean lost = false;

    public void iaShipPositioning(){

        Random rand = new Random();
        boolean horizontal = true;
        boolean cont = true;
        int x, y = 0;
        boolean placed = false;

        for(Ship ship : ships) {
            placed = false;
            while (!placed) {
                horizontal = rand.nextBoolean();

                if (horizontal) {
                    x = rand.nextInt(10 - ship.getSize());
                    y = rand.nextInt(10);
                } else {
                    x = rand.nextInt(10);
                    y = rand.nextInt(10 - ship.getSize());
                }

                if (this.navalBattle.canPlace(x, y, horizontal, ship)) {
                    this.navalBattle.setShips(x, y, horizontal, ship);
                    placed = true;
                }
            }
        }
    }

    public void shot(Player player) {

        NavalBattle navalB = player.getNavalBattle();

        boolean canShot = false;
        Random rand = new Random();
        int x = 0, y = 0;

        while (!canShot) {
            x = rand.nextInt(10);
            y = rand.nextInt(10);

            Point aim = new Point(x, y);

            if (!shoted.contains(aim)) {
                canShot = true;
                navalB.shot(x, y);
                shoted.add(aim);
            }
        }
        player.checkDefeated();
    }

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

    //Getters y setters

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
    public ArrayList<Point> getShoted() {
        return this.shoted;
    }
}
