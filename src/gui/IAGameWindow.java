package gui;
import battle.Ship;
import player.*;
import utils.Impact;

import javax.swing.*;
import java.awt.*;

public class IAGameWindow {

    Player player = null;
    IA cpu = null;
    JTextArea log = null;
    JButton[][] playerButtons = new JButton[10][10];
    JButton[][] radarButtons = new JButton[10][10];
    JFrame frame = null;

    public void showIAGameWindow(Player player, IA cpu){
        this.player = player;
        this.cpu = cpu;

        frame = new JFrame();
        frame.setTitle("Battleships");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        log = new JTextArea(10, 25);
        log.setEditable(false);
        frame.add(new JScrollPane(log), BorderLayout.EAST);
        log.append("Naval Battle Begins!");

        JPanel board =  new JPanel(new GridLayout(2, 1, 10, 10));
        board.add(createRadarPanel());
        board.add(createPlayerPanel());

        frame.add(board, BorderLayout.CENTER);

        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createRadarPanel(){

        JPanel radarPanel = new JPanel(new GridLayout(10, 10));
        radarPanel.setBorder(BorderFactory.createTitledBorder("Radar"));

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                radarButtons[i][j] = button;
                int x = j;
                int y = i;
                button.setBackground(Color.BLUE);

                button.addActionListener(e -> {
                    cpu.getNavalBattle().shot(x, y);

                    button.setText("X");
                    button.setEnabled(false);

                    Object res = cpu.getNavalBattle().getBoard()[y][x];
                    Ship ship = cpu.getNavalBattle().getGrid()[y][x];

                    if(res == Impact.Hit){
                        button.setBackground(Color.RED);
                        log.append("\n" + player.getName() + ": Hit at (" + x + "," + y + ")");
                    }
                    else if(res == Impact.Sunk){
                        button.setBackground(Color.RED);
                        log.append("\n" + player.getName() + ": Sunk " + ship.getName() + " at (" + x + "," + y + ")");
                    }
                    else{
                        log.append("\n" + player.getName() + ": Miss at (" + x + "," + y + ")");
                    }

                    cpu.checkDefeated();
                    checkGameOver();

                    if(!cpu.getLost()){
                        iaTurn();
                    }
                });
                radarPanel.add(button);
            }
        }
        return radarPanel;
    }

    private JPanel createPlayerPanel(){

        JPanel playerPanel = new JPanel(new GridLayout(10, 10));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Fleet Status"));

        Ship[][] grid = player.getNavalBattle().getGrid();

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                playerButtons[i][j] = button;
                button.setEnabled(false);

                if(grid[i][j] != null){
                    button.setBackground(Color.GRAY);
                }
                playerPanel.add(button);
            }
        }
        return playerPanel;
    }

    private void iaTurn(){

        cpu.shot(player);

        Point lastShot = cpu.getShoted().get(cpu.getShoted().size() -1);
        int x = lastShot.x;
        int y = lastShot.y;

        Enum res = player.getNavalBattle().getBoard()[y][x];
        Ship ship = player.getNavalBattle().getGrid()[y][x];

        if(res == Impact.Hit){
            playerButtons[y][x].setText("X");
            playerButtons[y][x].setBackground(Color.RED);
            log.append("\n" + cpu.getName() + ": Hit at (" + x + "," + y + ")");
        }
        else if(res == Impact.Sunk){
            playerButtons[y][x].setText("X");
            playerButtons[y][x].setBackground(Color.RED);
            log.append("\n" + cpu.getName() + ": Sunk " + ship.getName() + " at (" + x + "," + y + ")");
        }
        else{
            playerButtons[y][x].setText("X");
            log.append("\n" + cpu.getName() + ": Miss at (" + x + "," + y + ")");
        }

        player.checkDefeated();
        checkGameOver();
    }

    private void checkGameOver(){

        if(cpu.getLost()){
            JDialog winDialog = new JDialog(frame, "Game Over", true);
            winDialog.setSize(200, 150);
            winDialog.setLocationRelativeTo(null);
            winDialog.setLayout(new GridLayout(2, 1, 10, 10));
            JLabel winLabel = new JLabel("You win!");
            winDialog.add(winLabel);

            JButton winButton = new JButton("Return Main Manu");
            winButton.addActionListener(e -> {
               winDialog.dispose();
               frame.dispose();

               UIMenu uiMenu = new UIMenu();
               uiMenu.showUIMenu();
            });
            winDialog.add(winButton);
            winDialog.setVisible(true);
        }
        if(player.getLost()){
            JDialog loseDialog = new JDialog(frame, "Game Over", true);
            loseDialog.setSize(200, 150);
            loseDialog.setLocationRelativeTo(null);
            loseDialog.setLayout(new GridLayout(2, 1, 10, 10));
            JLabel loseLabel = new JLabel("You lose!");
            loseDialog.add(loseLabel);

            JButton loseButton = new JButton("Return Main Manu");
            loseButton.addActionListener(e -> {
                loseDialog.dispose();
                frame.dispose();

                UIMenu uiMenu = new UIMenu();
                uiMenu.showUIMenu();
            });
            loseDialog.add(loseButton);
            loseDialog.setVisible(true);
        }
    }
}