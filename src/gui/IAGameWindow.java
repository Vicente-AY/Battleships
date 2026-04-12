package gui;
import player.*;

import javax.swing.*;
import java.awt.*;

public class IAGameWindow {

    Player player = null;
    IA cpu = null;
    JTextArea log = null;

    public void showIAGameWindow(Player player, IA cpu){
        this.player = player;
        this.cpu = cpu;

        JFrame frame = new JFrame();
        frame.setTitle("Battleships");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        log = new JTextArea();
        log.setEditable(false);
        frame.add(new JScrollPane(log), BorderLayout.EAST);

        JPanel board =  new JPanel();
        board.add(createRadarPanel());
        board.add(createPlayerPanel());

        frame.add(board, BorderLayout.CENTER);
    }

    private JPanel createRadarPanel(){

        JPanel radarPanel = new JPanel(new GridLayout(10, 10));
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                int x = j;
                int y = i;

                button.addActionListener(e -> {
                    cpu.getNavalBattle().shot(x, y);

                    button.setText("X");
                    button.setEnabled(false);
                    log.append(player.getName() + " shot at (" + x + "," + y + ")");
                });
                radarPanel.add(button);
            }
        }
        return radarPanel;
    }

    private JPanel createPlayerPanel(){

        JPanel playerPanel = new JPanel(new GridLayout(10, 10));
        for(int i = 0; i < 10; i++){
            JPanel cell = new JPanel();
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerPanel.add(cell);
        }
        return playerPanel;
    }
}