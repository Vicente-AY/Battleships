package battle;

import java.awt.*;

public class Ship {

    String name = "";
    int size = 0;
    Point[] position = null;
    int hits = 0;
    boolean sunk = false;

    public Ship(String name, int size){
        this.name = name;
        this.size = size;
        this.position = new Point[size];
    }

    public void hit(){
        this.hits++;
        if(hits == size){
            this.sunk = true;
        }
    }

    @Override
    public String toString() {
        return this.name; // Así el JComboBox mostrará "Carrier", "Battleship", etc.
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
}
