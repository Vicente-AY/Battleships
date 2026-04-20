package gui;

import battle.Ship;
import player.IA;
import player.Player;
import utils.Impact;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

/**
 * Clase que inicia una partida entre dos jugadores
 */
public class VSPlayerGameWindow {

    Player player1 = null;
    Player player2 = null;
    JTextArea log1 = null;
    JTextArea log2 = null;
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

    /**
     * Metodo que crea los tableros de ambos jugadores
     * @param player1
     * @param player2
     */
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

        log1 = new JTextArea(10, 25);
        log1.setEditable(false);
        log1.append("Naval Battle Begins!");
        player1Frame.add(log1, BorderLayout.EAST);

        log2 = new JTextArea(10, 25);
        log2.setEditable(false);
        log2.append("Naval Battle Begins!");
        player2Frame.add(log2, BorderLayout.EAST);

        JPanel player1Board = new JPanel(new GridLayout(2, 1, 10, 10));
        player1Board.add(createPlayer1RadarPanel());
        player1Board.add(createPlayer1Panel());
        player1Frame.add(player1Board, BorderLayout.CENTER);

        JPanel player2Board = new JPanel(new GridLayout(2, 1, 10, 10));
        player2Board.add(createPlayer2RadarPanel());
        player2Board.add(createPlayer2Panel());
        player2Frame.add(player2Board, BorderLayout.CENTER);

        player1Frame.setSize(800, 800);
        player1Frame.setLocationRelativeTo(null);
        player1Frame.setVisible(false);

        player2Frame.setSize(800, 800);
        player2Frame.setLocationRelativeTo(null);
        player2Frame.setVisible(false);

        newGame();
    }

    /**
     * Metodo auxiliar que crea el panel radar del jugador1
     * @return radar del jugador 1
     */
    private JPanel createPlayer1RadarPanel(){

        this.player1RadarPanel = new JPanel(new GridLayout(10, 10));
        //borde que determina el estado de la flota enemiga
        player1RadarBorder = BorderFactory.createTitledBorder("Radar " + countAlive(player2) + "/5 Ships Detected");
        player1RadarPanel.setBorder(player1RadarBorder);

        //añadimos los botones para  el tablero
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player1RadarButtons[i][j] = button;
                int x = j;
                int y = i;

                //accion que realiza el boton cuando se hace click en el
                button.addActionListener(e -> {
                    player2.getNavalBattle().shot(x, y);

                    //el boton se marca con una X y se deshabilita
                    button.setText("X");
                    button.setEnabled(false);

                    //recogemos los tablero de juego del jugador2
                    Impact res = player2.getNavalBattle().getBoard()[y][x];
                    Ship ship = player2.getNavalBattle().getGrid()[y][x];

                    //si el disparo ha impactado pintamos el boton  de rojo
                    if(res == Impact.Hit){
                        //añadimos la X y el color rojo al tablero de estado del jugador 2
                        player2Buttons[y][x].setText("X");
                        player2Buttons[y][x].setBackground(Color.RED);

                        button.setBackground(Color.RED);
                        String message = player1.getName() + ": Hit at (" + x + "," + y + ")";
                        addLogs(message);
                    }
                    else if(res == Impact.Sunk){
                        player2Buttons[y][x].setText("X");
                        player2Buttons[y][x].setBackground(Color.RED);

                        button.setBackground(Color.RED);
                        if(ship.getSize() == 5){
                            String message = player1.getName() + ": A pique el portaaviones (" + x + "," + y + ")";
                            addLogs(message);
                        }
                        else{
                            player2Buttons[y][x].setText("X");
                            String message = player1.getName() + ": Sunk at (" + x + "," + y + ")";
                            addLogs(message);
                        }
                    }
                    //de fallar pintamos el boton de azul y el panel de posicion del jugador 2 del mismo color y su X
                    else{
                        player2Buttons[y][x].setText("X");
                        player2Buttons[y][x].setBackground(Color.BLUE);
                        button.setBackground(Color.BLUE);

                        String message = player1.getName() + ": Miss at (" + x + "," + y + ")";
                        addLogs(message);
                    }

                    refreshBorders();
                    updateSunkColor();
                    //llamamos al metodo que actualiza la situación del jugador
                    player2.checkDefeated();
                    //si ha perdido paramos la ejecución
                    boolean end = checkGameOver();
                    if(end){
                        return;
                    }
                    //quitamos la visibilidad del panel para cambiar de turno
                    player1Frame.setVisible(false);

                    //añadimos un dialogo con el resultado del disparo y añadimos un boton para qeu lo pulse el jugador2
                    JDialog dialog = new JDialog();
                    dialog.setSize(200, 150);
                    dialog.setLocationRelativeTo(null);
                    dialog.add(new JLabel("You made a " + res + " at " + x + "," + y));
                    JButton nextButton = new JButton(player2.getName() + "´s Turn");
                    //al pulsarlo cerramos el dialogo y mostramos el frame del jugador 2
                    nextButton.addActionListener(p -> {
                        player2Frame.setVisible(true);
                        dialog.dispose();
                    });
                    dialog.add(nextButton);
                    dialog.setVisible(true);
                });
                player1RadarPanel.add(button);
                player1RadarPanel.setVisible(true);
            }
        }
        return player1RadarPanel;
    }

    /**
     * Metodo que crea el panel de radar del jugador 2
     * @return panel de radar del jugador 2
     */
    private JPanel createPlayer2RadarPanel(){

        this.player2RadarPanel = new JPanel(new GridLayout(10, 10));
        //borde con la situación de los barcos del enemigo
        player2RadarBorder = BorderFactory.createTitledBorder("Radar " + countAlive(player1) + "/5 Ships Detected");
        player2RadarPanel.setBorder(player2RadarBorder);

        //llenamos el tablero de botones
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player2RadarButtons[i][j] = button;
                int x = j;
                int y = i;

                button.addActionListener(e -> {
                    player1.getNavalBattle().shot(x, y);

                    //el boton que pulsamos se marca con una X y se deshabilita
                    button.setText("X");
                    button.setEnabled(false);

                    Impact res = player1.getNavalBattle().getBoard()[y][x];
                    Ship ship = player1.getNavalBattle().getGrid()[y][x];

                    /*si el resultado del disparo es un hit se pinta de rojo el boton y el tablero del jugador1 ademas
                    se marca con una X*/
                    if(res == Impact.Hit){
                        player1Buttons[y][x].setText("X");
                        player1Buttons[y][x].setBackground(Color.RED);

                        button.setBackground(Color.RED);
                        String message = player2.getName() + ": Hit at (" + x + "," + y + ")";
                        addLogs(message);
                    }
                    else if(res == Impact.Sunk){
                        player1Buttons[y][x].setText("X");
                        player1Buttons[y][x].setBackground(Color.RED);

                        button.setBackground(Color.RED);
                        if(ship.getSize() == 5){
                            String message = player2.getName() + ": A pique el portaaviones (" + x + "," + y + ")";
                            addLogs(message);
                        }
                        else{
                            String message = player2.getName() + ": Sunk at (" + x + "," + y + ")";
                            addLogs(message);
                        }
                    }
                    //de fallar el boton y el tablero del jugador1 se pinta de azul y se marca con una x
                    else{
                        player1Buttons[y][x].setText("X");
                        player1Buttons[y][x].setBackground(Color.BLUE);

                        button.setBackground(Color.BLUE);

                        String message = player2.getName() + ": Miss at (" + x + "," + y + ")";
                        addLogs(message);
                    }

                    refreshBorders();
                    updateSunkColor();
                    //actualizamos la situación del jugador 1 tras el disparo
                    player1.checkDefeated();
                    //si ha perdido paramos la ejecución
                    boolean end = checkGameOver();
                    if(end){
                        return;
                    }
                    //para cambiar de turno ocultamos el panel del jugador 2 y le indicamos que ha ocurrido en su turno
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
                    dialog.add(nextButton);
                    dialog.setVisible(true);
                });
                player2RadarPanel.add(button);
                player2RadarPanel.setVisible(true);
            }
        }
        return player2RadarPanel;
    }

    /**
     * Metodo que crea el panel de estado de barcos propios del jugador1
     * @return panel de estado del jugador1
     */
    private JPanel createPlayer1Panel(){

        this.player1Panel = new JPanel(new GridLayout(10, 10));
        //borde con un resumen de la situacion de los barcos del jugador1
        player1FleetBorder = BorderFactory.createTitledBorder("FleetStatus: " + countAlive(player1) + "/5 Ships Standing by");
        player1Panel.setBorder(player1FleetBorder);

        Ship[][] grid = player1.getNavalBattle().getGrid();

        //añadimos botones al tablero
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player1Buttons[i][j] = button;
                button.setEnabled(false);

                //de encontrar barco en el tablero, pintamos el boton de gris
                if(grid[i][j] != null){
                    button.setBackground(Color.GRAY);
                }
                player1Panel.add(button);
            }
        }
        return player1Panel;
    }

    /**
     * metodo que crea el panel de estado del jugador2
     * @return penel de estado del jugador 2
     */
    private JPanel createPlayer2Panel(){

        this.player2Panel = new JPanel(new GridLayout(10, 10));
        //borde con la situación de los barcos propios
        player2FleetBorder = BorderFactory.createTitledBorder("FleetStatus: " + countAlive(player2) + "/5 Ships Standing by");
        player2Panel.setBorder(player2FleetBorder);

        Ship[][] grid = player2.getNavalBattle().getGrid();

        //añadimos botones al tablero y los deshabilitamos
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                player2Buttons[i][j] = button;
                button.setEnabled(false);

                //de encontrar barco en el tablero, pintamos el boton de gris
                if(grid[i][j] != null){
                    button.setBackground(Color.GRAY);
                }
                player2Panel.add(button);
            }
        }
        return player2Panel;
    }

    /**
     * Metodo que determina cuantos barcos activos tiene un jugador
     * @param player jugador del que se requiere la información
     * @return nuemor de barcos activos
     */
    private int countAlive(Player player){

        int alive = 0;
        for(Ship ship : player.getShips()){
            if(!ship.getSunk()){
                alive++;
            }
        }
        return alive;
    }

    /**
     * Metodo que cambia el color de los barcos si estos han sido hundidos
     */
    private void updateSunkColor(){

        for(Ship ship : player1.getShips()){
            if(ship.getSunk()){
                for(Point point : ship.getPosition()){
                    player1Buttons[point.y][point.x].setBackground(new Color(150, 0, 0));
                    player2RadarButtons[point.y][point.x].setBackground(new Color(150, 0, 0));
                }
            }
        }

        for(Ship ship : player2.getShips()){
            if(ship.getSunk()){
                for(Point point : ship.getPosition()){
                    player2Buttons[point.y][point.x].setBackground(new Color(150, 0, 0));
                    player1RadarButtons[point.y][point.x].setBackground(new Color(150, 0, 0));
                }
            }
        }
    }

    /**
     * Metodo que refresca la información contendia en los bordes de los jugadores
     */
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

    /**
     * Metodo que comprueba si cualquiera de los jugadores ha perdido
     * @return booleano si el juego ha terminado
     */
    private boolean checkGameOver(){

        String player1N = player1.getName();
        String player2N = player2.getName();
        JLabel winLabel = null;

        //Si uno de los jugadores ha perdido, se mostrará al ganador junto a un boton para volver al menu principal
        if(player1.getLost() ||  player2.getLost()){
            JDialog dialog = new JDialog(player2Frame, "Game Over", true);
            dialog.setSize(200, 150);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new GridLayout(2, 1, 10, 10));
            if(player1.getLost()) {
                winLabel = new JLabel(player2N + " Win!");
            }
            else{
                winLabel = new JLabel(player2N + " Win!");
            }
            dialog.add(winLabel);

            JButton winButton = new JButton("Return to Main Menu");
            winButton.addActionListener(e -> {
                dialog.dispose();
                player1Frame.dispose();
                player2Frame.dispose();

                UIMenu uiMenu = new UIMenu();
                uiMenu.showUIMenu();
            });

            dialog.add(winButton);
            dialog.setVisible(true);
            return true;
        }
        return false;
    }

    /**
     * Metodo que  determina que jugador empieza el juego
     */
    public void newGame(){

        JButton nextButton = null;
        Random random = new Random();
        boolean turn = random.nextBoolean();
        JDialog dialog = new JDialog();
        dialog.setSize(200, 150);
        dialog.setLocationRelativeTo(null);
        if(turn) {
            nextButton = new JButton(player1.getName() + "´s Turn");
            nextButton.addActionListener(e -> {
                player1Frame.setVisible(true);
                dialog.dispose();
            });
        }
        else{
            nextButton = new JButton(player2.getName() + "´s Turn");
            nextButton.addActionListener(e -> {
                player2Frame.setVisible(true);
                dialog.dispose();
            });
        }
        dialog.add(nextButton);
        dialog.setVisible(true);
    }

    /**
     * Metodo que añade el histroico de disparos a los logs de cada jugador
     * @param message
     */
    private void addLogs(String message){

        log1.append("\n" + message);
        log2.append("\n" + message);
    }
}
