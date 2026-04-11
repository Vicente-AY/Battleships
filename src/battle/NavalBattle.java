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

    public Impact shot(int x, int y){

        Ship ship = grid[x][y];

        if(ship != null){
            ship.hit();
            if(ship.getSunk()) {
                return Impact.Sunk;
            }
            else{
                return Impact.Hit;
            }
        }
        return Impact.Miss;
    }

    public void registerImpact(Impact impact, int x, int y){

        this.board[x][y] = impact;
    }

    public boolean canPlace(int x, int y, boolean horizontal, Ship ship){

        for(int i = 0; i < ship.size; i++){
            if(horizontal){
                if(this.grid[x + i][y] != null){
                    System.out.println("The ship cannot be on the top of another");
                    return false;
                }
            }
            else{
                if(this.grid[x][y + i] != null){
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
                this.grid[x + i][y] = ship;
                ship.position[i] = new Point(x + i, y);
            }
            else{
                this.grid[x][y + i] = ship;
                ship.position[i] = new Point(x, y + i);
            }
        }
    }

    //Getters & Setters

    public void setGrid(Ship[][] grid){
        this.grid = grid;
    }
}
