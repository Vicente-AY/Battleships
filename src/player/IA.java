package player;


import battle.NavalBattle;
import battle.Ship;
import utils.Impact;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class IA {

    String name = "A.D.M.I.R.A.L";
    NavalBattle navalBattle = null;
    ArrayList<Point> shoted = new ArrayList<Point>();
    Ship[] ships = new Ship[]{new Ship(1), new Ship(2), new Ship(3), new Ship(4), new Ship(5)};

    public void iaShipPositioning(NavalBattle navalB, Ship ship){

        Random rand = new Random();
        boolean horizontal = rand.nextBoolean();
        boolean cont = true;
        int x, y = 0;

        while(cont) {
            if (horizontal) {
                x = rand.nextInt(10 - ship.getSize());
                y = rand.nextInt(10);
            } else {
                x = rand.nextInt(10);
                y = rand.nextInt(10 - ship.getSize());
            }

            if (navalB.canPlace(x, y, horizontal, ship)) {
                navalB.setShips(x, y, horizontal, ship);
                cont = false;
            }
        }
    }

    public void shot(NavalBattle navalB) {

        boolean canShot = false;
        boolean pointShoted = false;
        Random rand = new Random();
        int x = 0, y = 0;

        while (!canShot) {
            x = rand.nextInt(10);
            y = rand.nextInt(10);

            Point aim = new Point(x, y);

            if (!shoted.contains(aim)) {
                canShot = true;
                Impact result = navalB.shot(x, y);
                shoted.add(aim);
                navalB.registerImpact(result, x, y);
            }
        }
    }
}
