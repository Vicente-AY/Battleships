package game;

import battle.Ship;
import player.IA;
import player.Player;

import java.util.Scanner;

public class VsCPU {

    public void newGame(Player player, IA cpu){

        boolean cpuLost = false;
        boolean playerLost = false;

        System.out.println("Staring New Game");
        initialPositioning(player,cpu);

        while(true){
            player.shot(cpu);
            if(cpu.getLost()){
                System.out.println(player.getName() + " Won the game");
                break;
            }
            cpu.shot(player);
            if(player.getLost()){
                System.out.println(cpu.getName() + " Won the game");
                break;
            }
        }
    }

    public void initialPositioning(Player player, IA cpu){

        Scanner input = new Scanner(System.in);
        int x = 0, y = 0;
        boolean horizontal = false;
        boolean shipSet = false;
        Ship ship = null;

        for(int i = 0; i < player.getShips().length; i++){

            shipSet = false;

            while(!shipSet) {
                ship = player.getShips()[i];

                System.out.println("Positioning Ship: " + ship.getSize());

                System.out.println("Enter X Coordinates");
                x = input.nextInt();
                input.nextLine();

                System.out.println("Enter Y Coordinates");
                y = input.nextInt();
                input.nextLine();

                System.out.println("Horizontal Alignment?");
                horizontal = input.nextBoolean();

                shipSet = player.getNavalBattle().setPosition(ship, x, y, horizontal);

                if(!shipSet){
                    System.out.println("Invalid Coordinates");
                }
            }
        }

        for(int i = 0; i < cpu.getShips().length; i++){
            cpu.iaShipPositioning(cpu.getNavalBattle(), cpu.getShips()[i]);
        }
    }
}
