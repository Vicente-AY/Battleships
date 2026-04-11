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
            playerTurn(player, cpu);
            if(checkLost(cpu.getShips())){
                System.out.println(player.getName() + " Won the game");
                break;
            }
            cpuTurn(player, cpu);
            if(checkLost(cpu.getShips())){
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

        for(int i = 0; i < player.getShips().length; i++){

            while(!shipSet) {
                Ship ship = player.getShips()[i];

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

    public void playerTurn(Player player, IA cpu){

        Scanner input = new Scanner(System.in);

        System.out.println("Enter X Coordinates");
        int x = input.nextInt();
        input.nextLine();
        System.out.println("Enter Y Coordinates");
        int y = input.nextInt();
        input.nextLine();

        cpu.getNavalBattle().shot(x, y);
    }

    public void cpuTurn(Player player, IA cpu){

        cpu.shot(player.getNavalBattle());
    }

    public boolean checkLost(Ship[] ships){

        for(Ship ship : ships){
            if(!ship.getSunk()){
                return false;
            }
        }
        return true;
    }
}
