package gui;

import battle.Ship;
import player.IA;
import player.Player;
import utils.Impact;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class VSPlayerGameWindow {

    Player player1 = null;
    Player player2 = null;
    JTextArea log = null;
    JButton[][] player1Buttons = new JButton[10][10];
    JButton[][] player2Buttons = new JButton[10][10];
    JButton[][] player1RadarButtons = new JButton[10][10];
    JButton[][] player2RadarButtons = new JButton[10][10];
    JFrame player1Frame = null;
    JFrame player2Frame = null;
    TitledBorder player1RadarBorder;
    TitledBorder player1FleetBorder;
    TitledBorder player2RadarBorder;
    TitledBorder player2FleetBorder;
    JPanel player1RadarPanel;
    JPanel player1Panel;
    JPanel player2RadarPanel;
    JPanel player2Panel;

    public void createPlayersWindow(Player player1, Player player2){

        this.player1 = player1;
        this.player2 = player2;

        player1Frame = new JFrame();
        player1Frame.setTitle("Battleships");
        player1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player1Frame.setLayout(new BorderLayout());

        player2Frame = new JFrame();
        player2Frame.setTitle("Battleships");
        player2Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player2Frame.setLayout(new BorderLayout());

        log = new JTextArea(10, 25);
        log.setEditable(false);
        log.append("Naval Battle Begins!");
        player1Frame.add(log, BorderLayout.EAST);
        player2Frame.add(log, BorderLayout.EAST);

        JPanel player1Board = new JPanel();
        player1Board.add(createPlayer1RadarPanel());
        player1Board.add(createPlayer1Panel());
        player1Frame.add(player1Board, BorderLayout.CENTER);

        JPanel player2Board = new JPanel();
        player2Board.add(createPlayer2RadarPanel());
        player2Board.add(createPlayer2Panel());
        player2Frame.add(player2Board, BorderLayout.CENTER);

        player1Frame.setSize(800, 800);
        player1Frame.setLocationRelativeTo(null);
        player1Frame.setVisible(false);

        player2Frame.setSize(800, 800);
        player2Frame.setLocationRelativeTo(null);
        player2Frame.setVisible(false);
    }

    private JPanel createPlayer1RadarPanel(){

        this.player1RadarPanel = new JPanel(new GridLayout(10, 10));
        player1RadarBorder = BorderFactory.createTitledBorder("Radar " + countAlive(player2) + "/5 Ships Detected");
        player1RadarPanel.setBorder(player1RadarBorder);

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player1RadarButtons[i][j] = button;
                int x = j;
                int y = i;
                button.setBackground(Color.BLUE);

                button.addActionListener(e -> {
                    player2.getNavalBattle().shot(x, y);

                    button.setText("X");
                    button.setEnabled(false);

                    Impact res = player2.getNavalBattle().getBoard()[y][x];
                    Ship ship = player2.getNavalBattle().getGrid()[y][x];

                    if(res == Impact.Hit){
                        button.setBackground(Color.RED);
                        log.append("\n" + player1.getName() + ": Hit at (" + x + "," + y + ")");
                    }
                    else if(res == Impact.Sunk){
                        button.setBackground(Color.RED);
                        if(ship.getSize() == 5){
                            log.append("\n" + player1.getName() + ": A pique el portaaviones (" + x + "," + y + ")");
                        }
                        else{
                            log.append("\n" + player1.getName() + ": Sunk at (" + x + "," + y + ")");
                        }
                    }
                    else{
                        log.append("\n" + player1.getName() + ": Miss at (" + x + "," + y + ")");
                    }

                    refreshBorders();
                    updateSunkColor();
                    player2.checkDefeated();
                    checkGameOver();
                    player1Frame.setVisible(false);

                    JDialog dialog = new JDialog();
                    dialog.setSize(200, 150);
                    dialog.setLocationRelativeTo(null);
                    dialog.add(new JLabel("You made a " + res + " at " + x + "," + y));
                    JButton nextButton = new JButton(player2.getName() + "´s Turn");
                    nextButton.addActionListener(p -> {
                        player2Frame.setVisible(true);
                        dialog.dispose();
                    });
                });
                player1RadarPanel.add(button);
            }
        }
        return player1RadarPanel;
    }

    private JPanel createPlayer2RadarPanel(){

        this.player2RadarPanel = new JPanel(new GridLayout(10, 10));
        player2RadarBorder = BorderFactory.createTitledBorder("Radar " + countAlive(player1) + "/5 Ships Detected");
        player2RadarPanel.setBorder(player2RadarBorder);

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player2RadarButtons[i][j] = button;
                int x = j;
                int y = i;
                button.setBackground(Color.BLUE);

                button.addActionListener(e -> {
                    player1.getNavalBattle().shot(x, y);

                    button.setText("X");
                    button.setEnabled(false);

                    Impact res = player1.getNavalBattle().getBoard()[y][x];
                    Ship ship = player1.getNavalBattle().getGrid()[y][x];

                    if(res == Impact.Hit){
                        button.setBackground(Color.RED);
                        log.append("\n" + player2.getName() + ": Hit at (" + x + "," + y + ")");
                    }
                    else if(res == Impact.Sunk){
                        button.setBackground(Color.RED);
                        if(ship.getSize() == 5){
                            log.append("\n" + player2.getName() + ": A pique el portaaviones (" + x + "," + y + ")");
                        }
                        else{
                            log.append("\n" + player2.getName() + ": Sunk at (" + x + "," + y + ")");
                        }
                    }
                    else{
                        log.append("\n" + player2.getName() + ": Miss at (" + x + "," + y + ")");
                    }

                    refreshBorders();
                    updateSunkColor();
                    player1.checkDefeated();
                    checkGameOver();
                    player2Frame.setVisible(false);

                    JDialog dialog = new JDialog();
                    dialog.setSize(200, 150);
                    dialog.setLocationRelativeTo(null);
                    dialog.add(new JLabel("You made a " + res + " at " + x + "," + y));
                    JButton nextButton = new JButton(player1.getName() + "´s Turn");
                    nextButton.addActionListener(p -> {
                        player1Frame.setVisible(true);
                        dialog.dispose();
                    });
                });
                player2RadarPanel.add(button);
            }
        }
        return player2RadarPanel;
    }

    private JPanel createPlayer1Panel(){

        this.player1Panel = new JPanel(new GridLayout(10, 10));
        player1FleetBorder = BorderFactory.createTitledBorder("FleetStatus: " + countAlive(player1) + "/5 Ships Standing by");
        player1Panel.setBorder(player1FleetBorder);

        Ship[][] grid = player1.getNavalBattle().getGrid();

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player1Buttons[i][j] = button;
                button.setEnabled(false);

                if(grid[i][j] != null){
                    button.setBackground(Color.GRAY);
                }
                player1Panel.add(button);
            }
        }
        return player1Panel;
    }

    private JPanel createPlayer2Panel(){

        this.player2Panel = new JPanel(new GridLayout(10, 10));
        player2FleetBorder = BorderFactory.createTitledBorder("FleetStatus: " + countAlive(player2) + "/5 Ships Standing by");
        player2Panel.setBorder(player2FleetBorder);

        Ship[][] grid = player2.getNavalBattle().getGrid();

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player2Buttons[i][j] = button;
                button.setEnabled(false);

                if(grid[i][j] != null){
                    button.setBackground(Color.GRAY);
                }
                player2Panel.add(button);
            }
        }
        return player2Panel;
    }

    private int countAlive(Player player){

        int alive = 0;
        for(Ship ship : player.getShips()){
            if(!ship.getSunk()){
                alive++;
            }
        }
        return alive;
    }

    private void updateSunkColor(){

        for(Ship ship : player1.getShips()){
            if(ship.getSunk()){
                for(Point point : ship.getPosition()){
                    player1Buttons[point.y][point.x].setBackground(new Color(150, 0, 0));
                }
            }
        }

        for(Ship ship : player2.getShips()){
            if(ship.getSunk()){
                for(Point point : ship.getPosition()){
                    player2Buttons[point.y][point.x].setBackground(new Color(150, 0, 0));
                }
            }
        }
    }

    private void refreshBorders(){

        if(player1RadarBorder != null){
            player1RadarBorder = BorderFactory.createTitledBorder("Radar " + countAlive(player2) + "/5 Ships Detected");
            player1RadarPanel.setBorder(player1RadarBorder);
            player1RadarPanel.revalidate();
            player1RadarPanel.repaint();
        }
        if(player1FleetBorder != null){
            player1FleetBorder = BorderFactory.createTitledBorder("Fleet Status: " + countAlive(player1) + "/5 Ships Standing by");
            player1Panel.setBorder(player1FleetBorder);
            player1Panel.revalidate();
            player1Panel.repaint();
        }

        if(player2RadarBorder != null){
            player2RadarBorder = BorderFactory.createTitledBorder("Radar " + countAlive(player1) + "/5 Ships Detected");
            player2RadarPanel.setBorder(player2RadarBorder);
            player2RadarPanel.revalidate();
            player2RadarPanel.repaint();
        }
        if(player2FleetBorder != null){
            player2FleetBorder = BorderFactory.createTitledBorder("Fleet Status: " + countAlive(player2) + "/5 Ships Standing by");
            player2Panel.setBorder(player2FleetBorder);
            player2Panel.revalidate();
            player2Panel.repaint();
        }
    }

    private void checkGameOver(){

        String player1N = player1.getName();
        String player2N = player2.getName();

        if(player1.getLost()){
            JDialog player1WDialog = new JDialog(player2Frame, "Game Over", true);
            player1WDialog.setSize(200, 150);
            player1WDialog.setLocationRelativeTo(null);
            player1WDialog.setLayout(new GridLayout(2, 1, 10, 10));
            JLabel p1WinLabel = new JLabel(player1N + " Win!");
            player1WDialog.add(p1WinLabel);

            JButton winButton = new JButton("Return to Main Menu");
            winButton.addActionListener(e -> {
                player1WDialog.dispose();
                player1Frame.dispose();

                UIMenu uiMenu = new UIMenu();
                uiMenu.showUIMenu();
            });

            player1WDialog.add(winButton);
            player1WDialog.setVisible(true);
        }
        else if(player2.getLost()){
            JDialog player2WDialog = new JDialog(player2Frame, "Game Over", true);
            player2WDialog.setSize(200, 150);
            player2WDialog.setLocationRelativeTo(null);
            player2WDialog.setLayout(new GridLayout(2, 1, 10, 10));
            JLabel p2WinLabel = new JLabel(player2N + " Win!");
            player2WDialog.add(p2WinLabel);

            JButton winButton = new JButton("Return to Main Menu");
            winButton.addActionListener(e -> {
                player2WDialog.dispose();
                player2Frame.dispose();

                UIMenu uiMenu = new UIMenu();
                uiMenu.showUIMenu();
            });

            player2WDialog.add(winButton);
            player2WDialog.setVisible(true);
        }
    }

    public void newGame(){

        Random random = new Random();
        boolean turn = random.nextBoolean();
        if(turn){
            JDialog dialog = new JDialog();
            dialog.setSize(200, 150);
            dialog.setLocationRelativeTo(null);
            JButton nextButton = new JButton(player1.getName() + "´s Turn");
            nextButton.addActionListener(e -> {
                player1Frame.setVisible(true);
                dialog.dispose();
            });
        }
        else{
            JDialog dialog = new JDialog();
            dialog.setSize(200, 150);
            dialog.setLocationRelativeTo(null);
            JButton nextButton = new JButton(player2.getName() + "´s Turn");
            nextButton.addActionListener(e -> {
                player2Frame.setVisible(true);
                dialog.dispose();
            });
        }
    }
}
