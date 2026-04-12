package gui;

import battle.Ship;
import player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlacementWindow {

    public void showPlacementWindow(Player player, IA cpu) {

        JFrame frame = new JFrame("Fleet Placement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();

        JLabel selectShip = new JLabel("Select Ship");
        controlPanel.add(selectShip);

        JComboBox<Ship> comboShip = new JComboBox<>(player.getShips());
        controlPanel.add(comboShip);

        JToggleButton orientationButton = new JToggleButton("Switch Orientation");
        orientationButton.addActionListener(e -> {
            if(orientationButton.isSelected()) {
                orientationButton.setText("Horizontal");
            }
            else {
                orientationButton.setText("Vertical");
            }
        });

        controlPanel.add(orientationButton);

        frame.add(controlPanel, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(10, 10));
        JButton[][] buttons = new JButton[10][10];

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                JButton button = new JButton();
                buttons[i][j] = button;
                int x = j;
                int y = i;

                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e){
                        Ship selectedShip = (Ship) comboShip.getSelectedItem();
                        if(selectedShip != null && button.isEnabled()) {
                            boolean isHorizontal = orientationButton.isSelected();
                            int size = selectedShip.getSize();
                            for(int a = 0; a < size; a++) {
                                int previewRow = 0;
                                int previewCol = 0;

                                if(isHorizontal) {
                                    previewRow = y;
                                    previewCol = x + a;
                                }
                                else{
                                    previewRow = y + a;
                                    previewCol = x;
                                }
                                if(previewRow < 10 && previewCol < 10 && buttons[previewRow][previewCol].isEnabled()) {
                                    buttons[previewRow][previewCol].setBackground(Color.LIGHT_GRAY);
                                }
                            }
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e){
                        Ship selectedShip = (Ship) comboShip.getSelectedItem();
                        if(selectedShip != null && button.isEnabled()) {
                            boolean isHorizontal = orientationButton.isSelected();
                            int size = selectedShip.getSize();
                            for(int a = 0; a < size; a++) {
                                int previewRow = 0;
                                int previewCol = 0;

                                if(isHorizontal) {
                                    previewRow = y;
                                    previewCol = x + a;
                                }
                                else{
                                    previewRow = y + a;
                                    previewCol = x;
                                }
                                if(previewRow < 10 && previewCol < 10 && buttons[previewRow][previewCol].isEnabled()) {
                                    buttons[previewRow][previewCol].setBackground(null);
                                }
                            }
                        }
                    }
                });
                button.addActionListener(e -> {
                    Ship selectedShip = (Ship) comboShip.getSelectedItem();
                    if(selectedShip != null) {
                        boolean isHorizontal = orientationButton.isSelected();
                        if(player.getNavalBattle().setPosition(selectedShip, x, y, isHorizontal)) {
                            for(int k = 0; k < selectedShip.getSize(); k++) {
                                if(isHorizontal){
                                    buttons[y][x + k].setBackground(Color.GRAY);
                                    buttons[y][x + k].setEnabled(false);

                                }
                                else {
                                    buttons[y + k][x].setBackground(Color.GRAY);
                                    buttons[y + k][x].setEnabled(false);

                                }
                            }
                            comboShip.removeItem(selectedShip);


                            if(comboShip.getItemCount() == 0){
                                JDialog dialog = new JDialog();
                                dialog.setLocationRelativeTo(null);
                                dialog.add(new JLabel("All Ships selected"));
                                JButton battle = new JButton("Start the Battle");
                                battle.addActionListener(u -> {
                                    dialog.dispose();
                                    frame.dispose();

                                    IAGameWindow iaGW = new IAGameWindow();

                                    iaGW.showIAGameWindow(player, cpu);
                                });
                                dialog.add(battle);
                                dialog.setSize(200, 200);
                                dialog.setVisible(true);
                            }
                        }
                    }
                });
                board.add(button);
            }
        }
        frame.add(board, BorderLayout.CENTER);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
