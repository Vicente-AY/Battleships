package deprecated;

import player.IA;
import player.Player;

import java.util.Scanner;

/**
 * Clase deprecated por el añadido de la interfaz grafica. Usada como fase inicial del proyecto para juego por consola
 */
public class Menu {

    IA cpu = new IA();

    public void mainMenu(){

        Scanner input = new Scanner(System.in);
        int option = 0;

        while(true) {
            System.out.println("Welcome to Battleships!");
            System.out.println("1. Game Vs CPU | 2. Game VS Another Player | 3. Close Game");
            option = input.nextInt();
            input.nextLine();
            switch (option) {
                case 1:
                    System.out.println("Enter your Name");
                    String name = input.nextLine();
                    Player player = new Player(name);
                    VsCPU vsCPU = new VsCPU();
                    vsCPU.newGame(player, cpu);
                    break;
                case 2:
                    System.out.println("Enter Player 1´s Name");
                    String name1 = input.nextLine();
                    Player player1 = new Player(name1);
                    System.out.println("Enter Player 2´s Name");
                    String name2 = input.nextLine();
                    Player player2 = new Player(name2);
                    //logica de jugador vs jugador por hacer
                    break;
                case 3:
                    System.out.println("Closing Game");
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}
