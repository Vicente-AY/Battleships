package battle;

import java.awt.*;

public class Ship {

    int size = 0;
    Point[] position = null;
    int hits = 0;
    boolean sunk = false;

    public Ship(int size){
        this.size = size;
        this.position = new Point[size];
    }

    public void hit(){
        this.hits++;
        if(hits == size){
            this.sunk = true;
        }
    }

    //Getters & Setters

    public boolean getSunk(){
        return this.sunk;
    }
    public int getSize(){
        return this.size;
    }
}
