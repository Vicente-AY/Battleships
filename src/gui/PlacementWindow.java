package gui;

import battle.Ship;
import player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Clase que muestra la interfaz grafica para el posicionamiento de barcos.
 */
public class PlacementWindow {

    /**
     * Metodo que muestra la interfaz para e posicionamiento de los barcos
     * @param player usuario principal que posiciona los barcos
     * @param player2 usuario secundario, será nulo si es un juego contra el programa
     * @param cpu jugador IA, será nulo si es un juego contra otro jugador
     * @param isPvP booleano que determina si es un juagador contra el programa o contra otro jugador
     */
    public void showPlacementWindow(Player player, Player player2, IA cpu, boolean isPvP) {

        JFrame frame = new JFrame("Fleet Placement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();

        JLabel selectShip = new JLabel("Select Ship");
        controlPanel.add(selectShip);

        //Los jugadores podrán elegir el barco seleccionado
        JComboBox<Ship> comboShip = new JComboBox<>(player.getShips());
        controlPanel.add(comboShip);

        //boton que permite cambiar la orientación  a la que se posicionan los barcos
        JToggleButton orientationButton = new JToggleButton("Switch Orientation");
        orientationButton.addActionListener(e -> {
            if (orientationButton.isSelected()) {
                orientationButton.setText("Horizontal");
            } else {
                orientationButton.setText("Vertical");
            }
        });

        controlPanel.add(orientationButton);

        frame.add(controlPanel, BorderLayout.NORTH);

        /*creamos el panel de botones donde el jugador indicara la posición inicial de la que parte el posicionamiento
        del barco*/
        JPanel board = new JPanel(new GridLayout(10, 10));
        JButton[][] buttons = new JButton[10][10];

        //llenamos el panel de botones
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = new JButton();
                buttons[i][j] = button;
                int x = j;
                int y = i;

                button.addMouseListener(new MouseAdapter() {
                    /**
                     * Metodo que realiza una previsualización de donde quedaria el barco si el usuario decide esas
                     * coordenadas.
                     * @param e the event to be processed
                     */
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        Ship selectedShip = (Ship) comboShip.getSelectedItem();
                        //el comportamiento será diferente si el boton de selleccion de orientación esta activado
                        if (selectedShip != null && button.isEnabled()) {
                            boolean isHorizontal = orientationButton.isSelected();
                            int size = selectedShip.getSize();
                            /*recorremos todos los botones posibles donde podria posicionarse el barco aumentando el
                            eje x o y según este indicado*/
                            for (int a = 0; a < size; a++) {
                                int previewRow = 0;
                                int previewCol = 0;

                                if (isHorizontal) {
                                    previewRow = y;
                                    previewCol = x + a;
                                }
                                else {
                                    previewRow = y + a;
                                    previewCol = x;
                                }
                                /*si los valores previstos y los botones de esa prevision estén activos, indicamos
                                la previsualización del posicionamiento del barco con un gris claro*/
                                if (previewRow < 10 && previewCol < 10 && buttons[previewRow][previewCol].isEnabled()) {
                                    buttons[previewRow][previewCol].setBackground(Color.LIGHT_GRAY);
                                }
                            }
                        }
                    }

                    /**
                     * Metodo que elimina la prevision de posicionamiento una vez el usuario deje de posicionar el cursor
                     * encima del boton
                     * @param e the event to be processed
                     */
                    @Override
                    public void mouseExited(MouseEvent e) {
                        Ship selectedShip = (Ship) comboShip.getSelectedItem();
                        if (selectedShip != null && button.isEnabled()) {
                            boolean isHorizontal = orientationButton.isSelected();
                            int size = selectedShip.getSize();
                            //usamos el mismo metodo que antes pero devolviendo el boton su color original
                            for (int a = 0; a < size; a++) {
                                int previewRow = 0;
                                int previewCol = 0;

                                if (isHorizontal) {
                                    previewRow = y;
                                    previewCol = x + a;
                                }
                                else {
                                    previewRow = y + a;
                                    previewCol = x;
                                }
                                if (previewRow < 10 && previewCol < 10 && buttons[previewRow][previewCol].isEnabled()) {
                                    buttons[previewRow][previewCol].setBackground(null);
                                }
                            }
                        }
                    }
                });

                //Action listener que ejecuta el evento cuando el usuario hace click en un boton
                button.addActionListener(e -> {
                    Ship selectedShip = (Ship) comboShip.getSelectedItem();
                    if (selectedShip != null) {
                        boolean isHorizontal = orientationButton.isSelected();

                        /*ejecutamos el metodo de posicionamiento de barcos del tablero del jugador. Si este devuelve
                        true pintaremos los botones de gris para indicar el posicionamiento final del barco*/
                        if (player.getNavalBattle().setPosition(selectedShip, x, y, isHorizontal)) {
                            for (int k = 0; k < selectedShip.getSize(); k++) {
                                if (isHorizontal) {
                                    buttons[y][x + k].setBackground(Color.GRAY);
                                    buttons[y][x + k].setEnabled(false);

                                }
                                else {
                                    buttons[y + k][x].setBackground(Color.GRAY);
                                    buttons[y + k][x].setEnabled(false);
                                }
                            }
                            //eliminamos el barco seleccionado del JComboBox para evitar duplicados
                            comboShip.removeItem(selectedShip);

                            //si el listado de barcos llega a 0, acabamos el posicionamiento del jugador actual
                            if (comboShip.getItemCount() == 0) {
                                JDialog dialog = new JDialog(frame, "Placement Finished!", true);
                                dialog.setLayout(new FlowLayout());
                                dialog.setSize(250, 150);
                                dialog.setLocationRelativeTo(frame);
                                dialog.add(new JLabel("All Ships placed!"));

                                /*añadimos un nuevo boton que dependiendo del modo de juego y la fase, entrará en una
                                logica u otra*/
                                JButton nextButton = new JButton();
                                if(isPvP && player2 == null){
                                    nextButton.setText("Go to Player 2");
                                }
                                else{
                                    nextButton.setText("Start Battle!");
                                }

                                nextButton.addActionListener(u -> {
                                    dialog.dispose();
                                    frame.dispose();

                                    /*De ser pvp el modo elegido y el jugador2 ser nulo, añadimos un dialogo para
                                    registrar el nombre del segundo jugador*/
                                    if(isPvP && player2 == null){
                                        JDialog player2Dialog = new JDialog();
                                        player2Dialog.setTitle("Player 2 Data");
                                        player2Dialog.setLayout(new GridLayout(3, 1));
                                        JLabel nameLabel = new JLabel("Enter Player 2´s Name", SwingConstants.CENTER);
                                        JTextField nameField = new JTextField();
                                        JButton player2Button = new JButton("Accept");

                                        player2Button.addActionListener(r -> {
                                            String name = nameField.getText();
                                            //si el nombre no esta vacio ejecutaremos la ventana de posicionamiento
                                            if(!name.isEmpty()) {
                                                player2Dialog.dispose();
                                                Player secondPlayer = new Player(name);
                                                PlacementWindow pW = new PlacementWindow();
                                                pW.showPlacementWindow(secondPlayer, player, cpu, isPvP);
                                            }
                                        });

                                        player2Dialog.add(nameLabel);
                                        player2Dialog.add(nameField);
                                        player2Dialog.add(player2Button);

                                        player2Dialog.setVisible(true);
                                        player2Dialog.setSize(400, 400);
                                        player2Dialog.setLocationRelativeTo(null);
                                    }
                                    //si el modo es pvp y el jugador 2 ya no es nulo empezaremos el juego entre ambos
                                    else if(isPvP){
                                        VSPlayerGameWindow vsPlayerGameWindow = new VSPlayerGameWindow();
                                        vsPlayerGameWindow.createPlayersWindow(player, player2);
                                    }
                                    //si el modo de juego no es pvp empezaremos el juego con una IA
                                    else{
                                        IAGameWindow gameWindow = new IAGameWindow();
                                        gameWindow.showIAGameWindow(player, cpu);
                                    }
                                });

                                dialog.setSize(200, 200);
                                dialog.add(nextButton);
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