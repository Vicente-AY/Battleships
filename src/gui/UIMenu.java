package gui;

import player.IA;
import player.Player;

import javax.swing.*;
import java.awt.*;

public class UIMenu {

    public void showUIMenu(){

        JFrame frame = new JFrame();
        frame.setTitle("Battleships");
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("Battleships Main Menu");
        frame.add(title);

        JButton vsCPU = new JButton("Game Vs CPU");
        vsCPU.addActionListener(e -> {
            startVsCPU(frame);
        });
        frame.add(vsCPU);


        JButton vsPlayer = new JButton("Game Vs Player");
        /*vsPlayer.addActionListener(e -> {
            startVsPlayer();
        });*/
        frame.add(vsPlayer);

        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });
        frame.add(exit);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void startVsCPU(JFrame frame){

        JDialog dialog = new JDialog(frame, "Player Data", true);
        dialog.setSize(200,100);
        dialog.setLayout(new GridLayout(3,1));
        dialog.add(new JLabel("Enter the Player´s Name", SwingConstants.CENTER));
        JTextField nameField = new JTextField();
        dialog.add(nameField);

        JButton accept = new JButton("Accept");
        accept.addActionListener(e -> {
            String name = nameField.getText();
            if(!name.isEmpty()){
                dialog.dispose();
                frame.dispose();
                Player player = new Player(name);
                IA cpu = new IA();

                PlacementWindow pW = new PlacementWindow();

                pW.showPlacementWindow(player, cpu);
            }
        });
        dialog.add(accept);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
