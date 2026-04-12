package battle;

import utils.Impact;

import java.awt.*;

public class NavalBattle {

    Ship[][] grid = new Ship[10][10];
    Enum[][] board = new Enum[10][10];

    public boolean setPosition(Ship ship, int x, int y, boolean horizontal){

        int size = ship.size;

        if(horizontal && (x + size > 10)){
            System.out.println("The ship is out of the Naval Battle");
            return false;
        }
        if(!horizontal && (y + size > 10)){
            System.out.println("The ship is out of the Naval Battle");
            return false;
        }

        if(canPlace(x, y, horizontal, ship)){
            setShips(x, y, horizontal, ship);
            return true;
        }
        else {
            return false;
        }
    }

    public void shot(int x, int y){

        Ship ship = grid[y][x];

        if(ship != null){
            ship.hit();
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

    public boolean canPlace(int x, int y, boolean horizontal, Ship ship){

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

    public void setShips(int x, int y, boolean horizontal, Ship ship){

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
}
