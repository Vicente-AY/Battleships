package gui;
import battle.Ship;
import player.*;
import utils.Impact;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class IAGameWindow {

    Player player = null;
    IA cpu = null;
    JTextArea log = null;
    JButton[][] playerButtons = new JButton[10][10];
    JButton[][] radarButtons = new JButton[10][10];
    JFrame frame = null;
    private TitledBorder radarBorder;
    private TitledBorder fleetBorder;
    private JPanel radarPanel;
    private JPanel playerPanel;

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

        this.radarPanel = new JPanel(new GridLayout(10, 10));
        radarBorder = BorderFactory.createTitledBorder("Radar " + countAliveCPU(cpu) + "/5 Ships Detected");
        radarPanel.setBorder(radarBorder);

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
                        if(ship.getSize() == 5){
                            log.append("\n" + player.getName() + ": A pique el portaaviones (" + x + "," + y + ")");

                        }
                        else {
                            log.append("\n" + player.getName() + ": Sunk " + ship.getName() + " at (" + x + "," + y + ")");
                        }
                    }
                    else{
                        log.append("\n" + player.getName() + ": Miss at (" + x + "," + y + ")");
                    }

                    refreshBorders();
                    updateSunkColor();
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

        this.playerPanel = new JPanel(new GridLayout(10, 10));
        fleetBorder = BorderFactory.createTitledBorder("Fleet Status: " + countAlivePlayer(player) + "/5 Ships Standing by");
        playerPanel.setBorder(fleetBorder);

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

        Impact res = player.getNavalBattle().getBoard()[y][x];
        Ship ship = player.getNavalBattle().getGrid()[y][x];

        if(res == Impact.Hit){
            playerButtons[y][x].setText("X");
            playerButtons[y][x].setBackground(Color.RED);
            log.append("\n" + cpu.getName() + ": Hit at (" + x + "," + y + ")");
        }
        else if(res == Impact.Sunk){
            playerButtons[y][x].setText("X");
            playerButtons[y][x].setBackground(Color.RED);

            if(ship.getSize() == 5){
                log.append("\n" + cpu.getName() + ": A pique el portaaviones (" + x + "," + y + ")");
            }
            else {
                log.append("\n" + cpu.getName() + ": Sunk " + ship.getName() + " at (" + x + "," + y + ")");
            }
        }
        else{
            playerButtons[y][x].setText("X");
            log.append("\n" + cpu.getName() + ": Miss at (" + x + "," + y + ")");
        }

        refreshBorders();
        updateSunkColor();
        player.checkDefeated();
        checkGameOver();
    }

    private int countAlivePlayer(Player player){

        int alive = 0;
        for(Ship ship : player.getShips()){
            if(!ship.getSunk()){
                alive++;
            }
        }
        return alive;
    }
    private int countAliveCPU(IA cpu){

        int alive = 0;
        for(Ship ship : cpu.getShips()){
            if(!ship.getSunk()){
                alive++;
            }
        }
        return alive;
    }

    private void updateSunkColor(){

        for(Ship ship : player.getShips()){
            if(ship.getSunk()){
                for(Point point : ship.getPosition()){
                    playerButtons[point.y][point.x].setBackground(new Color(150, 0, 0));
                }
            }
        }
        for(Ship ship : cpu.getShips()){
            if(ship.getSunk()){
                for(Point point : ship.getPosition()){
                    radarButtons[point.y][point.x].setBackground(new Color(150, 0, 0));
                }
            }
        }
    }

    private void refreshBorders(){
        if(radarBorder != null) {
            radarBorder = BorderFactory.createTitledBorder("Radar " + countAliveCPU(cpu) + "/5 Ships Detected");
            radarPanel.setBorder(radarBorder);
            radarPanel.revalidate();
            radarPanel.repaint();
        }
        if(fleetBorder != null) {
            fleetBorder = BorderFactory.createTitledBorder("Fleet Status: " + countAlivePlayer(player) + "/5 Ships Standing by");
            playerPanel.setBorder(fleetBorder);
            playerPanel.revalidate();
            playerPanel.repaint();
        }
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