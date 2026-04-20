package gui;
import battle.Ship;
import player.*;
import utils.Impact;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Clase que inicia una partida contra un jugador IA
 */
public class IAGameWindow {

    Player player = null;
    IA cpu = null;
    JTextArea log = null;
    JButton[][] playerButtons = new JButton[10][10];
    JButton[][] radarButtons = new JButton[10][10];
    JFrame frame = null;
    TitledBorder radarBorder;
    TitledBorder fleetBorder;
    JPanel radarPanel;
    JPanel playerPanel;

    /**
     * Metodo que muestra la interfaz de un juego contra un jugador IA
     * @param player usuario actual
     * @param cpu jugador IA
     */
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

    /**
     * Metodo que crea el panel de radar, que el usuario usara para indicar las coordenadas donde quiere realizar un
     * disparo
     * @return panel con los botones que se añadirá al JFrame principal de la clase
     */
    private JPanel createRadarPanel(){

        this.radarPanel = new JPanel(new GridLayout(10, 10));
        //border que indica el estado actual del rival
        radarBorder = BorderFactory.createTitledBorder("Radar " + countAliveCPU(cpu) + "/5 Ships Detected");
        radarPanel.setBorder(radarBorder);

        //creamos los botones del radar
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                radarButtons[i][j] = button;
                int x = j;
                int y = i;

                //accion que ejecutan los botones al hacer click en ellos
                button.addActionListener(e -> {
                    //realizamos el disparo
                    cpu.getNavalBattle().shot(x, y);

                    //barcamos el boton con una X y lo deshabilitamos para no repetir jugada
                    button.setText("X");
                    button.setEnabled(false);

                    //recogemos los tableros de barcos y resultados
                    Impact res = cpu.getNavalBattle().getBoard()[y][x];
                    Ship ship = cpu.getNavalBattle().getGrid()[y][x];

                    //Con cada dispaor indicamos en el log lo ocurrido
                    if(res == Impact.Hit){
                        //de ser un hit, pintamos el boton de rojo
                        button.setBackground(Color.RED);
                        log.append("\n" + player.getName() + ": Hit at (" + x + "," + y + ")");
                    }
                    else if(res == Impact.Sunk){
                        button.setBackground(Color.RED);
                        if(ship.getSize() == 5){
                            //Simpson referencia indispensable para el correcto funcionamiento del programa
                            log.append("\n" + player.getName() + ": A pique el portaaviones (" + x + "," + y + ")");

                        }
                        else {
                            log.append("\n" + player.getName() + ": Sunk " + ship.getName() + " at (" + x + "," + y + ")");
                        }
                    }
                    else{
                        //de ser un disparo fallidom, pintamos el boton de azul
                        button.setBackground(Color.BLUE);
                        log.append("\n" + player.getName() + ": Miss at (" + x + "," + y + ")");
                    }

                    refreshBorders();
                    updateSunkColor();
                    cpu.checkDefeated();
                    checkGameOver();

                    //despues del disparo comprobamos si la ia ha perdido, si no, realizamos con su turno
                    if(!cpu.getLost()){
                        iaTurn();
                    }
                });
                radarPanel.add(button);
            }
        }
        return radarPanel;
    }

    /**
     * Metodo que crea el panel del usuario para combrobar el estado de tablero
     * @return panel con el posicionamiento de los barcos del usuario
     */
    private JPanel createPlayerPanel(){

        this.playerPanel = new JPanel(new GridLayout(10, 10));
        //borde que refleja el estado de los barcos del usuario
        fleetBorder = BorderFactory.createTitledBorder("Fleet Status: " + countAlivePlayer(player) + "/5 Ships Standing by");
        playerPanel.setBorder(fleetBorder);

        //recogemos el tablero del jugador
        Ship[][] grid = player.getNavalBattle().getGrid();

        /*creamos los botones y los añadimos al tablero. Los inhabilitamos porque no están hechos para realizar
        ninguna acción*/
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton button = new JButton();
                playerButtons[i][j] = button;
                button.setEnabled(false);

                //si el tablero tiene un barco asignado en la posición actual lo pintamos de gris
                if(grid[i][j] != null){
                    button.setBackground(Color.GRAY);
                }
                playerPanel.add(button);
            }
        }
        return playerPanel;
    }

    /**
     * metodo que realiza el turno del jugador IA
     */
    private void iaTurn(){

        //realizamos un disparo
        cpu.shot(player);

        //recogemos el ultimo disparo efectuado y lo marcamos en el panel del jugador con una X
        Point lastShot = cpu.getShoted().get(cpu.getShoted().size() -1);
        int x = lastShot.x;
        int y = lastShot.y;
        playerButtons[y][x].setText("X");

        //recogemos los tableros del jugador
        Impact res = player.getNavalBattle().getBoard()[y][x];
        Ship ship = player.getNavalBattle().getGrid()[y][x];

        //si el resultado ha sido un impacto, pintamos el boton de rojo, de ser un disparo fallido lo pintamos de azul
        if(res == Impact.Hit){
            playerButtons[y][x].setBackground(Color.RED);
            log.append("\n" + cpu.getName() + ": Hit at (" + x + "," + y + ")");
        }
        else if(res == Impact.Sunk){
            playerButtons[y][x].setBackground(Color.RED);

            if(ship.getSize() == 5){
                log.append("\n" + cpu.getName() + ": A pique el portaaviones (" + x + "," + y + ")");
            }
            else {
                log.append("\n" + cpu.getName() + ": Sunk " + ship.getName() + " at (" + x + "," + y + ")");
            }
        }
        else{
            playerButtons[y][x].setBackground(Color.BLUE);
            log.append("\n" + cpu.getName() + ": Miss at (" + x + "," + y + ")");
        }

        refreshBorders();
        updateSunkColor();
        player.checkDefeated();
        checkGameOver();
    }

    /**
     * Metodo que devuelve el numero total de barcos a flote del jugador
     * @param player usuario actual
     * @return nuemor total de barcos en juego
     */
    private int countAlivePlayer(Player player){

        int alive = 0;
        for(Ship ship : player.getShips()){
            if(!ship.getSunk()){
                alive++;
            }
        }
        return alive;
    }

    /**
     * Metodo cuenta los barcos totals a flote del jugador IA
     * @param cpu jugador IA actual
     * @return el numero de barcos en juego
     */
    private int countAliveCPU(IA cpu){

        int alive = 0;
        for(Ship ship : cpu.getShips()){
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

    /**
     * Metodo que refresca los bordes tanto del radar como del panel de estado para que muestre datos actualizados
     */
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

    /**
     * Metodo que determina si el juego ha terminado
     */
    private void checkGameOver(){

        if(cpu.getLost() || player.getLost()){

            JLabel endLabel = null;

            JDialog winDialog = new JDialog(frame, "Game Over", true);
            winDialog.setSize(200, 150);
            winDialog.setLocationRelativeTo(null);
            winDialog.setLayout(new GridLayout(2, 1, 10, 10));
            //el mensaje cambiará dependiendo de si perdio el jugador IA o el usuario
            if(cpu.getLost()) {
                endLabel = new JLabel("You win!");
            }
            else{
                endLabel = new JLabel("You lose!");
            }
            winDialog.add(endLabel);

            JButton endButton = new JButton("Return Main Manu");
            endButton.addActionListener(e -> {
               winDialog.dispose();
               frame.dispose();

               UIMenu uiMenu = new UIMenu();
               uiMenu.showUIMenu();
            });
            winDialog.add(endButton);
            winDialog.setVisible(true);
        }
    }
}