package gui;

import player.IA;
import player.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que sirve para la visulalización del menú Grafico
 */
public class UIMenu {

    /**
     * Metodo que muestra el Menu Principal
     */
    public void showUIMenu(){

        JFrame frame = new JFrame();
        frame.setTitle("Battleships");
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("Battleships Main Menu");
        frame.add(title);

        //Al clicar en el siguiente boton ejecutaremos la logica que inicia el juego contra el programa
        JButton vsCPU = new JButton("Game Vs CPU");
        vsCPU.addActionListener(e -> {
            startVsCPU(frame);
        });
        frame.add(vsCPU);

        //Al clicar en el siguiente boton ejecutaremos la logica que inicia el juego contra otro jugador
        JButton vsPlayer = new JButton("Game Vs Player");
        vsPlayer.addActionListener(e -> {
            startVsPlayer(frame);
        });
        frame.add(vsPlayer);

        //boton usado para salir del programa
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });
        frame.add(exit);

        //este metodo centra el Jframe al centro
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Metodo previo para el inicio de un juego contra un jugador IA
     * @param frame JFrame inicial del menú sobre el que se posiciona el Jdialog
     */
    public void startVsCPU(JFrame frame){

        JDialog dialog = new JDialog(frame, "Player Data", true);
        dialog.setSize(200,100);
        dialog.setLayout(new GridLayout(3,1));
        dialog.add(new JLabel("Enter the Player´s Name", SwingConstants.CENTER));
        JTextField nameField = new JTextField();
        dialog.add(nameField);

        /*una vez pulado el boton aceptar, comprobamos que el Jfield no esta vacio, cerramos el Jframe y Jdialog y
        comenzamos con el posicionamiento de barcos por parte del jugador*/
        JButton accept = new JButton("Accept");
        accept.addActionListener(e -> {
            String name = nameField.getText();
            if(!name.isEmpty()){
                dialog.dispose();
                frame.dispose();
                Player player = new Player(name);
                IA cpu = new IA();

                //realizamos el posicionamiento de barcos del jugador cpu
                cpu.iaShipPositioning();

                frame.dispose();

                //visualizamos la pantalla del posicionamiento del jugador
                PlacementWindow pW = new PlacementWindow();
                Player player2 = null;
                boolean isPvP = false;
                pW.showPlacementWindow(player, player2, cpu, isPvP);
            }
        });
        dialog.add(accept);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    /**
     * Metodo previo para el comienzo de un juego contra otro jugador
     * @param frame
     */
    public void startVsPlayer(JFrame frame){

        JDialog dialog = new JDialog(frame, "Player Data", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(3,1));
        dialog.add(new JLabel("Enter the Player 1´s Name", SwingConstants.CENTER));
        JTextField name1Field = new JTextField();
        dialog.add(name1Field);

        JButton accept = new JButton("Player 1 Position Fleet");
        accept.addActionListener(e -> {
            String name1 = name1Field.getText();
            //iniciaremos el posicionamiento del jugador1 si el Jfield del nombre no está vacio
            if(!name1.isEmpty()){
                dialog.dispose();
                frame.dispose();
                Player player1 = new Player(name1);
                PlacementWindow pW = new PlacementWindow();

                Player player2 = null;
                IA cpu = null;
                boolean isPvP = true;
                pW.showPlacementWindow(player1, player2, cpu, isPvP);
            }
        });

        dialog.add(accept);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
