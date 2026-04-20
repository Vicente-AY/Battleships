package battle;

import utils.Impact;

import java.awt.*;

/**
 * Clase que simula un tablero de juego donde se posicionan los barcos de ambos jugadores
 */
public class NavalBattle {

    Ship[][] grid = new Ship[10][10];
    Impact[][] board = new Impact[10][10];

    /**
     * Metodo con la logica de posicionamiento de barcos
     * @param ship barco seleccionado para ser posicionado
     * @param x identifica el eje horizontal
     * @param y identifica el eje vertical
     * @param horizontal booleano que define si el usuario quiere poner el barco en horizonal o no
     * @return booleano que indica si el barco esta bien posicionado o no
     */
    public boolean setPosition(Ship ship, int x, int y, boolean horizontal){

        //recogemos la variable que indica el tamaño del barco
        int size = ship.size;

        //Comprobamos si el barco cabe en el eje indicado
        if(horizontal && (x + size > 10)){
            System.out.println("The ship is out of the Naval Battle");
            return false;
        }
        if(!horizontal && (y + size > 10)){
            System.out.println("The ship is out of the Naval Battle");
            return false;
        }

        //comprobamos, con un metodo auxiliar, si el barco tropieza con otro ya posicionado
        if(canPlace(x, y, horizontal, ship)){
            //de cumplirla condicion anterior, posicionamos el barco
            setShips(x, y, horizontal, ship);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * metodo que simula el recibimiento de un disparo
     * @param x identifica el eje horizontal
     * @param y identifica el eje vertical
     */
    public void shot(int x, int y){

        Ship ship = grid[y][x];

        //Comprobamos si hay un barco en las coordenadas recibidas. De haberlo, el barco recibira un toque
        if(ship != null){
            ship.hit();
            //comprobamos si el barco esta hundido después de ese toque
            if(ship.getSunk()) {
                this.board[y][x] = Impact.Sunk;
            }
            else{
                this.board[y][x] = Impact.Hit;
            }
        }
        else {
            this.board[y][x] = Impact.Miss;
        }
    }

    /**
     * Metodo que determina si un barco choca con otro a la hora del posicionamiento
     * @param x identifica el eje horizontal
     * @param y identifica el eje vertical
     * @param horizontal booleano que define si el usuario quiere poner el barco en horizonal o no
     * @param ship barco seleccionado
     * @return booleano que identifica si un barco esta siendo posicionado dentro de la posicion de otro
     */
    public boolean canPlace(int x, int y, boolean horizontal, Ship ship){

        /*comprobamos cada posible posición que el barco tendría de ser posicionado en las coordenadas introducidas
        y si el barco está o no en horizontal*/
        for(int i = 0; i < ship.size; i++){
            if(horizontal){
                if(this.grid[y][x + i] != null){
                    System.out.println("The ship cannot be on the top of another");
                    return false;
                }
            }
            else{
                if(this.grid[y + i][x] != null){
                    System.out.println("The ship cannot be on the top of another");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Metodo que introduce un barco dentro del tablero
     * @param x identifica el eje horizontal
     * @param y identifica el eje vertical
     * @param horizontal booleano que define si el usuario quiere poner el barco en horizonal o no
     * @param ship barco seleccionado
     */
    public void setShips(int x, int y, boolean horizontal, Ship ship){

        /*posicionamos los barcos partiendo desde las coordenadas recibidas y añadiendo del eje x o y dependiendo de si
        el jugador ha activado el booleano horizontal*/
        for(int i = 0; i < ship.size; i++){
            if(horizontal){
                this.grid[y][x + i] = ship;
                ship.position[i] = new Point(x + i, y);
            }
            else{
                this.grid[y + i][x] = ship;
                ship.position[i] = new Point(x, y + i);
            }
        }
    }

    //Getters & Setters

    public void setGrid(Ship[][] grid){
        this.grid = grid;
    }
    public Impact[][] getBoard() {
        return this.board;
    }

    public Ship[][] getGrid() {
        return this.grid;
    }
}
