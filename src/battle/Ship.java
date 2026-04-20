package battle;

import java.awt.*;

/**
 * Clase que simula un barco
 */
public class Ship {

    String name = "";
    int size = 0;
    Point[] position = null;
    int hits = 0;
    boolean sunk = false;

    /**
     * Constructor de la clase
     * @param name nombre del barco
     * @param size tamaño en posiciones que ocupa un barco en el tablero
     */
    public Ship(String name, int size){
        this.name = name;
        this.size = size;
        this.position = new Point[size];
    }

    /**
     * Metodo simula el recibimiento de un toque y determina si está hundido o no
     */
    public void hit(){
        this.hits++;
        if(hits == size){
            this.sunk = true;
        }
    }

    /**
     * Metodo que envia el la representación del objeto barco como un string
     * @return El nombre del barco
     */
    @Override
    public String toString() {
        return this.name;
    }

    //Getters & Setters

    public boolean getSunk(){
        return this.sunk;
    }
    public int getSize(){
        return this.size;
    }
    public String getName(){
        return this.name;
    }
    public Point[] getPosition(){
        return this.position;
    }
}
