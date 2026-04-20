package player;


import battle.NavalBattle;
import battle.Ship;
import utils.Impact;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que identifica al jugador controlado por el ordenador
 */
public class IA {

    String name = "A.D.M.I.R.A.L";
    NavalBattle navalBattle = new  NavalBattle();
    ArrayList<Point> shoted = new ArrayList<Point>();
    Ship[] ships = new Ship[]{new Ship("Carrier", 5), new Ship("Battleship", 4), new Ship("Cruiser", 3), new Ship("Submarine", 2), new Ship("Patrol Boat", 1)};
    boolean lost = false;

    /**
     * Clase que se encarga del posicionamiento aleatorio de barcos
     */
    public void iaShipPositioning(){

        Random rand = new Random();
        boolean horizontal = true;
        boolean cont = true;
        int x, y = 0;
        boolean placed = false;

        //bucle que recorre todos los barcos del jugador cpu
        for(Ship ship : ships) {
            placed = false;
            //bucle que se ejecutara continuamente hasta que el barco sea posicionado correctamente
            while (!placed) {
                //determinamos si el barco se posicionará como horizontal con un booleano aleatorio
                horizontal = rand.nextBoolean();

                /*dependiendo del resultado anterior, determinamos como se posicionara el barco de manera aleatoria,
                limitando el espacio disponible restando el tamaño del barco al resultado para que este no se salga de
                los limites del tablero*/
                if (horizontal) {
                    x = rand.nextInt(10 - ship.getSize());
                    y = rand.nextInt(10);
                } else {
                    x = rand.nextInt(10);
                    y = rand.nextInt(10 - ship.getSize());
                }

                //comprobamos si las coordenadas estan libres y posicionamos el barco.
                if (this.navalBattle.canPlace(x, y, horizontal, ship)) {
                    this.navalBattle.setShips(x, y, horizontal, ship);
                    placed = true;
                }
            }
        }
    }

    /**
     * Metodo que simula un disparo por parte del jugador cpu al jugador contrario
     * @param player jugador actual
     */
    public void shot(Player player) {

        //recogemos el tablero del juagor para efectual el disparo sobre el
        NavalBattle navalB = player.getNavalBattle();

        boolean canShot = false;
        Random rand = new Random();
        int x = 0, y = 0;

        //bucle que determina si el jugador cpu puede disparar a la posición
        while (!canShot) {
            //aleatorizamos el disparo
            x = rand.nextInt(10);
            y = rand.nextInt(10);

            //hacemos del resultado anterior un Point
            Point aim = new Point(x, y);

            //el disparo se realizará si el listado de disparo no contenga el intento de disparo actual
            if (!shoted.contains(aim)) {
                canShot = true;
                //reflejamos el disparo en el tablero del jugador contrario
                navalB.shot(x, y);
                //añadimos el disparo al listado de disparo para que no se pueda repetir la misma juagada
                shoted.add(aim);
            }
        }
        player.checkDefeated();
    }

    /**
     * Metodo que comprueba si el jugador ha sido derrotado comprobado si aun le quedan barcos a flote
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
